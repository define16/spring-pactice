# Kafka와 GraphQL Subscription을 이용한 실시간 알림 시스템

## 1. 프로젝트 개요

본 프로젝트는 **Spring Boot**, **Kafka**, **GraphQL Subscription**을 활용하여 특정 이벤트가 발생했을 때, 해당 이벤트를 구독하는 클라이언트에게 실시간으로 푸시 알림을 보내는 기능을 구현한 예제입니다.

- **`demo` API**: 외부로부터 데이터 생성/수정 요청을 받고, 변경 사항이 발생하면 Kafka 토픽으로 메시지를 발행(Produce)합니다.
- **`board` 모듈**: Kafka 토픽의 메시지를 구독(Consume)하여 데이터를 처리하고, GraphQL Subscription을 통해 클라이언트에게 실시간으로 변경 내용을 전송합니다.

## 2. 기술 스택

- **Backend**: `Java 21`, `Spring Boot 3.5.3`
- **Messaging**: `Spring Kafka`
- **API**: `Spring Web`, `Spring for GraphQL`
- **Database**: `MariaDB`, `Spring Data JPA`
- **Build Tool**: `Gradle`
- **Container**: `Docker` (Kafka, Kafka-UI)
- **Etc**: `Lombok`, `Project Reactor`

## 3. 실행 방법

### 사전 요구사항

- `Java 21`
- `Docker`

### 실행 순서

1.  **Kafka 실행**
    프로젝트 루트 디렉토리에서 아래 명령어를 실행하여 Kafka와 Kafka-UI를 실행합니다.

    ```bash
    docker-compose up -d
    ```

    -   **Kafka Broker**: `localhost:9092`
    -   **Kafka UI**: `http://localhost:9090`

2.  **Spring Boot 애플리케이션 실행**
    IDE 또는 Gradle 명령어를 통해 Spring Boot 애플리케이션을 실행합니다.

## 4. API 명세

### 4.1. REST API

#### 📝 Demo API (`DemoController`)

`demo` 데이터의 생성, 수정, 조회를 담당하며, 데이터 생성 시 Kafka로 메시지를 발행합니다.

**POST** `/demo`

-   **설명**: 새로운 `demo` 데이터를 생성하고, 생성된 데이터 정보를 Kafka의 `demo-topic`으로 발행합니다.
-   **Request Body**:
    ```json
    {
      "headline": "새로운 데모 헤드라인",
      "content": "데모 콘텐츠 내용입니다."
    }
    ```
-   **Response**: `Long` - 생성된 `demo` 데이터의 ID
    ```
    1
    ```

**GET** `/demo/{id}`

-   **설명**: ID에 해당하는 `demo` 데이터를 조회합니다.
-   **Path Variable**: `id` (Long) - 조회할 데이터의 ID
-   **Response**: `String` - `demo` 데이터의 메시지

**PUT** `/demo/{id}`

-   **설명**: ID에 해당하는 `demo` 데이터를 수정합니다.
-   **Path Variable**: `id` (Long) - 수정할 데이터의 ID
-   **Request Body**:
    ```json
    {
      "headline": "수정된 데모 헤드라인",
      "content": "수정된 콘텐츠 내용입니다."
    }
    ```
-   **Response**: `Long` - 수정된 `demo` 데이터의 ID

####  게시판 Board API (`BoardController`)

**GET** `/board/{id}`

-   **설명**: ID에 해당하는 `board` 데이터의 헤드라인을 조회합니다.
-   **Path Variable**: `id` (Long) - 조회할 데이터의 ID
-   **Response**: `String` - `board` 데이터의 헤드라인

### 4.2. GraphQL API

#### 📡 실시간 게시판 알림 (`BoardController`)

`demo` 데이터가 생성되어 Kafka 메시지가 발행되면, 이를 감지하여 구독 중인 클라이언트에게 실시간으로 `PushMessage`를 전송합니다.

**Subscription** `boardSubscription`

-   **설명**: 특정 `boardId`를 구독하여 해당 게시판에 변경 사항이 생길 때마다 실시간으로 데이터를 수신합니다.
-   **GraphQL Query**:
    ```graphql
    subscription BoardSubscription($boardId: Long!) {
      boardSubscription(boardId: $boardId) {
        id
        headline
        savedAt
      }
    }
    ```
-   **Argument**: `boardId` (Long!) - 구독할 게시판의 ID
    ```Json
    {"boardId": 1}
    ```
-   **Response Stream**: `PushMessage`
    ```json
    {
      "data": {
        "boardSubscription": {
          "id": 1,
          "headline": "새로운 데모 헤드라인",
          "savedAt": "2025-07-27T14:30:00.123Z"
        }
      }
    }
    ```
    -   `id`: 게시판 ID
    -   `headline`: 게시판 헤드라인
    -   `savedAt`: 저장 시각 (DateTime)

## 5. 프로젝트 동작 흐름

1.  클라이언트가 `POST /demo` API를 호출하여 새로운 데이터를 생성합니다.
2.  `DemoController`는 요청을 받아 `DemoService`를 통해 데이터를 DB에 저장합니다.
3.  `DemoProducer`는 `demo-topic`으로 `DemoCreatedMessage` (id, headline 포함)를 Kafka에 발행(produce)합니다.
4.  `BoardConsumer`는 `demo-topic`을 구독(consume)하고 있다가 메시지를 수신합니다.
5.  수신한 메시지를 바탕으로 `BoardService`를 통해 `Board` 데이터를 생성/처리합니다.
6.  `BoardService`는 `BoardSubscriptionResolver`를 호출하여 구독자에게 보낼 `PushMessage`를 발행합니다.
7.  `boardSubscription`을 구독하고 있던 클라이언트는 실시간으로 `PushMessage`를 수신하게 됩니다.
