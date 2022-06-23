package com.triple.test.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.test.review.domain.QReviewPoint;
import com.triple.test.review.domain.ReviewPoint;
import com.triple.test.review.dto.PostEventDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewPointRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public ReviewPointRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(ReviewPoint.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Integer findPointsByIds(String reviewId, String userId) {
        QReviewPoint qReviewPoint = new QReviewPoint("reviewPoint");
        Integer points = jpaQueryFactory
                .select(qReviewPoint.point.sum())
                .from(qReviewPoint)
                .where(qReviewPoint.reviewId.eq(reviewId)
                        .and(qReviewPoint.userId.eq(userId)))
                .fetchFirst();
        return points != null ? points : 0;
    }

    public boolean aleadyWrite(PostEventDto postEventDto) {
        QReviewPoint qReviewPoint = new QReviewPoint("reviewPoint");
        ReviewPoint reviewPoint = jpaQueryFactory
                .select(qReviewPoint)
                .from(qReviewPoint)
                .where(qReviewPoint.reviewId.eq(postEventDto.getReviewId())
                        .and(qReviewPoint.userId.eq(postEventDto.getUserId()))
                        .and(qReviewPoint.placeId.eq(postEventDto.getPlaceId())))
                .fetchFirst();
        return reviewPoint != null ? true : false;
    }
}
