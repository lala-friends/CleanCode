# 6. 객체와 자료 구조
* 변수를 비공개(private)으로 정의하는 이유
  * 남들이 변수에 의존하지 않게 만들고 싶어서
## 자료 추상화
```java
public class Point {
    public double x;
    public double y;
}
```
* 구현을 외부로 노출
```java
public interface Point {
    double getX();
    double getY();
    void setCartesian(double x, double y);
    double getR();
    double getTheta();
    void setPolar(double r, double theta);
}
```
* 직교좌표계를 사용하는지 극좌표계를 사용하는지 외부에서는 모름
```java
public interface Vehicle {
    double getFuelTankCapacityInGallons();
    double getGallonsOfGasoline();
}
```
* 직접적으로 들어나지는 않았지만, 함수를 통해 구현을 외부로 노출하고 있음
```java
public interface Vehicle {
    double getPercentFuelRemaining();
}
```
* 추상적인 정보를 제공함으로써, 직접적으로 구현이 드러나지 않음

* 개발자는 객체가 포함하는 자료를 표현할 가장 좋은 방법을 고민해야 한다.
## 자료/객체 비대칭
* 객체와 자료구조의 차이
  * 객체: 추상화 뒤로 자료를 숨긴 채 자료를 다루는 함수만 공개
  * 자료를 그대로 공개하며, 별다른 함수는 제공하지 않음
* 절차지향 코드 
  * 장점: 기존 자료 구조를 변경하지 않으면서, 새 함수를 추가하기 쉽다.
  * 단점: 기존 함수를 변경하지 않으면서, 새 클래스를 추가하기 어렵다.
* 객체지향 코드
  * 장점: 기존 함수를 변경하지 않으면서, 새 클래스를 추가하기 쉽다.
  * 단점: 기존 자료 구조를 변경하지 않으면서, 새 함수를 추가하기 어렵다.

## 디미터의 법칙
* 디미터의 법칙
  * 자신이 조작하는 객체의 속사정을 몰라야 한다.
  * 클래스 C의 메서드 f는 다음과 같은 객체의 메서드만 호출해야 한다.
    * 클래스 C
    * f가 생성한 객체
    * f 인수로 넘어온 객체
    * C 인스턴수 변수에 저장된 객체
### 기차 충돌
* bad
```java
final String outputDir = ctxtx.getOptions().getScratchDir().getAbsolutePath();
```
* good
  * 아래 ctxt, Options, ScratchDir이 객체이면 내부구조를 숨겨야 하므로 디미터 법칙 위반
  * 하지만 자료 구조라면 당연히 내부 구조를 노출하므로 디미터 법칙이 적용되지 않는다.
```java
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir();
final String outputDir = scratchDir.getAbsolutePath();
```
### 잡종 구조
* 객체와 자료구조의 단점만 있음
### 구조체 감추기
* bad
```java
// ctxt에 공개해야 하는 메서드가 너무 만ㅇㅎ아 짐
ctxt.getAbsolutePathOfScratchDirectoryOption();
ctx.getScratchDirectoryOption().getAbsolutePath();
```
* good
    * 위와 같은 코드가 발생한 이유는 임시 파일을 생성하기 위해서 임
    * ctxt 에 임시 파일을 생성하는 메서드 생성
```java
BufferedOutputStream bos = ctxt.createScratchFileStream(classFileName);
```
## 자료 전달 객체
* 자료 구조체 = 자료 전달 객(Data Transfer Object, DTO)
* bean 구조
  * 비곡갱 변수를 조회/설정 함수로 조작
  * 사이비 캡슐화 - 캡슐화 처럼 보이지만 외부에서 객체의 내부를 들여다 볼 수 있는 구조
### 활성 레코드
* DTO의 특수한 형태
  * 공개변수가 있거나, 비공개 변수에 조회/설정함수가 있는 자료 구조지만
  * 대개 save, find 같은 함수도 제공
* 데이터베이스 테이블이나 다른 소스에서 자료를 직접 변환한 결과
* 문제상황: 활성 레코드에 비즈니스 규칙 메서드를 추가해 객체로 취급하는 것
  * 해결책: 활설레코드는 자료 구조, 객체는 따로 생성
## 결론
* 객체
  * 동작을 공개하고 자료를 숨긴다.
  * 기존 동작을 변경하지 않으면서, 새 객체 타입을 추가하기 쉬움
  * 기존 객체에 새 동작을 추가하기는 어려움
* 자료구조
  * 별다른 동작 없이 자료를 노출
  * 새 동작을 추가하기는 쉬움
  * 기존 함수에 새 자료 구조를 추가하기는 어려움
* 시스템을 구현할 때, 새로운 자료 타입으르 추가하는 유연성이 필요한 경우
  * 객체가 적합
* 새로운 동작을 추가하는 유연성이 필요한 경우
  * 절자치장 코드가 더 적합