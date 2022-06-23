package com.triple.test.review.service;

import com.triple.test.review.dto.ReviewPointDto;
import com.triple.test.review.dto.PostEventDto;

public interface ReviewService {
    ReviewPointDto reviewPoint(PostEventDto postEventDto);
    ReviewPointDto getTotalPoints(String reviewId, String userId);
}
