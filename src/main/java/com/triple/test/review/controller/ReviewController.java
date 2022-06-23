package com.triple.test.review.controller;

import com.triple.test.review.dto.ErrorDto;
import com.triple.test.review.dto.GetTotalPointDto;
import com.triple.test.review.dto.PostEventDto;
import com.triple.test.review.dto.ReviewPointDto;
import com.triple.test.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDto argumentEx(IllegalArgumentException e) {
        e.printStackTrace();
        return new ErrorDto(e, e.getMessage(), LocalDate.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDto argumentEx(InvalidDataAccessApiUsageException e) {
        e.printStackTrace();
        return new ErrorDto(e, e.getMessage(), LocalDate.now());
    }

    @GetMapping("/events")
    public ReviewPointDto totalReviewPoint(@RequestBody GetTotalPointDto getTotalPointDto) {
        return reviewService.getTotalPoints(getTotalPointDto.getReviewId(), getTotalPointDto.getUserId());
    }

    @PostMapping("/events")
    public ReviewPointDto reviewPoint(@RequestBody PostEventDto postEventDto) {
        Optional<ReviewPointDto> reviewPoint = Optional.of(reviewService.reviewPoint(postEventDto));
        return reviewPoint.orElseThrow(IllegalArgumentException::new);
    }
}
