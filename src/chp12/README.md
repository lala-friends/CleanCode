# 12. 창밟성

* 창발성(創發性, Emergence)
  * 시스템이나 집단에서 개별 구성 요소들이 상호작용하면서, 각 요소가 독립적으로는 가질 수 없는 새롭고 독특한 특성이나 행동이 나타나는 현상을

## 창발적 설계로 깔끔한 코드를 구현하자
* 켄트 백이 제시한 단순한 설계규칙 네가지만 지키면, 소프트웨어 품질이 크게 높아진다.(중요도 순)
  1. 모든 테스트를 실행한다.
  2. 중복을 없앤드
  3. 프로그래머 의도를 표현한다.
  4. 클래스와 메서드 수를 최소로 줄인다.

## 단순한 설계 규칙1: 모든 테스트를 실행하라
* 테스트 가능한 시스템
  * 테스트를 철저히 거쳐 모든 테스트 케이스를 항상 통과하는 시스템
* 테스트가 가능한 시스템을 만들려고 애쓰면
  * 크기가 작고, 목적 하나만 수행하는 클래스(SRP를 준수)
  * 결합도가 낮은 코드
    * 의존성 주입, 인터페이스, 추상화 등 사용
    * DIP 원칙 준수
  * 낮은 결합도와 높은 응집력

## 단순한 설계 규칙 2-4: 리팩터링
* 테스트를 모두 작성했다면, 코드와 클래스 정리 -> 리팩터링!
* 소프트웨어 설계 품질을 높이는 기법이라면 무엇이든 적용해도 됨

## 중복을 없애라
* 똑같은 코드, 비슷한 코드 역시 중복
  * 비슷한 코드는 더 비슷하게 고쳐주면 리팩토링이 쉬워짐
* TEMPLATE METHOD 패턴
  * 고차원 중복을 제거할 목적으로 자주 사용하는 기법
  * 아래 예제 코드
```java
public class VacationPolicy {
    public void accrueUSDivisionVacation() {
        // 지금까지 근무한 시간을 바탕으로 휴가 일수를 계산
        // 휴가 일수가 미국 최소 법정 일수를 만족하는지 확인
        // 휴가 일수를 급여 대장에 적용
    }

    public void accrueEUDivisionVacation() {
        // 지금까지 근무한 시간을 바탕으로 휴가 일수를 계산
        // 휴가 일수가 유럽연합 최소 법정 일수를 만족하는지 확인
        // 휴가 일수를 급여 대장에 적용
    }
}
```
```java
abstract public class VacationPolicy {
    public void accrueDivisionVacation() {
        calculateBaseVacationHours();
        alterForLegalMinimums();
        applyToPayroll();
    }
    
    private void calculateBaseVacationHours() {}
    abstract protected void alterForLegalMinimums();
    private void applyToPayroll() {}
}

public class USVacationPolicy extends VacationPolicy {
    @Override
    protected void alterForLegalMinimums() {}
}

public class EUVacationPolicy extends VacationPolicy {
    @Override
    protected void alterForLegalMinimums() {}
}
```
## 표현하라
* 남이 이해하기 좋은 코드
  * 버그 줄고 유지보수 비용 준다.
* 남이 이해하기 좋으 코드 짜는 방법
  1. 좋은 이름
  2. 함수와 클래스 크기를 줄인다.
  3. 표준 명칭
  4. 단위 테스트 케이스 꼼꼼히 작성

## 클래스와 메서드 수를 최소로 줄여라
* 무의미하고 독단적인 정책 탓에 클래스 수와 메서드 수가 늘어나는 케이스도 있다.
  * 클래스 마다 무조건 인터페이스를 생성하라고 요구하는 구현 표준
  * 자료 클래스와 동작 클래스는 무조건 분리해야 한다고 주장하는 개발자
* 목표: 함수와 클래스의 크기를 작게 유지하면서, 동시에 시스템 크기도 작게 유지하는 것
  * 하지만 테스트 케이스를 만들고 중복을 제거하고 의도를 표현하는게 우선이다.

