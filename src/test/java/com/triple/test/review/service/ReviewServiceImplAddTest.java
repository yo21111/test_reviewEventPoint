package com.triple.test.review.service;

import com.triple.test.review.dto.PostEventDto;
import com.triple.test.review.dto.ReviewPointDto;
import com.triple.test.review.repository.ReviewPointRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("리뷰작성 이벤트 - ADD 요청 시")
class ReviewServiceImplAddTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewPointRepository pointRepository;

    PostEventDto postEventDto;

    @BeforeAll
    void setUp() {
        setupdata();
    }

    @AfterEach
    void afterEach() {
        pointRepository.deleteAll();
        setupdata();
    }

    @Nested
    @DisplayName("리뷰 작성 - 성공")
    class SuccessTest {

        @Test
        @DisplayName("특정 장소에 가장 먼저 리뷰를 남겼을 시")
        void successFirst() {
            ReviewPointDto pointDto = reviewService.reviewPoint(postEventDto);
            log.info("add결과={}", pointDto.toString());
            assertThat(pointDto.getReviewId()).isEqualTo(postEventDto.getReviewId());
            assertThat(pointDto.getUserId()).isEqualTo(postEventDto.getUserId());
            assertThat(pointDto.getTotalPoint()).isEqualTo(3);
        }

        @Test
        @DisplayName("특정 장소에 두번째 이상으로 리뷰를 남긴 경우")
        void successAfter() {
            reviewService.reviewPoint(postEventDto);
            postEventDto.setUserId(UUID.randomUUID().toString());
            ReviewPointDto pointDto = reviewService.reviewPoint(postEventDto);
            log.info("add결과={}", pointDto.toString());
            assertThat(pointDto.getTotalPoint()).isEqualTo(2);
        }

        @Test
        @DisplayName("첫번째 리뷰가 삭제되어 등록한 리뷰가 처음인 경우")
        void fortuneFirst() {
            reviewService.reviewPoint(postEventDto);
            postEventDto.setAction("DELETE");
            reviewService.reviewPoint(postEventDto);

            postEventDto.setAction("ADD");
            postEventDto.setUserId(UUID.randomUUID().toString());
            ReviewPointDto pointDto = reviewService.reviewPoint(postEventDto);
            log.info("add결과={}", pointDto.toString());
            assertThat(pointDto.getTotalPoint()).isEqualTo(3);
        }

        @Test
        @DisplayName("특정 장소 처음 리뷰 작성을 2번 연속")
        void fortuneFirst2() {
            reviewService.reviewPoint(postEventDto);
            postEventDto.setPlaceId(UUID.randomUUID().toString());
            ReviewPointDto reviewPointDto = reviewService.reviewPoint(postEventDto);

            assertThat(reviewPointDto.getTotalPoint()).isEqualTo(3 + 3);
        }

        @Test
        @DisplayName("내용을 작성 안한 경우")
        void noContent() {
            postEventDto.setContent(null);
            ReviewPointDto pointDto = reviewService.reviewPoint(postEventDto);
            log.info("add결과={}", pointDto.toString());
            assertThat(pointDto.getTotalPoint()).isEqualTo(2);
        }

        @Test
        @DisplayName("사진을 첨부 안한 경우")
        void noPicture() {
            postEventDto.setAttachedPhotoIds(new ArrayList<>());
            ReviewPointDto pointDto = reviewService.reviewPoint(postEventDto);
            log.info("add결과={}", pointDto.toString());
            assertThat(pointDto.getTotalPoint()).isEqualTo(2);
        }

        @Test
        @DisplayName("사진을 첨부 및 내용을 작성 안한 경우")
        void noPictureAndContent() {
            postEventDto.setContent(null);
            postEventDto.setAttachedPhotoIds(new ArrayList<>());
            ReviewPointDto pointDto = reviewService.reviewPoint(postEventDto);
            log.info("add결과={}", pointDto.toString());
            assertThat(pointDto.getTotalPoint()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("리뷰 작성 - 실패")
    class FailTest {

        @Test
        @DisplayName("reviewId 미입력")
        void invalidDataAccessApiUsageException1() {
            postEventDto.setReviewId(null);
            Assertions.assertThrows(InvalidDataAccessApiUsageException.class,
                    () -> reviewService.reviewPoint(postEventDto));
        }

        @Test
        @DisplayName("userId 미입력")
        void invalidDataAccessApiUsageException2() {
            postEventDto.setUserId(null);
            Assertions.assertThrows(InvalidDataAccessApiUsageException.class,
                    () -> reviewService.reviewPoint(postEventDto));
        }

        @Test
        @DisplayName("placeId 미입력")
        void invalidDataAccessApiUsageException3() {
            postEventDto.setPlaceId(null);
            Assertions.assertThrows(InvalidDataAccessApiUsageException.class,
                    () -> reviewService.reviewPoint(postEventDto));
        }

        @Test
        @DisplayName("action 오입력")
        void illegalArgument() {
            postEventDto.setAction("add");
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> reviewService.reviewPoint(postEventDto));
        }

        @Test
        @DisplayName("ADD 2번 연속 실행")
        void addTwice() {
            reviewService.reviewPoint(postEventDto);
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> reviewService.reviewPoint(postEventDto));
        }
    }


    public void setupdata() {
        this.postEventDto = new PostEventDto();
        postEventDto.setType("REVIEW");
        postEventDto.setAction("ADD");
        postEventDto.setReviewId(UUID.randomUUID().toString());
        postEventDto.setContent("좋아요!!");

        List<String> list = new ArrayList<>();
        list.add(UUID.randomUUID().toString());
        list.add(UUID.randomUUID().toString());
        postEventDto.setAttachedPhotoIds(list);

        postEventDto.setUserId(UUID.randomUUID().toString());
        postEventDto.setPlaceId(UUID.randomUUID().toString());
    }
}