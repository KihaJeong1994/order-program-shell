# [29cm 백엔드 과제 정기하]

## 실행 방법

### 프로그램 빌드
```bash
./gradlew build
```

### 프로그램 실행
```bash
java -jar ./build/libs/homework-0.0.1-SNAPSHOT.jar
```

- command line에서 작동하는 프로그램을 만들기 위해 `spring-shell-starter` 라이브러리를 활용하였습니다.
- 그러나, 개발 진행 도중 Intellij 환경에서 해당 라이브러리로 만든 프로그램을 IDE 환경에서 Debug 하기 힘든 이슈를 발견했습니다. https://github.com/spring-projects/spring-shell/wiki/Debugging
- 위 링크에 따르면, Intellij는 jvm input/output stream에 직접 연결하여 `spring-shell-starter`로 만든 프로그램을 디버깅하기 부적합하다고 나와있습니다.
- 따라서 IDE로 VSCode를 활용하거나, 프로그램을 빌드 후 실행시켜서 테스트를 해야합니다.
---

## 프로젝트 구조

### main
```bash
├── HomeworkApplication.java
├── command
│   ├── OrderCommand.java
│   ├── QuitCommand.java
│   ├── handler
│   │   └── CommandNotFoundResultHandler.java
│   └── prompt
│       ├── CustomPromptProvider.java
│       └── StringInputCustomRenderer.java
├── common
│   ├── csv
│   │   └── CSVDataLoader.java
│   └── exception
│       ├── CustomException.java
│       ├── NoSuchProductException.java
│       └── SoldOutException.java
└── domain
    ├── order
    │   ├── entity
    │   │   └── Order.java
    │   └── service
    │       └── OrderService.java
    └── product
        ├── entity
        │   └── Product.java
        ├── repository
        │   └── ProductRepository.java
        └── service
            └── ProductService.java
```
프로젝트는 크게 3가지 패키지로 구성되어있습니다.
- command : 프로그램에 사용되는 명령어(`@ShellComponent`)들이 있습니다. 이 프로그램의 경우 주문과 종료 명령어가 있습니다.
- common : 공통으로 사용되는 기능들이 있습니다.
- domain : 각각의 도메인들이 하위 패키지로 존재합니다. 이 프로그램의 경우 주문과 상품 도메인이 있습니다.

### test
```bash
└── order
    └── service
        └── OrderServiceTest.java

```
multi thread 요청으로 SoldOutException이 작동하는 지 테스트하는 코드가 있습니다.

## 프로젝트 구현 방향

### 비즈니스 로직과 기타 기능의 분리
비즈니스 로직과 기타 기능(csv 데이터 동기화, 명령어 처리)를 분리하여 비즈니스 로직 구현에 집중할 수 있도록 하였습니다.

**비즈니스 로직**
- Product(상품) : 상품 데이터를 가져오고, 저장하고, 재고를 수정합니다.
- Order(주문) : 주문을 수행합니다. 주문이 수행되면 상품 재고를 변경하기 때문에 상품 서비스에 의존해있습니다.
- `spring data jpa`를 사용하여 보다 간편하게 java 어플리케이션에서 영속성 관리를 개발했습니다.

**기타기능**
- csv의 데이터를 초기 데이터로 활용해 기동 시 in memory db에 저장하였습니다.
- `spring-shell-starter` 라이브러리를 사용하여 주문, 종료 명령어 작동을 위한 로직을 분리하였습니다.
- command 패키지는 일반 MVC 패턴의 controller 처럼, service를 주입받아 주로 service의 메소드를 호출하고 결과를 출력하는 것에 집중하였습니다.


### 트랜잭션 처리와 동시성 이슈 관리

**트랜잭션 처리**
- 트랜잭션 처리를 용이하게 하고 의존성 관리를 위해 Spring 프레임워크를 사용했습니다.
- 여러 상품에 대한 주문을 완료하고 결제를 실행할 때, 상품의 재고가 변경되는데, 하나의 상품에라도 재고 부족(SoldOutException)이 발생하면 나머지 재고 변동을 취소해야하여, 하나의 주문을 하나의 트랜잭션으로 묶어 관리하였습니다.

**동시성 이슈 관리**
- 테스트 시나리오 : 한 개의 상품에 대해 동시에 전량을 주문헀을 때, 하나의 주문은 성공하고, 다른 주문은 재고부족 예외가 발생해야합니다.
- main thread말고 별도의 thread 풀에서 2개의 thread로 동시에 주문을 수행하여, SoldOutException이 발생하는 지 테스트했습니다.
- 동시성 이슈 관리 방법 : 재고 감소를 위한 상품 조회 시, 해당 데이터에 대해 비관적 lock을 걸었습니다. 이렇게 되면 여전히 상품에 대한 조회는 가능하지만, 수정을 위해 transaction을 시작한 조회는 lock이 풀리기를 기다려야합니다.
- 비관적 lock으로 인한 성능 감소와 dead lock 이슈를 피하기 위해 lock 획득시간을 설정하였습니다.

**기타 예외 처리**
- 주문, 종료 이외의 명령어 입력 시 정해진 명령어 요구
- 없는 상품번호 번호 입력 시 바른 상품번호 입력 요구
- 상품번호, 수량 입력시 숫자 입력 요구
- 0 이상의 수량 입력 요구
등의 예외를 추가적으로 처리하였습니다.