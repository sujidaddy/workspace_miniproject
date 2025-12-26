## 실시간 바다낚시 대시보드 REST API 명세

요구사항과 명시된 기술 스택(Spring Boot, Next.js, MySQL)을 기반으로 기능별 REST API 명세를 정리했습니다.

---

### 1. 기본 사항

*   **Base URL**: `/api/v1`
*   **인증**: 현재 명세에서는 별도 인증을 가정하지 않으나, 향후 사용자 기능 추가 시 JWT 등을 통한 인증이 필요할 수 있습니다.
*   **공통 응답 포맷**: 모든 API 응답은 아래와 같은 구조를 따릅니다. Next.js의 SWR이나 React-Query 같은 데이터 페칭 라이브러리에서 일관되게 처리하기 용이합니다.

    **성공 시 (Success)**
    ```json
    {
      "success": true,
      "data": {
        // 실제 데이터 객체
      },
      "error": null
    }
    ```

    **실패 시 (Error)**
    ```json
    {
      "success": false,
      "data": null,
      "error": {
        "code": "ERROR_CODE",
        "message": "에러 메시지"
      }
    }
    ```

---

### 2. API 명세 (기능별)

#### 2.1. 지도 및 포인트 정보 API

**1. 전체 낚시 포인트 요약 정보 조회 (지도 표시용)**

*   **설명**: 지도에 표시할 모든 낚시 포인트의 핵심 요약 정보를 조회합니다. 클라이언트(Next.js)는 이 데이터를 받아 지도 위에 마커를 렌더링합니다.
*   **Endpoint**: `GET /api/v1/fishing-points/summary`
*   **Query Parameters**:
    *   `date` (String, Optional): 조회할 날짜 (형식: `YYYYMMDD`). 지정하지 않으면 현재 날짜를 기본값으로 사용합니다.
    *   `timeCode` (Integer, Optional): 오전(1)/오후(2) 구분 코드. 지정하지 않으면 현재 시간에 맞춰 서버에서 자동 설정합니다.
    *   `fishName` (String, Optional): 특정 어종의 이름. 지정하면 해당 어종에 대한 지수(`totalIndex`, `lastScr`)를 반환합니다. 지정하지 않으면 종합 지수를 반환합니다.
*   **Successful Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        {
          "pointId": "NIFS_2024_001",
          "pointName": "가거도",
          "lat": 34.07,
          "lon": 125.12,
          "totalIndex": "좋음",
          "lastScr": 75
        },
        {
          "pointId": "NIFS_2024_002",
          "pointName": "거문도",
          "lat": 34.03,
          "lon": 127.31,
          "totalIndex": "매우나쁨",
          "lastScr": 15
        }
        // ... more points
      ],
      "error": null
    }
    ```

**2. 특정 낚시 포인트 상세 정보 조회**

*   **설명**: 사용자가 지도에서 특정 포인트를 클릭했을 때, 해당 포인트의 모든 상세 정보를 조회합니다.
*   **Endpoint**: `GET /api/v1/fishing-points/{pointId}`
*   **Path Variable**:
    *   `pointId` (String, Required): 조회할 낚시 포인트의 고유 ID.
*   **Query Parameters**:
    *   `date` (String, Optional): 조회할 날짜 (형식: `YYYYMMDD`). 지정하지 않으면 현재 날짜를 기본값으로 사용합니다.
    *   `timeCode` (Integer, Optional): 오전(1)/오후(2) 구분 코드. 지정하지 않으면 현재 시간에 맞춰 서버에서 자동 설정합니다.
*   **Successful Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "pointId": "NIFS_2024_001",
        "pointName": "가거도",
        "predcYmd": "20251226",
        "predcNoonSeCd": 1, // 1: 오전
        "totalIndex": "좋음",
        "lastScr": 75,
        "weatherMetrics": {
          "minWvhgt": 0.5, "maxWvhgt": 1.0,
          "minWspd": 3.0, "maxWspd": 5.5,
          "minWtem": 15.5, "maxWtem": 16.0,
          "minArtmp": 12.0, "maxArtmp": 14.5,
          "minCrsp": 50.0, "maxCrsp": 80.0
        },
        "fishScores": [
          {
            "seafsTgfshNm": "감성돔",
            "tdlvHrScr": 80
          },
          {
            "seafsTgfshNm": "돌돔",
            "tdlvHrScr": 65
          },
          {
            "seafsTgfshNm": "참돔",
            "tdlvHrScr": 70
          }
        ]
      },
      "error": null
    }
    ```

#### 2.2. 필터링을 위한 데이터 API

**1. 필터링 가능한 어종 목록 조회**

*   **설명**: 클라이언트의 '대상 어종 필터' 드롭다운을 채우기 위한 전체 어종 목록을 제공합니다.
*   **Endpoint**: `GET /api/v1/fishes`
*   **Query Parameters**: 없음
*   **Successful Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        "감성돔",
        "돌돔",
        "참돔",
        "우럭",
        "농어",
        "벵에돔"
        // ... more fish names
      ],
      "error": null
    }
    ```

---

### 3. 데이터 모델 (Spring Boot DTO 예시)

백엔드(Spring Boot)에서는 아래와 같은 DTO(Data Transfer Object)를 사용하여 클라이언트와 데이터를 주고받을 수 있습니다.

**FishingPointSummaryDto.java**
```java
public class FishingPointSummaryDto {
    private String pointId;
    private String pointName;
    private double lat;
    private double lon;
    private String totalIndex;
    private int lastScr;
    // Getters and Setters
}
```

**FishingPointDetailDto.java**
```java
public class FishingPointDetailDto {
    private String pointId;
    private String pointName;
    private String predcYmd;
    private int predcNoonSeCd;
    private String totalIndex;
    private int lastScr;
    private WeatherMetricsDto weatherMetrics;
    private List<FishScoreDto> fishScores;
    // Getters and Setters
}
```

**WeatherMetricsDto.java**
```java
public class WeatherMetricsDto {
    private double minWvhgt;
    private double maxWvhgt;
    private double minWspd;
    private double maxWspd;
    // ... 등 모든 상세 지표
    // Getters and Setters
}
```

**FishScoreDto.java**
```java
public class FishScoreDto {
    private String seafsTgfshNm;
    private int tdlvHrScr;
    // Getters and Setters
}
```
