package com.triple.test.review.dto;

import lombok.Data;

import java.util.List;

/**
 * 리뷰작성 이벤트로 전달되는 json 데이터
 */
@Data
public class PostEventDto {
    private String type;
    private String action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;
}
