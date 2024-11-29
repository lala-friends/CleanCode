# 10. 클래스
## 클래스 체계
* 변수 목록
  1. 정적 공개 상수
  2. 정적 비공개 변수
  3. 비공개 인스턴스 변수
  4. 공개 함수
  5. 비공개 함수는 자신을 호출하는 공개 함수 직후에 넣는다.(추상화 단계가 순차적으로 내려감)
### 캠슐화
* 변수와 유틸리티 함수는 가능한 공개하지 않는 편이 낫지만 테스트를 위해 protected 로 선언하기도 함 
## 클래스는 작아야 한다!
* 얼마나 작아야 하나?
  * 함수: 물리적인 행수
  * 클래스: 클래스가 맡은 책임의 수
  * 클래스의 이름은 해등 클래스 책임을 기술해야 한다.
    * 간결한 이름이 떠오르지 않는다면, 클래스가 너무 많은 책임을 갖는 것
  * 클래스의 설명은 if, and, or, but 을 사용하지 않고 25단어 내외로 가능해야 함
### 단일 책임 원칙
* 단일 책임 원칙(Single Responsibility Principle, SRP)
  * 클래스나 모듈을 변경할 이유가 하나, 단 하나 뿐이어야 한다.
* 책임, 즉 변경할 이유를 파악하려 애쓰다 보면, 코드를 추상화 하기 쉬워진다.
* 큰 클래스 몇개가 아니라 작은 클래스 여럿으로 이뤄진 시스템이 더 바람직
### 응집도
* 클래스는 인스턴스 변수 수가 작아야 한다.
* 클래스의 메서드는 클래스 인스턴스 변수를 하나 이상 사용해야 한다.
  * 메서드가 변수를 많이 사용ㅇ할 수록, 메서드와 클래스는 응집도가 높다.
* 응집도가 높다
  * 클래스의 속한 메서드와 변수가 서로 의존하며, 논리적인 단위로 묶인다.
* 일부 메서드만 사용하는 인스턴수가 많아지면, 클래스를 쪼개야 한다는 신호
### 응집도를 유지하면 작은 클래스 여럿이 나온다.
* 큰 함수 일부를 작은 함수로 분리
  * 작은 함수가 큰 함수의 변수 넷을 사용
  * 인수로 넘기지 말고, 인스턴스 변수로 승격
    * 응집도가 떨어짐, 몇몇함수(해당하는 큰함수와 작은함수)만 사용하는 변수가 늘어났기 때문
    * 독자적인 클래스로 분
* 예시
  * before
    * PrinterPrime
  * after
    * PrimePrinter: main 함수, 실행환경
    * RowColumnPagePrinter: 숫자 목록을 주어진 행과 열에 맞춰 페이지에 출력
    * PrimeGenerator: 소수 목록을 생성
## 변경하기 쉬운 클래스
* before
  * SRP 위반
    * 새 SQL문을 지원하려면, 반드시 Sql 클래스에 손대야 한다.
    * 기존 SQL문을 수정할 때도 반드시 Sql 클래스에 손대야 한다.
    * selectWithCriteria 라는 비공개 메서드는 select
* after
  * SRP 준수
  * OCP 준수
### 변경으로부터 격리
* 요구사항은 변한다
* 클래스
  * 구체적인 클래스: 상세한 구현 포함
  * 추상 클래스: 개념만 포함
* 구현에 의존하는 코드는 테스트가 어려움
  * before
    * Portfolio 클래스에서 TokyoStockExchange API를 사용해서 포트폴리오 값을 계산
    * 테스트 코드는 시세변화의 영향을 받는다.
  * after
    * StockExchange 인터페이스를 통해 TokyoStockExchange 주입
      * DIP(Dependency Inversion Principle, DIP): 클래스가 추상화에 의존해야 한다.
    * 시스템 결함도가 낮아짐
      * 유연성, 재사용성 높아짐
    ```java
    public interface StockExchange {
        Money currentPrince(String symbol);
    }
    
    public class Portfolio {
        private StockExchange exchange;
        public Portfoloio(StockExchange exchnage) {
            this.exchange = exchange;
        }
    }
    
    public class PortfolioTest {
        private FixedStockExchangeStub exchange;
        private Portfolio portfolio;
        @Before
        void setUp() throws Exception {
            exchange = new FixedStockExchangeStub();
            exchange.fix("MSFT", 100);
            portfolio = new Portfolio(exchange);
        }
    }
    ```