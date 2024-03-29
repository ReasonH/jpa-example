## 다양한 상황에서의 spring data jpa (및 JPA) 동작 테스트

### Concept
spring transaction manager 및 JPA가 다양한 상황에서 어떻게 동작하는지 확인합니다.

#### 예시
1. propagation level에 따른 트랜잭션 전파 및 예외 처리, 전역 롤백 등
2. @Async, TaskExecutor 등을 활용한 비동기 트랜잭션 수행에서의 업데이트
3. EventListner에서의 영속성 전이, 1차 캐시 공유 관련 사항 및 예외 처리
4. JPQL과 1차캐시 동작 관련

#### launch mySQL with docker and create a test schema
```bash
./run-db.sh
```

#### run the application runner
```bash
./gradlew bootRUn
```

#### run test
```bash
./gradlew test
```

---

## 트랜잭션 관련 정리

### 트랜잭션 전파 레벨에 따른 RuntimeException 롤백 처리 (RuntimeExceptionOuterServiceTest)

부모-자식 관계의 트랜잭션은 자식 트랜잭션 전파레벨에 따라 다음과 같이 동작한다.
- progagation = `REQUIRES` 인 경우
    - 부모 트랜잭션에서 catch한 경우 → rollback 마킹에 의해 전역롤백
    - 자식 트랜잭션에서 catch한 경우 → 롤백하지 않음
- propagation = `REQUIRES_NEW`를 적용한 경우
    - 부모 트랜잭션에서 catch한 경우 → 자식 트랜잭션은 롤백됨
    - 자식 트랜잭션에서 catch한 경우 → 롤백하지 않음
- 트랜잭션이 아닌 경우
    - 부모/자식 트랜잭션 구분없이 예외가 잡히기만 하면 롤백하지 않음

### 다른 스레드에 전달된 영속성 객체 `UpdateAsyncOuterServiceTest`

트랜잭션이 진행중인 메인 스레드에서 비동기 동작하는 함수로 영속성 객체를 전달하고, 해당 함수 내에서 영속성 객체를 수정하는 경우를 가정해보자. 
이 때는 함수의 `@Transactional` 여부와 관계없이 dirty-checking이 이루어지지 않는다. 
영속성 객체가 다른 스레드로 넘어가는 순간 detached 상태가 되기 때문이다. 
> 메인 스레드 함수가 종료(커밋)되기 전, 비동기 함수 내의 영속성 객체 수정 로직이 동작했다면 dirty-checking이 이루어지고 변경 사항이 저장될 수 있다. 
> 그러나 함수 호출마다 동작이 달라질 수 있기에 유의해야 한다.

> 메서드가 호출한 곳과 별도의 스레드라면 전파 레벨과 관계 없이 무조건 별도의 트랜잭션을 생성한다. (REQUIRES_NEW와 동일) Spring은 트랜잭션 정보를 ThreadLocal 변수에 저장하기에 다른 스레드로 트랜잭션 전파가 일어나지 않는다.

### 트랜잭션이 종료된 영속성 객체 반환 시 동작 `UpdateOuterServiceTest`

> - 일반 함수 A
> - `@Transactional` 함수 B

가 있다고 가정한다.

B에서 조회한 엔티티를 A가 반환받는 경우 해당 엔티티는 영속성 컨텍스트에서 detached된다는 것을 기억해야 한다. 

- `@Transactional` 함수 A
- `@Transactional(propagation = REQUIRES_NEW)` 함수 B

인 경우에도 동일하게 동작한다.

이미 detach된 객체는 다른 트랜잭션에 전달하더라도 다시 영속화되지 않는다.

### 이벤트 리스너에서의 영속성 `EventListenerTest`

- @EventListener에서 이벤트 객체 내에 엔티티를 직접 전달해서 업데이트 하는 경우 모든 것은 일반 함수 호출과 동일하게 동작한다.
- @TransactionalEventListener는 기본적으로 트랜잭션 커밋 이후 수행된다.
    - 만약 이벤트로 영속성 객체를 전달한다면 전달받은 곳에서 해당 객체는 managed 상태이다.
      - 동기로 처리되는 경우 lazy loading을 지원한다.
      - 이는 이벤트를 퍼블리싱하는 함수, 이벤트 핸들러가 어떤 트랜잭션 레벨이어도 동일하다.
      - 커밋 이후 동작하기 때문에 수정/쓰기는 동작하지 않는다.

### 1차 캐시와 Flush `FirstCacheTest`

@Transactional에서는 다음과 같은 상황이 벌어질 수 있다.

> 1. A라는 유니크 컬럼에 a라는 값이 지정되어 있는 영속성 객체 X가 존재
> 2. X의 A컬럼 값을 a → b로 수정
> 3. Y라는 엔티티를 생성해서 A컬럼 값을 a로 지정
> 4. Y를 영속화하려 하면 unique column duple로 인한 Exception 발생

이는 변경감지가 반영되지 않은 상태에서 이미 DB에 존재하는 유니크 값으로 엔티티를 저장하려 해서 발생하는 예외이다. dirty-checking은 기본적으로 flush 단계에서 수행되며 flush는 commit시 자동으로 호출된다.
이때 flush 내에서도 쿼리가 반영되는 순서가 다른데, Hibernate에서는 다음과 같은 순서를 따르고 있다.

- `OrphanRemovalAction`
- `AbstractEntityInsertAction`
- `EntityUpdateAction`
- `QueuedOperationCollectionAction`
- `CollectionRemoveAction`
- `CollectionUpdateAction`
- `CollectionRecreateAction`
- `EntityDeleteAction`

순서를 확인해보면 UpdateAction은 InsertAction의 뒤에 있는 것을 확인할 수 있다. 즉, 기존 유니크 컬럼을 업데이트하는 동작이 insert 구문보다 뒤에 수행되기 때문에 SQL Exception이 발생한 것이다.

참고: https://vladmihalcea.com/hibernate-facts-knowing-flush-operations-order-matters/

이를 방지하기 위해서는 기존 객체의 유니크 컬럼 수정 후, 해당 내용을 즉시 DB에 동기화시키는 saveAndFlush()를 호출해야 한다.  

### 1차 캐시와 JPQL `FirstCacheTest`

- JPQL 쿼리는 수행 직전 flush를 호출한다. 따라서, JPQL을 사용할 때는 JPQL 실행 전 변경된 1차 캐시의 값들이 이미 DB에 반영된 상태라는 것을 신경써야 한다.
- JPQL 수행 시, DB를 조회한 이후 해당 데이터를 1차 캐시에 저장 시도하는데, 해당 데이터의 식별자가 이미 컨텍스트에 존재하는 경우 새로운 값을 버리게 된다.

  예시는 다음과 같다.

      1. 팀-멤버의 1:N 양방향 관계에서 신규 Team, Member를 만들고, Member에 Team 정보 삽입
      2. fetch join을 통해 팀-멤버 정보 조회
      3. Team.members.size() 조회 결과는 0

  이 때 문제 해결 방법은 연관관계 편의 메서드를 통해 Member의 Team 삽입 시, Team의 Member도 추가하게 만드는 것이다.
