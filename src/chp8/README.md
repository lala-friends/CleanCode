# 8. 경계
* 외부코드를 깔끔하게 처리하는 기법
  * 오픈 소스
  * 다른 팀이 제공하는 컴포넌트
## 외부 코드 사용하기
* java.util.Map: 다양한 인터페이스로 수많은 기능 제공하지만 위험도 큼
  * clear 메서드가 공개되어 있어서 누구나 삭제 가능
  * 특정 객체만 저장하기로 했다고 하더라도, 객체 유형을 제한하지 않음

```java
Map sensors = new HashMap();
Sensor s = (Sensor) sensors.get(sensorId);
```
* 매번 타입을 변환하는 것이 깨끗하지 않음
* 의도가 분명하게 드러나지 않음
```java
// 제네릭스 사용
Map<String, Sensor> sensors = new HashMap<Sensor>();
Sensor s = sensors.get(sensorId);
```
* 사용자에게 필요하지 않은 기능까지 제공한다
* Map<String, Sensor> 인스턴스를 파라미터로 넘기면, Map 인터페이스가 변할 경우, 수정할 코드가 많다.

```java
public class Sensors {
    private Map sensors = new HashMap();
    
    public getById(String id) {
        return (Sensor) sensors.get(id);
    }
}
```
* Map 인터페이스가 변하더라도 다른 프로그램에 영향을 미치지 않음
* 프로그램에 필요한 인터페이스만 제공
* Map을 사용할 때는, 클래스 밖으로 넘기지 않음
* Map 인스턴스를 공개 API의 인수로 넘기거나 반환값으로 사용하지 않는다.
## 경계 살피고 익히기
* 외부 코드를 가져다 사용할 때, 학습 테스트를 이용
* 학습 테스트: 프로그램에서 사용하려는 방식대로 외부 API를 호출하여 테스트 작성
## log4j 익히기 - 학습테스트 사용하는 예
## 학습 테스트는 공짜 이상이다
* 새 버전이 나오면, 학습 테스트를 돌려 차이가 있는지 확
## 아직 존재하지 않는 코드를 사용하기
* 아직 미개발 한 코드
  * 자체적으로 인터페이스 정의
  * adapter 패턴을 이용하여 api 사용 캡슐화 
## 깨끗한 경계
* 우수한 소프트웨어 설계
  * 변경하는데 많은 투자와 재작업이 필요하지 않음
* 경계에 위치하는 코드는 깔끔히 분리하고, 기대치를 정의하는 테스트케이스 작성
* 외부 패키지를 호출하는 코드를 가능한 줄여 경계를 관리
  * adapter 패턴 사용