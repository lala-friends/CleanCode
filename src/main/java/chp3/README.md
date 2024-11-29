# 3. 함수
* 프로그램 초창기
  * 시스템을 루틴과 하위 뤼틴으로 나눔
* 포트란, PL/1
  * 시스템을 프로그램, 하위 프로그램, 함수로 나눔
* 현재
  * 함수만 살아남음
  * 어떤 프로그램이등 가장 기본적인 단위는 함수
* 학습목표
  * 함수를 잘 만드는 법
## 작게 만들어라!
* 작을 수록 좋다.
### 블록과 들여쓰기
* if 문 / else 문 / switch 문에 들어가는 블록은 한 줄이어야 한다.
  * 대게 함수를 호출
* 중첩 구조가 생길만큼 함수가 커서는 안된다.
* 함수의 들여 쓰기 수준은 1단, 2단을 넘어서면 안된다.
## 한가지만 해라
* 지정된 함수 이름 아래 추상화 수준이 하나인 단계만 수행한다면, 그 함수는 한가지 작업만 한다고 볼 수 있음
* 함수가 한가지만 하는지 판단하는 방법
  * 의미 있는 이름으로 다른 함수를 추출할 수 있다면, 그 함수는 여러가지 작업을 하는 것이다.
### 함수 내 섹션
* 함수 내 섹션이 있다는 건, 함수에서 여러 작업을 한다는 뜻
## 함수 당 추상화 수준은 하나로
* 한 함수 내에서 추상화 수준을 섞으면, 가독성이 떨어진다.
  * 특정 표현이 근본 개념인지 세부사항인지 헷갈리기 때문
* 추상화 수준 높음
  * getHtml();
* 추상화 수준 중간
  * String pagePathName = PathParser.render(pagePath);
* 추상화 수준 낮음
  * .append("\n)"
### 위에서 아래로 코드 읽기: 내려가기 규칙
* 한 함수 다음에는, 추상화 수준이 한 단계 낮은 함수가 온다.
## Switch 문
* 기본적으로 N가지 일을 처리, 작게 만들기 어려움
* 다형성을 이용
* 저자는 일반적으로, switch 문을 다형적인 객체를 생성하는 코드에서만 사용 
```java
// before
public Money calculatePay() throws InvalidEmployeeType {
    switch(e.type) {
        case COMMISIONED:
            return calculateCommisionedPay(e);
        case HOURLY:
            return calculateHourlyPay(e);
        case SALARIED:
            return calculateSalariedPay(e);
        default:
            throw new InvalidEmployeeType(e.type);
    }
}
```
```java
// after
public abstract class Employee {
    public abstract boolean isPayday();
    public abstract Money calculatePay();
    public abstract void deliveryPay(Money pay);
}

public interface EmployeeFactory {
    Employee makeEmployee(EmployeeRecord r) throws InvalidEmpoyeeType;
}

public class EmployeeFactoryImpl implements EmployeeFactory {
    public Employee makeEmployee(EmployeeRecord r) throws InvalidEmpoyeeType {
        switch (r.type) {
            case COMMISSIONED:
                return new CommissionedEmployee(r);
            case HOURLY:
                return new HourlyEmployee(r);
            case SALARIED:
                return new SalariedEmployee(r);
            default:
                throw new InvalidEmpoyeeType();
        }
    }
}

```
* 예시
  * 문제점
      * 문제점1. 함수가 길다
      * 문제점2. 한가지 작업만 수행하지 않는다.
      * 문제점3. SRP 를 위반 - 코드를 변경할 이유가 여럿
      * 문제점4. OCP 를 위반 - 새 유형의 직원을 추가할 떄마다 코드를 변경.
      * 문제점5. 구조가 동일한 함수가 무한 존재한다 - isPayday(Employee e, Date date), deliverPay(Employee e, Money pay)
  * 해결: Switch 문을 팩토리 메서드에 숨김
## 서술적인 이름을 사용하라!
* 길어도 되지만 일관성이 있어야 함
## 함수 인수
* 함수에서 이상적인 인수 개수는 0개이지만 2개까지 허용한다.
### 많이 쓰는 단항 형식
* 인수 한개를 넘기는 흔한 이유 두가지
  1. 인수에 질문을 던지는 경우
  ```java
  boolean fileExists("MyFile")
  ```
  2. 인수를 뭔가로 변환해 반환하는 경우 
  ```java
  InputStream fileOpen("MyFile")
  ```
* 드물게 사용되지만 유용한 이유
  3. 이벤트
  ```java
  passwordAttemptFailedNtimes(int attempts)
  ```
* 위에 설명한 경우가 아니면, 단항 함수는 가급적 피한다.
### 플래그 인수
* 함수가 한꺼번에 여러가지를 처리한다고 대놓고 공표하는 셈이므로 사용하지 않는다.
### 이항 함수
* 단항함수가 이해하기 쉬우니, 가급적이면 바꾸도록 해야 함
  * ex1)writeField(outputStream, name) -> outputStream.writeField(name)
  * ex2) FieldWriter 라는 새클래스를 만들어 outputStream 을 받고, write 메서드를 구현
    * final var fieldWriter = FieldWriter(outputStream); fieldWriter.write(name);
### 삼항 함수
### 인수 객체
* 인수가 2-3개 필요하다면, 일부를 독자적 클래스 변수로 선언할 가능성을 살펴본다.
### 인수 목록
* 인수 개수가 가변적인 함수
```java
String.format("%s worked %.2f hours.", name, hours); // 이상합수

public String format(String format, Object... args) {}
```
### 동사와 키워드
* 단항 함수는 함수와 인수가 동사/명사 쌍
  * ex) write(name), writeField(name)
* 함수 이름에 키워드를 추가하는 형식 (함수 이름에 인수 이름을 넣음)
  * ex) assertExpectedEqualsActual(expected, actual)
## 부수 효과를 일으키지 마라!
* 부수효과(Side Effect): 함수나 메서드가 본래의 목적(입력값을 받아 출력값을 반환하는 계산)을 수행하는 것 외에 추가적으로 상태를 변경하거나, 외부에 영향을 미치는 행위
* 많은 경우에 시간적 결합이나 순서 종속성을 초래
### 출력 인수
* 일반적으로 출력 인수 피하고, 함수가 속한 객체의 상태를 변경하는 방식을 택해야 한다.
  * ex) public void appendFooter(StringBuffer report) -> report.appendFooter()
## 명령과 조회를 분리하라!
## 오류 코드보다 예외를 사용하라!
* 명령함수에서 오류코드 반환은, 명령/조회 규칙 위반
* 오류 코드 대신 예외 사용하면, 오류 처리 코드가 원래 코드에서 분리되어 코드가 깔끔
```java
public void delete(Page page) {
    try {
        deletePage(page);
        registry.deleteRerfence(page.name);
        configKeys.deleteKey(page.name.makeKey());
    } catch (Exception e) {
        logger.log(e.getMessage());
    }
}
```

### Try/Catch 블록 뽑아내기
```java
public void delete(Page page) {
    try {
        deletePageAndAllReferences(page);
    } catch (Exception e) {
        logError(e);
    }
}

private void deletePageAndAllReferences(Page page) throws Exception {
    deletePage(page);
    registry.deleteRerfence(page.name);
    configKeys.deleteKey(page.name.makeKey());
}

private void logError(Exception e) {
    logger.log(e.getMessage());
}
```
### 오류 처리도 한 가지 작업이다.
* 따라서 오류를 처리하는 함수는 오류만 처리해야 한다.
### Error.java 의존성 자석
* 오류코드를 반환한다는 것은, 어디선가 오류코드를 정의한다는 뜻
  * Error enum 을 import
  * Error enum 이 변하면 사용하는 클래스 전부를 다시 컴파일하고 배치해야 한다.
* 예외를 사용하면, 새 예외는 Exception 을 상속받아 구현하므로 재컴파일/재배치 없이도 추가할 수 잇따.
## 반복하지 마라!
* 중복은 악의 근원
## 구조적 프로그래밍
* 다익스트라의 구조적 프로그래밍 원칙
  * 모든 함수와 함수 내 모든 블록에 입구와 출구가 하나만 존재해야 한다. 
  * 즉, 함수는 return 문이 하나여야 한다.
  * 루프 안에서 break, continue 안됨, goto 절대 안됨
* 작은 함수에서는, goto 문만 절대 안됨
## 함수를 어떻게 짜죠?
* 작성 후 리팩토링
## 결론
* 모든 시스템은 응용 분야 시스템을 기술할 목적으로, 프로그래머가 설계한 도메인 특화 언어로 만들어 진다.
* 함수는 동사, 클래스는 명사
* 대가는 시스템을 프로그램이 아니라 이야기로 여긴다.
