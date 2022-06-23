package com.triple.test.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewPointDto {
    private String type;
    private String reviewId;
    private String userId;
    private Integer totalPoint;

    public ReviewPointDto (String reviewId, String userId, Integer totalPoint) {
        this.type = "REVIEW";
        this.reviewId = reviewId;
        this.userId = userId;
        this.totalPoint = totalPoint;
    }
}
