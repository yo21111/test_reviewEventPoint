package com.triple.test.review.service;

import com.triple.test.review.dto.PostEventDto;
import com.triple.test.review.dto.ReviewPointDto;

public interface ReviewPointProc {
    ReviewPointDto add(PostEventDto postEventDto);
    ReviewPointDto mod(PostEventDto postEventDto);
    ReviewPointDto delete(PostEventDto postEventDto);

    ReviewPointDto getTotalPoints(String reviewId, String userId);
}
