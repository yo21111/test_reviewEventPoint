package com.triple.test.review.domain;

import com.triple.test.review.dto.PostEventDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewId;

    private String placeId;

    private String userId;

    private Integer point;

    @Builder
    public ReviewPoint(PostEventDto postEventDto, Integer point) {
        this.reviewId = postEventDto.getReviewId();
        this.placeId = postEventDto.getPlaceId();
        this.userId = postEventDto.getUserId();
        this.point = point;
    }

    public void modifyPoint(Integer point) {
        this.point = point;
    }
}
