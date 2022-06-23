# Review Point API


## 사용한 기술
  - Java 15
  - Spring boot 2.7.0
  - queryDsl 5.0.0
  - MySQL 8.0.27 (Schema : review_event)


## 1. 포인트 적립 API
  - ### 요청 URL
    **POST** : http://localhost:9090/events

  - ### 요청
  ```json
  {
     "type":"REVIEW",
     "action":"ADD",
     "reviewId":"240a0658-dc5f-4878-9381-ebb7b2667772",
     "content":"좋아요!",
     "attachedPhotoIds":[
        "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8",
        "afb0cef2-851d-4a50-bb07-9cc15cbdc332"
     ],
     "userId":"3ede0ef2-92b7-4817-a5f3-0c575361f745",
     "placeId":"2e4baf1c-5acb-4efb-a1af-eddada31b00f"
  }
  ```
  |Name|Type|Required|Description|
  |------|---|---|-------|
  |type|String|필수 0|"REVIEW"|
  |action|String|필수 0|"ADD" / "MOD" / "DELETE" 중 1개 입력|
  |reviewID|String|필수 0|리뷰 이벤트 id|
  |content|String|필수 X|리뷰 내용|
  |attachedPhotoIds|List<String>|필수 X|첨부된 이미지 id|
  |userId|String|필수 0|작성자 id|
  |placeId|String|필수 0|장소 id|

  - ### 응답
  ```json
  {
      "type": "REVIEW",
      "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
      "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f233",
      "totalPoint": 2
  }
  ```
  |Name|Type|Description|
  |------|---|---|
  |type|String|"REVIEW"|
  |reviewID|String|리뷰 이벤트 id|
  |userId|String|작성자 id|
  |totalPoint|String|해당 리뷰 이벤트에 대한 작성자의 누적 적립 포인트|


## 2. 포인트 조회 API
  - ### 요청 URL
    **GET** : http://localhost:9090/events
  - ### 요청
  ```json
  {
     "reviewId":"240a0658-dc5f-4878-9381-ebb7b2667772",
     "userId":"3ede0ef2-92b7-4817-a5f3-0c575361f745"
  }
  ```
  |Name|Type|Required|Description|
  |------|---|---|-------|
  |reviewID|String|필수 0|리뷰 이벤트 id|
  |userId|String|필수 0|작성자 id|

  - ### 응답
  ```json
  {
      "type": "REVIEW",
      "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
      "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f233",
      "totalPoint": 2
  }
  ```
  |Name|Type|Description|
  |------|---|---|
  |type|String|"REVIEW"|
  |reviewID|String|리뷰 이벤트 id|
  |userId|String|작성자 id|
  |totalPoint|String|해당 리뷰 이벤트에 대한 작성자의 누적 적립 포인트|


## 3. 테스트 코드
  * 위치 : test.java.com.triple.test.review.service
  * 테스트 코드 분류
      1. **ReviewServiceImplAddTest** ("ADD" 요청과 관련된 성공 / 실패 테스트)
          * 성공 상황
             1) 특정 장소에 가장 먼저 리뷰를 남겼을 경우
             2) 특정 장소에 두번째 이상으로 리뷰를 남긴 경우
             3) 첫번째 리뷰가 삭제되어 등록한 리뷰가 처음인 경우
             4) 특정 장소 처음 리뷰 작성을 2번 연속
             5) 내용을 작성 안한 경우
             6) 사진을 첨부 안한 경우
             7) 사진을 첨부 및 내용을 작성 안한 경우
          * 실패 상황
             1) reviewId 미입력
             2) userId 미입력
             3) placeId 미입력
             4) action 오입력
             5) ADD 2번 연속 실행
      2. **ReviewServiceImplDeleteTest** ("DELETE" 요청과 관련된 성공 / 실패 테스트)
          - 성공 상황
             1) 작성한 리뷰 삭제
          - 실패 상황
             1) 삭제하려는 것이 이미 삭제되었을 때
      3. **ReviewServiceImplModTest** ("MOD" 요청과 관련된 성공 / 실패 테스트)
          - 성공 상황
             1) 작성한 리뷰 수정 = content 삭제
             2) 작성한 리뷰 수정 = 첨부 사진 삭제
          - 실패 상황
             1) 수정하려는 것이 이미 삭제되었을 때
      
