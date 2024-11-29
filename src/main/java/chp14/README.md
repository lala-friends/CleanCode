# 14. 점진적인 개선 - 명령행 구문분석기 사례 연구
* Args: 명령형 인수를 분석하는 프로그램
```bash
$ program -l true -p 8080 -d /usr/logs
```
* 의미 
  * -l: boolean 값 (로그 활성화 여부)
  * -p: int 값 (포트 번호)
  * -d: string 값 (디렉토리 경로)
* 파싱 결과
  * -l: true (boolean)
  * -p: 8080 (integer)
  * -d: "/usr/logs" (string)

## Args 구현
* Boolean 인수만 지원

### 어떻게 짰느냐고?
* 지저분한 코드를 짠 뒤에 정리해야 한다.
  * Boolean 인수만 지원
  * String 인수 추가
  * Integer 인수 추가

## Args: 1차 초안
### 그래서 멈췄다
### 점진적으로 개선하다
* 테스트 슈트 작성 후 변경 시작

## String 인수

## 결론
* 나쁜 코드 보다 더 오랫동안 개발 프로젝트에 악영향을 끼치는 요인은 없음
* 코드는 언제나 최대한 깔끔하고 단순하게 정리하자.