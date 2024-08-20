# stock-concurrency-issue

## 재고 시스템을 통해 동시성 이슈 해결 해보기

### ⚙ 프로젝트 개발 환경
- 통합개발환경 :Intellij
- JDK 버전 : JDK 17
- Spring Boot 버전 : 3.3.2
- 사용 DB : MySQL 8.0.31
- 빌드툴 : Gradle
- 관리툴 : Git, GitHub

### ⚒ 기술 스택
- Backend
  - Spring Boot
  - Spring Web
  - Spring Data JPA

### ❔동시성 해결 방법
- RDB 를 이용한 방법
  - Pessimistic Lock
    - 실제 데이터에 Lock 을 걸어 정합성을 맞추는 방법
    - Row 또는 Table 단위로 Lock 을 거는 방법
    - Exclusive Lock 을 걸게되면 다른 트랜잭션에서는 Lock 이 해제되기 전에 데이터를 가져갈 수 없음.
    - 데드락이 걸릴 수 있으므로 사용시 주의해야함
  - Optimistic Lock
    - 실제 Lock 을 사용하지 않고, Version 을 사용해 정합성을 맞추는 방법
    - 데이터를 읽고 update 를 수행할 때, 현재 내가 읽은 버전이 맞나 확인 후 업데이트
    - 만약 내가 읽은 버전에서 수정사항이 생겼을 경우, Application 에서 다시 읽고 작업을 수행해야 함.
  - Named Lock
    - 이름을 가진 Lock 을 획득한 후, 해제 할 때 까지 다른 세션이 Lock 을 획득하지 못하도록 하는 방법
    - Metadata 에 Lock 을 하는 방법
    - Transaction 이 종료될 때 Lock 이 자동으로 해제되지 않음
    - 별도의 명령어로 Lock 을 해제 해주거나, 선점시간이 끝나야 함
- Redis 를 이용한 방법
  - Lettuce
    - setnx 명령어를 이용해 분산락 구현
    - Spin Lock 방식이여서 Retry Logic 개발자가 직접 구현 해줘야 함. (구현이 간단함)
    - 동시에 많은 Thread 가 Lock 획득 대기 상태이면 Redis 에 부하가 발생할 수 있어 구현 시 조심해야 함.
  - Redisson
    - pub-sub 기반으로 Lock 구현 제공
    - 채널을 만들어 Lock 을 점유중인 Thread 가 Lock 을 획득하려고 대기하는 Thread 에게 Lock 해제를 알려주면 안내받은 Thread 가 Lock 을 획득하는 방식.
    - 별도의 라이브러리 의존성 추가 해줘야 함.
    - 별도의 Retry Logic 작성 안해줘도 되고 Lock 을 라이브러리 차원에서 지원 해주기 때문에 사용법을 공부해야 함.