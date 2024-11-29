# 15. JUnit 들여다보기
## JUnit 프레임워크
* 대표 저자 - 켄트 벡, 에릭 감마
* 문자열 비교 오류를 파악할 때 유용한 코드를 사펴 본다.
  * ComparisonCompactor: 두 문자열을 받아 차이를 반환
  * Input: ABCDE, ABXDE
  * Output: <...B[X]D...>
* before
```java
public class ComparisonCompactor {
    private static final String ELLIPSIS = "...";
    private static final String DELTA_END = "]";
    private static final String DELTA_START = "[";

    private int fContextLength;
    private String fExpected;
    private String fActual;
    private int fPrefix;
    private int fSuffix;

    public ComparisonCompactor(
            int contextLength,
            String expected,
            String actual
    ) {
        fContextLength = contextLength;
        fExpected = expected;
        fActual = actual;
    }

    public String compact(String message) {
        if (fExpected == null || fActual == null || areStringEqual()) {
            return Assert.format(message, fExpected, fActual);
        }

        findCommonPrefix();
        findCommonSuffix();
        String expected = compactString(fExpected);
        String actual = compactString(fActual);
        return Assert.format(message, expected, actual);
    }

    private String compactString(String source) {
        String result = DELTA_START + source.substring(fPrefix, source.length() - fSuffix + 1) + DELTA_END;

        if (fPrefix > 0) {
            result = computeCommonPrefix() + result;
        }

        if (fSuffix > 0) {
            result = computeCommonSuffix() + result;
        }
    }

    private void findCommonSuffix() {
        int expectedSuffix = fExpected.length() - 1;
        int actualSuffix = fActual.length() - 1;

        for (; actualSuffix >= fPrefix && expectedSuffix >= fPrefix; actualSuffix--, expectedSuffix--) {
            if (fExpected.charAt(expectedSuffix) != fActual.charAt(actualSuffix)) {
                break;
            }
        }

        fSuffix = fExpected.length() - expectedSuffix;
    }

    private void findCommandPrefix() {
        fPrefix = 0;
        int end = Math.min(fExpected.length, fActual.length);
        for (; fPrefix < end; fPrefix++) {
            if (fExpected.charAt(fPrefix) != fActual.charAt(fPrefix)) {
                break;
            }
        }
    }

    private boolean areStringEqual() {
        return fExpected.equals(fActual);
    }

    public String computeCommonPrefix() {
        return (fPrifix > fContextLength ? ELLIPSIS : "") + fExpected.substring(Math.max(0, fPrefix - fContextLength), fPrefix);
    }

    public String computeCommonSuffix() {
        int min = Math.min(fExpected.length() - fSuffix + 1 + fContextLength, fExpected.length());

        return fExpected.substring(fExpected.length() - fSuffix + 1, end) + (fExpected.length() - fSuffix + 1 < fExpected.length() - fContextLength ? ELLIPSIS : "");
    }
}
```

* [N6] 인코딩을 피하라
  * 이름에 유형 정보나 범위 정보를 넣어서는 안된다.
  * 멤버변수 앞에 f 제거
```java
private int contextLength;
private String expected;
private String actual;
private int prefix;
private int suffix;
```

* [G28] 조건을 캡슐화 하라
  * compact 함수 시작부에 캡슐화 되지 않은 조건문
* [N4] 이름은 명확하게 붙인다.
```java
public String compact(String message) {
  if (shouldNotCompact()) {
    return Assert.format(message, expected, actual);
  }

  findCommonPrefix();
  findCommonSuffix();
  String compactExpected = compactString(compactExpected);
  String compactActual = compactString(compactActual);
  return Assert.format(message, compactExpected, compactActual);
}

private boolean shouldNotCompact() {
  return fExpected == null || fActual == null || areStringEqual();
}
```

* [G29] 부정분은 긍정문 보다 이해하기 어렵다.
```java
public String compact(String message) {
  if (canBeCompacted()) {
    findCommonPrefix();
    findCommonSuffix();
    String compactExpected = compactString(compactExpected);
    String compactActual = compactString(compactActual);
    return Assert.format(message, compactExpected, compactActual);
  } else {
    return Assert.format(message, expected, actual);
  }
}

private boolean canBeCompacted() {
  return fExpected != null && fActual != null && !areStringEqual();
}
```

* [N7] 이름으로 부수 효과를 설명하라
  * compact 라는 함수 이름에는 
    * 오류점검이라는 부가단계가 숨겨진다.
    * 단순히 압축 된 문자열이 아니라, 형식이 갖춰진 문자열을 반환한다.
  * formatCompactedComparison 이름이 적합
```java
public String formatCompactedComparison(String message) {
  if (canBeCompacted()) {
    compactExpectedAndActual();
    return Assert.format(message, compactExpected, compactActual);
  } else {
    return Assert.format(message, expected, actual);
  }
}

private void compactExpectedAndActual() {
  findCommonPrefix();
  findCommonSuffix();
  compactExpected = compactString(compactExpected);
  compactActual = compactString(compactActual);
}
```

* [G11] 일관성 부족
  * compactExpectedAndActual 에서 1, 2 행은 반환값이 없고, 3, 4 행은 반환 값이 있다.
* [N1] 서술적인 이름을 사용하라
  * prefix, suffix -> prefixIndex, suffixIndex
```java
private void compactExpectedAndActual() {
  prefixIndex = findCommonPrefix();
  sufixIndex = findCommonSuffix();
  compactExpected = compactString(compactExpected);
  compactActual = compactString(compactActual);
}
```

* [G31] 숨겨진 시간적 결합
  * findCommonSuffix 는 prefixIndex 가 계산되었다는 가정에 의존
```java
private int findCommonSuffix(int prefixIndex) {
    int expectedSuffix = expected.length() - 1;
    int actualSuffix = actual.length() - 1;

    for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; actualSuffix--, expectedSuffix--) {
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
        break;
      }
    }

    return expected.length() - expectedSuffix;
  }
```

* [G32] 일관성을 유지하라
  * findCommonPrefix, findCommonSuffix의 형태가 달라짐
  * prefixIndex 가 필요한 이유가 분명히 드러나지 않음
  * findCommonPrefixAndSuffix() 로 변경
```java
private int findCommonPrefixAndSuffix() {
  findCommonPrefix();
  
  int expectedSuffix = expected.length() - 1;
  int actualSuffix = actual.length() - 1;

  for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; actualSuffix--, expectedSuffix--) {
    if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
      break;
    }
  }

  suffixIndex = expected.length() - expectedSuffix;
}
```

* findCommonPrefixAndSuffix 함수 정리
```java
private void findCommonPrefixAndSuffix() {
    findCommandPrefix();

    int suffixLength = 1;
    int expectedSuffix = expected.length() - 1;
    int actualSuffix = actual.length() - 1;

    for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) {
        if (charFromEnd(expected, suffixLength) != charFromEnd(actual, suffixLength)) {
            break;
        }
    }

    suffixIndex = suffixLength;
}

private char charFromEnd(String s, int i) {
    return s.charAt(i);
}

private boolean suffixOverlapsPrefix(int suffixLength) {
    return actual.length() - suffixLength < prefixIndex || expected.length() - suffixLength < prefixIndex;
}

```

* [G33] 경계조건을 캡슐화 하라.
  * suffixIndex: 접미사의 길이, 즉 suffixLength
```java
private int suffixLength;

private void findCommonPrefixAndSuffix() {
  findCommandPrefix();

  suffixLength = 1;
  int expectedSuffix = expected.length() - 1;
  int actualSuffix = actual.length() - 1;

  for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) {
    if (charFromEnd(expected, suffixLength) != charFromEnd(actual, suffixLength)) {
      break;
    }
  }
}

private char charFromEnd(String s, int i) {
  return s.charAt(s.length() - i - 1);
}

private boolean suffixOverlapsPrefix(int suffixLength) {
  return actual.length() - suffixLength <= prefixIndex || expected.length() - suffixLength <= prefixIndex;
}

private String compactString(String source) {
  String result = DELTA_START + source.substring(prefixIndex, source.length() - suffixIndex) + DELTA_END;

  if (prefixIndex > 0) {
    result = computeCommonPrefix() + result;
  }

  if (suffixIndex > 0) {
    result = computeCommonSuffix() + result;
  }
}

public String computeCommonSuffix() {
  int end = Math.min(expected.length() - suffixIndex + contextLength, expected.length());

  return expected.substring(expected.length() - suffixIndex, end) + (expected.length() - suffixIndex < expected.length() - contextLength ? ELLIPSIS : "");
}
```

* compareString 의 if 문 삭제
```java
private String compactString(String source) {
  return computeCommonPrefix() 
          + DELTA_START 
          + source.substring(prefixIndex, source.length() - suffixIndex) 
          + DELTA_END
          + computeCommonSuffix();
}
```

* 함수정렬은 위상적으로
  * private 함수는 각 함수가 사용된 직후에 정의되며 분석함수 면저, 조합 함수 다음
## 결론

```java
public class ComparisonCompactor {
  private static final String ELLIPSIS = "...";
  private static final String DELTA_END = "]";
  private static final String DELTA_START = "[";

  private int contextLength;
  private String expected;
  private String actual;
  private int prefixIndex;
  private int suffixLength;
  private String compactExpected;
  private String compactActual;

  public ComparisonCompactor(
          int contextLength,
          String expected,
          String actual
  ) {
    this.contextLength = contextLength;
    this.expected = expected;
    this.actual = actual;
  }

  public String formatCompactedComparison(String message) {
    if (canBeCompacted()) {
      findCommonPrefixAndSuffix();
      compactExpected = compact(compactExpected);
      compactActual = compact(compactActual);
      return Assert.format(message, compactExpected, compactActual);
    } else {
      return Assert.format(message, expected, actual);
    }
  }

  private boolean canBeCompacted() {
    return expected != null && actual != null && !expected.equals(actual);
  }

  private void findCommonPrefixAndSuffix() {
    findCommandPrefix();

    suffixLength = 1;
    int expectedSuffix = expected.length() - 1;
    int actualSuffix = actual.length() - 1;

    for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) {
      if (charFromEnd(expected, suffixLength) != charFromEnd(actual, suffixLength)) {
        break;
      }
    }
  }

  private char charFromEnd(String s, int i) {
    return s.charAt(s.length() - i - 1);
  }

  private boolean suffixOverlapsPrefix(int suffixLength) {
    return actual.length() - suffixLength <= prefixIndex || expected.length() - suffixLength <= prefixIndex;
  }
  
  private void findCommandPrefix() {
    int prefixIndex = 0;
    int end = Math.min(expected.length(), actual.length());
    for (; prefixIndex < end; prefixIndex++) {
      if (expected.charAt(prefixIndex) != actual.charAt(prefixIndex)) {
        break;
      }
    }
  }
  
  private String compact(String source) {
    return new StringBuilder()
            .append(startEllipsis())
            .append(startContext())
            .append(DELTA_START)
            .append(delta())
            .append(DELTA_END)
            .append(endingContext())
            .append(endingEllipsis())
            .toString();
  }

  private String startEllipsis() {
      return prefixIndex > contextLength ? ELLIPSIS : "";
  }
  
  private String startContext() {
      int contextStart = Math.max(0, prefixIndex - contextLength);
      int contextEnd = prefixIndex;
      return expected.substring(contextStart, contextEnd);
  }
  
  private String delta(String s) {
      int deltaStart = prefixIndex;
      int deltaEnd = s.length() - suffixIndex;
     return source.substring(deltaStart, deltaEnd); 
  }
  
  private String endingContext() {
      int contextStart = expected.length() - suffixIndex;
      int contextEnd = Math.min(contextStart + contextLength, expected.length());
      return expected.substring(expected.length() - suffixIndex, contextEnd);
  }
  
  private String endingEllipsis() {
      return suffixLength < contextLength ? ELLIPSIS : "";
  }
}
```
* 개선이 불필요한 모듈은 없다.