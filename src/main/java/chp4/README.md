# 4. 주석
* 주석은 없는게 최선. 코드의 표현력을 높여 주석이 필요 없는 코드를 만들어야 함.
## 주석은 나쁜 코드르 보완하지 못한다.
* 이해하기 힘든 코드는 주석을 달게 아니라 다시 짜야 한다.
## 코드로 의도를 표현하라!
* 주석대신 함수로 의도를 표현
```java
void test() {
    // before
    if ((employee.flgs & HOURLY_FLAG) && employee.age > 65) {}
    // after
    if (employee.isEligibleForFullBenefits()) {}
}
```

## 좋은 주석
### 법적인 주석
* 회사가 정립한 구현 표준에 맞춰 법적인 이유로 넣는 주석
### 정보를 제공하는 주석
```java
class Test() {
    // before
    // 테스트 중인 Responder 인스턴스를 반환한다.
    protected abstract Responder responderInstance();
    
    // after
    protected abstract Responder responderBeingTested();
    
    // before
    // kk:mm:ss EEE, MMM dd, yyyy 형식이다.
    Pattern timeMatcher = Pattern.complile(
            "\\d*:\\d*:\\d* \\w*, \\w* \\d, \\d*:"
    );
    
    // after
    // 날짜와 시각을 변환하는 클래스 사용
}
```
### 의도록 명료하게 밝히는 주석
### 결과를 경고하는 주석
### TODO 주석
### 중요성을 강조하는 주석
### 공개 API에서 Javadocs

## 나쁜 주석
### 주절거리는 주석
### 같은 이야기를 중복하는 주석
### 오해할 여지가 있는 주석
### 의무적으로 다는 주석
### 이력을 기록하는 주석
### 있으나 마나 한 주석
### 무서운 잡음
### 함수나 변수로 표현할 수 있다면, 주석을 달리 마라
### 위치를 표시하는 주석
### 닫는 괄호에 다는 주석
### 공로를 돌리거나 저자를 표시하는 주석
### 주석으로 처리한 코드
### HTML 주석
* Javadocs 같은 도구로 주석을 뽑아 웹페이지에 올릴 작적이라면, 주석에 HTML을 삽입해야 하는 책임은 도구가 져야 함
### 전역 정보
### 너무 많은 정보
### 모호한 관계
### 함수 헤더
### 비공개 코드에서 Javadocs
### 예제
* 에라토스테네스의 체 - 코드 참조

