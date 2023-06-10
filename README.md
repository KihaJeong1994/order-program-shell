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
- command : 
- common
- domain

### test
```bash
└── order
    └── service
        └── OrderServiceTest.java

```

## 프로젝트 구현 방향