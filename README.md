## 다양한 상황에서의 Spring @Transactional 동작 예제

### Concept
이 프로젝트는 spring transaction manager 및 JPA가 다양한 상황에서 어떻게 각기 다르게 동작하는지 확인하기 위해 만들어졌습니다.
1. propagation level에 따른 트랜잭션 전파 및 예외처리, 전역 롤백 등
2. @Async, TaskExecutor 등을 활용한 비동기 트랜잭션 수행에서의 동작 및 예외처리
3. @TransactionalEventListner에서의 영속성 전이, 1차 캐시 공유 관련 사항

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
