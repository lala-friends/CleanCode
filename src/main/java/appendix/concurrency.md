# 부석A. 동시성2

## 클라이언트/서버 예제
```java
public class Server implements Runnable {
    // ...
    @Override
    public void run() {
        System.out.println("Server started");

        while (keepProcessing) {
            try {
                System.out.println("accepting client");
                final var socket = serverSocket.accept();
                System.out.println("get client");
                process(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```
```java
class TrivialClient implements Runnable {
        private final int clientNumber;

        public TrivialClient(final int clientNumber) {
            this.clientNumber = clientNumber;
        }

        @Override
        public void run() {
            try {
                connectSendReceive(clientNumber);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        private void connectSendReceive(final int i) throws IOException {
            System.out.printf("Client %2d: connecting\n", i);
            final var socket = new Socket("127.0.0.1", PORT);
            System.out.printf("Client %2d: sending message\n", i);
            MessageUtils.sendMessage(socket, Integer.toString(i));
            System.out.printf("Client %2d: getting reply\n", i);
            MessageUtils.getMessage(socket);
            System.out.printf("Client %2d: finished\n", i);
            socket.close();
        }
    }
```
```java
public class ClientTest {
    @Test(timeout = 10000)
    public void shouldRunInUnder10Seconds() throws InterruptedException {
        final var threads = createThreads();
        startAllThreads(threads);
        waitForAllThreadsToFinish(threads);
    }
}
```
* 10초 내에 처리하지 못하는 실패하는 코드
* 단일 스레드에서 속도를 끌어 올리는 방법
  * 거의 없음
* 다중 스레드를 사용하면
  * I/O - 소켓 사용, 데이터베이스 연결, 가상 메모리 스와핑 등에 시간을 보내는 경우
    * 한쪽이 I/O 연산에 시간을 보내는 동안, 다른 쪽이 뭔가를 처리해 노는 CPU를 효과적으로 활용
  * 프로세서 - 수치 계산, 정규 표현식 처리, 가비지 컬렉션 등에 시간을 보내는 경우
    * 하드웨어 추가해 성능을 높여야 함
### 서버
### 스레드 추가하기
```java
public class Server implements Runnable {
    // ...
    private void process(final Socket socket) {
        if (socket == null) {
            return;
        }

        final var clientHandler = new Runnable() {
            @Override
            public void run() {
                try {
                    final var message = MessageUtils.getMessage(socket);
                    Thread.sleep(1000);
                    MessageUtils.sendMessage(socket, "Processed: " + message);
                    closeIgnoringException(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        final var clientConnection = new Thread(clientHandler);
        clientConnection.start();
    }
}
```
* 문제점: 서버 코드가 가지는 책임이 너무 많음 -> 단일 책임 원칙 위반
  * 소켓 연결 관리 
  * 클라이언트 처리 
  * 스레드 정책 
  * 서버 종료 정책 
### 서버 살펴보기
* process 함수의 책임 분할
  * 소켓 연결 관리 - ClientConnection
  * 클라이언트 처리 - ClientRequestProcessor
  * 스레드 정책 - ClientScheduler
  * 서버 종료 정책 - ClientConnection
### 결론
* 단일 쓰레드 -> 다중 스레드로 변환하여 시스템의 성능을 높이는 방법
* 테스트 프레임워크에서 시스템 성능을 검증하는 방ㅂ법
* 단일 책임 원칙
  * 동시성은 그 자체가 복잡하기 때문에, 단일 책임 원칙이 특히 중요하다.

## 가능한 실행 경로
### 경로 수
### 가능한 순열 수 계산하기
### 심층 분석
### 결론
* 루프나 분기가 없년 명령 N개, 스레드 T개가 차례로 실행한다면
  * (NT)! / N! ... N! (T개)

## 라이브러리를 이해하라
### Executor 프레임워크
* 스레드 풍을 관리하고, 자동으로 플 크기를 조정하고, 필요하다면 스레드 재사용
### 스레드를 차단하지 않는 방법
* synchronized
  * 비관적인락
* AtomicBoolean, AtomicInteger, AtomicReference 를 사용
  * 낙관적인 락
  * 충돌이 잘 일어나지 않는다는 가정하에 사용
  * 충돌이 일어났을 경우, CAS(Compare and Swap) 연산
    * 알려진 값과 현재 변수의 값 확인하고 같다면 갱신
### 다중 스레드 환경에서 안전하지 않은 클래스

## 메서드 사이에 존재하는 의존성을 조심하라
### 실패를 용인한다.
* 비추
### 클라이언트 기반 잠금
* 비추
### 서버 기반 잠금 - 추천
* 서버에 동시성 관련 코드가 캡슐화 되므로
  * 코드 중복이 줄어 든다. 클라이언트에 잠금 코드를 추가할 필요 없다.
  * 성능이 좋아진다. 단일 스레드 환경으로 시스템을 배치할 경우, 서버만 교체하면 오버헤드가 줄어 든다.
  * 오류 발생 가능성이 줄어 든다.
  * 스레드 정책이 하나다.
  * 고유 변수 범위가 줄어든다.
* 서버 코드에 손을 대지 못한다면
  * ADAPTER 패턴을 사용해 API 를 변경한 후 잠금을 추가
  * 스레드에 안전하며 인터페이스가 확장된 집합 클래스를 사용한다.
    * Collections.synchronizedSet(Set<E> originalSet)
    * ConcurrentHashMap<K, V>
    * ConcurrentSkipListSet<E>

## 작업 처리량 높이기
### 작업 처리량 계산 - 단일 스레드 환경
### 작업 처리량 계산 - 다중 스레드 환경

## 데드락
* 데드락의 원인 - 아래 네가지 조건을 모두 만족하면 데드락이 발생한다.(네 가지 조건 중 하나라도 깨면, 데드락 발생하지 않음)
  * 상호 배제
  * 잠금 & 대기
  * 선점 불기
  * 순환 대기
### 상호 배체
* 여러 스레드가 한 자원을 공유하나 그 자원은
  * 여러 스레드가 동시에 사용하지 못하며
  * 개수가 제한적
* ex) 데이터베이스 연결, 쓰기용 파일 열기, 레코드 락, 세마포어 등
### 잠금 & 대기
* 일단 스레드가 자원을 점유하면, 필요한 나머지 자원까지 모두 점유해 작업을 마칠 때까지 자원을 내놓지 않는다.
### 선점 불가
* 한 스레드가 다른 스레드로 부터 자원을 뺏지 못한다.
### 순환 대기
* 죽음의 포옹
* T1, T2 쓰레드가 있고 자원 R1, R2가 있다. 두 쓰레드 모두 R1, R2 가 필요한데 T1이 R1, T2가 R2를 점유하고 있으면 무한대기

### 상호 배제 조건 깨기
* 동시에 사용해도 괜찮은 자원을 사용 
* 스레드 수 이상으로 자원 수를 늘림
* 자원을 점유하기 전에, 필요한 자원이 모두 있는지 확인
### 잠금 & 대기 조건 깨기
* 자원을 점유하기 전에, 필요한 모든 자원이 있는지 확인
* 어느 하나라도 점유하지 못하면 자원을 전부 반환
* 문제점
  * 기아 - 한 스레드가 계속해서 필요한 자원을 점유하지 못함
  * 라이브락 - 여러 스레드가 한꺼번에 잠금 단계로 진입하는 바람에, 계속 자원을 점위 했다 내놨다를 반복
### 선점 불가 조건 깨기
* 다른 스레드로부터 자원을 뺏어오는 방법
* 관리가 쉽지 않음
### 순환 대기 조건 깨기
* 데드락을 방지하는 흔한 전략
* 모든 스레드가 일정 순서에 동의하고 그 순서대로 자원 할당
* 문제점
  * 자원을 할당하는 순서와 사용하는 순서가 다를 수 있다.
  * 순서에 따라 자원을 할당하기 어려운 경우도 있다.

## 다중 스레드 코드 테스트
* 버그 찾아내기 무척 어려움 

## 스레드 코드 테스트를 도와주는 도구
* IBM의 ConTest -> 최신환경에서는 잘 사용되지 않는다고 함

## 결론
* 동시 갱신, 동시 갱신을 방지하는 깨끗한 동기화 잠금 기법
* 스레드가 I/O 위주 시스템의 처리율을 높여주는 이유, 실제로 처리율을 높이는 방법
* 데드락, 데드락 방지하는 기법
* 보조코드를 추가해 동시성 문제를 사전에 노출하는 방법

## 자습서: 전체 코드 예제 - 디렉토리 참조
### 클라이언트/서버 - 단일 스레드 버전
### 클라이언트/서버 - 다중 스레드 버전
