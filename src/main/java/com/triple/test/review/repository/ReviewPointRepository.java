package com.triple.test.review.repository;

import com.triple.test.review.domain.ReviewPoint;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewPointRepository extends JpaRepository<ReviewPoint, Long> {
    // 리뷰Id, 장소Id, 유저Id를 바탕으로 현재 포인트 찾기
    ReviewPoint findByReviewIdAndPlaceIdAndUserId(String reviewId, String placeId, String userId);

    // 해당 장소, 리뷰 아이디 바탕으로 갯수 찾기
    int countByReviewIdAndPlaceId(String reviewId, String placeId);
}
