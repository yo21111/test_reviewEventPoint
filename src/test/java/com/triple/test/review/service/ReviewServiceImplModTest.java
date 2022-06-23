package com.triple.test.review.service;

import com.triple.test.review.dto.PostEventDto;
import com.triple.test.review.dto.ReviewPointDto;
import com.triple.test.review.repository.ReviewPointRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("리뷰작성 이벤트 - MOD 요청 시")
class ReviewServiceImplModTest {
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
    @DisplayName("리뷰 수정 - 성공")
    class SuccessTest {

        @Test
        @DisplayName("작성한 리뷰 수정 = content 삭제")
        void successMod1() {
            reviewService.reviewPoint(postEventDto);
            postEventDto.setAction("MOD");
            postEventDto.setContent(null);
            ReviewPointDto reviewPointDto = reviewService.reviewPoint(postEventDto);
            assertThat(reviewPointDto.getTotalPoint()).isEqualTo(2);
        }

        @Test
        @DisplayName("작성한 리뷰 수정 = 첨부 사진 삭제")
        void successMod2() {
            reviewService.reviewPoint(postEventDto);
            postEventDto.setAction("MOD");
            postEventDto.setAttachedPhotoIds(new ArrayList<>());
            ReviewPointDto reviewPointDto = reviewService.reviewPoint(postEventDto);
            assertThat(reviewPointDto.getTotalPoint()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("리뷰 수정 - 실패")
    class FailTest {

        @Test
        @DisplayName("수정하려는 것이 이미 삭제되었을 때")
        void modFail() {
            reviewService.reviewPoint(postEventDto);
            postEventDto.setAction("DELETE");
            reviewService.reviewPoint(postEventDto);
            postEventDto.setAction("MOD");
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