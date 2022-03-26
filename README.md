## 다양한 상황에서의 Spring @Transactional 동작 예제

### Concept
이 프로젝트는 spring transaction manager 및 JPA가 다양한 상황에서 어떻게 동작하는지 확인하기 위해 만들어졌습니다.
1. propagation level에 따른 트랜잭션 전파 및 예외 처리, 전역 롤백 등
2. @Async, TaskExecutor 등을 활용한 비동기 트랜잭션 수행에서의 업데이트 및 예외 처리
3. EventListner에서의 영속성 전이, 1차 캐시 공유 관련 사항 및 예외 처리

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
