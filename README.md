## 다양한 상황에서의 Spring @Transactional 동작 예제

### Concept
이 프로젝트는 spring transaction manager 및 JPA가 다양한 상황에서 어떻게 동작하는지 확인하기 위해 만들어졌습니다.
1. propagation level에 따른 트랜잭션 전파 및 예외 처리, 전역 롤백 등
2. @Async, TaskExecutor 등을 활용한 비동기 트랜잭션 수행에서의 업데이트
3. EventListner에서의 영속성 전이, 1차 캐시 공유 관련 사항 및 예외 처리
4. JPQL과 1차캐시 동작 관련

등 다양한 상황에서 실제로 트랜잭션이 어떻게 수행되는지 확인 후 이론적인 내용을 검증하고
몰랐던 내용이나 특이 사항에 대해서 정리할 예정입니다.

#### launch mysql with docker and create a test schema
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
### 트랜잭션 전파 레벨에 따른 예외처리

- 부모-자식 관계의 트랜잭션은 자식 트랜잭션 전파레벨에 따라 다음과 같이 동작한다.
    - progagation = `REQUIRES` 인 경우
        - 부모 트랜잭션에서 catch한 경우 → rollback 마킹에 의해 전역롤백
        - 자식 트랜잭션에서 catch한 경우 → 롤백하지 않음
    - propagation = `REQUIRES_NEW`를 적용한 경우
        - 부모 트랜잭션에서 catch한 경우 → 자식 트랜잭션은 롤백됨
        - 자식 트랜잭션에서 catch한 경우 → 롤백하지 않음
    - 트랜잭션이 아닌 경우
        - 부모/자식 어디서간 예외가 잡히기만 하면 롤백하지 않음

### 다른 스레드에서의 영속성 객체 업데이트

트랜잭션이 진행중인 메인 스레드에서 다른 스레드로 영속성 객체를 전달하고, 전달받은 스레드에서 이를 수정하는 경우를 가정해보자. 이 때는 스레드에서 호출된 함수의 `@Transactional` 여부와 관계없이 기본적으로 dirty-checking이 이루어지지 않는다. 영속성 객체가 다른 스레드로 넘어가는 순간 detached 상태가 되기 때문이다. 메인 함수가 커밋 전 스레드를 대기한다면 부모 트랜잭션에서의 dirty-checking으로 변경 사항이 커밋되긴 하지만, 추천하는 방법은 아니다. 함수 호출마다 동작이 달라질 수 있기 때문이다.

### 영속성 객체 반환 시 유의점

일반 함수 A, `@Transactional` B, `@Transactional` C가 있다고 가정한다.

B에서 조회한 엔티티를 A가 반환받는 경우 해당 엔티티는 영속성 컨텍스트에서 detached된다는 것을 기억해야 한다. 엔티티에 수정을 가하는 C에 해당 영속성 객체를 인자로 전달하더라도, 수정 값은 커밋되지 않는다.

A가 `@Transactional`인 경우에도 B가 `@Transactional(propagation = REQUIRES_NEW)`라면 반환 값이 detached 된다.

### REQUIRED_NEW에서의 동작

부모 트랜잭션 A, A가 호출하는 자식 트랜잭션 B가 있다고 가정한다.

- A와 B가 동일한 객체를 조회해서 수정한다면 무조건 A의 엔티티 상태로 overriding 된다.
    - 최종 커밋 시점에서 바라보는 엔티티의 상태가 A에서의 상태이기 때문이다.
- B에서 조회 및 수정한 객체를 A에서 받아서 수정한다고 해도 수정된 정보는 DB에 반영되지 않는다.
    - 위에 설명했듯이 `REQUIRES_NEW`에서 반환되면서 영속성 컨텍스트에서 detached되기 때문이다.
