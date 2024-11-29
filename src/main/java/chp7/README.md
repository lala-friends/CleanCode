# 7. 오류 처리
## 오류 코드보다 예외를 사용하라
* 오류코드 사용하는 방법
  * 사용자 호출 코드가 복잡해짐 - 함수를 호출한 즉시 오류를 확인해야 하기 때문
  * 오류가 발생하면 예외를 던지는게 낫다.
## Try-Catch-Finally 문부터 작성하라
* 먼저 강제로 예외를 발생시키는 테스트 케이스를 작성한 후 테스트를 통과하게 하는 코드를 작성해라
## 미확인(unchecked) 예외를 사용하라
* checked exception 과 unchecked exception
  * checked exception: 
    * 컴파일 타임에 강제로 처리해야 하는 예외
    * try catch 블록으로 처리하거나, 메서드 시그니처에 throws 를 선언하도록 강제
    * 개발자가 예상할 수 있는 예외상황
  * unchecked exception
    * 컴파일 타임에 처리하지 않아도 되는 예외
    * try-catch 블록을 강제하지 않으며, 런타임에 발생할 수 있다.
    * 개발자의 실수로 발생하는 예외
  * 확인된 예외는 OCP 를 위반한다.
    * 최하위 함수에서 새로운 오류를 던지면, 호출한 상위 함수에서 이를 처리해야 한다.
## 예외에 의미를 제공하라
* 오류 메시지에 정보를 담아 예외를 함께 던짐
  * 연산 이름, 실패 유형
## 호출자를 고려해 예외 클래스를 정의하라
* 오류를 분류하는 방법을 다양하지만, 호출하는 부분에서 오류를 사용하기 편한 방식으로 던지는 것이 좋다.
* 외부 API 같은 경우, 외부 API를 감싸는 감싸기 기법을 사용하면, 의존도가 떨어져서 좋다.
## 정상 흐름을 정의하라
```java
void test() {
  try {
      MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
      m_total += expenses.getTotal();
  } catch(MealExpensesNotFound e) {
    m_total = getMealPerDien(); // 이것 또한 정상 흐름
  }
} 
```
* 특수 사례 패턴
  * 클래스를 만들거나 객체를 조작해 특수 사례를 처리하는 방식
  * 클라이언코드가 예외적인 상황을 처리할 필요가 없다.
```java
public clss PerDiemMealExpenses implements MealExpenses {
    @Override
    public int getTotal() {
    }
  }
}
```
## null을 반환하지 마라
* 나쁜 코드 - null 확인을 계속 해야 함
  * null을 반환하고 싶다면, 특수 사례 패턴을 반환하면 됨
## null을 전달하지 마라
* 나쁜 코드 - 정상적인 인수로 null 을 기대하는 api가 아니라면, 피한다.
* 애초에 null을 넘기지 못하도록 금지하는 정책이 합리적
## 결론
* 깨끗한 코드는 읽기도 좋고, 안정성도 높아야 한다.
* 오류 처리를 프로그램 논리 처리와 분리해 독자적인 사안으로 처리