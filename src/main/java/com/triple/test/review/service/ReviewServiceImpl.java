package com.triple.test.review.service;

import com.triple.test.review.dto.PostEventDto;
import com.triple.test.review.dto.ReviewPointDto;
import com.triple.test.review.repository.ReviewPointRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewPointRepositorySupport pointRepositorySupport;
    private final ReviewPointProc pointProc;

    @Override
    public ReviewPointDto reviewPoint(PostEventDto postEventDto) {
        String action = postEventDto.getAction();
        ReviewPointDto reviewPointDto = null;
        boolean alreadyWrite = pointRepositorySupport.aleadyWrite(postEventDto);

        switch (action) {
            case "ADD" -> {
                if (alreadyWrite) break;
                reviewPointDto = pointProc.add(postEventDto);
            }
            case "MOD" -> {
                if (!alreadyWrite) break;
                reviewPointDto = pointProc.mod(postEventDto);
            }
            case "DELETE" -> {
                if (!alreadyWrite) break;
                reviewPointDto = pointProc.delete(postEventDto);
            }
        }

        return Optional.ofNullable(reviewPointDto).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public ReviewPointDto getTotalPoints(String reviewId, String userId) {
        return pointProc.getTotalPoints(reviewId, userId);
    }


}
