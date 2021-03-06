package com.triple.test.review.service;

import com.triple.test.review.domain.ReviewPoint;
import com.triple.test.review.dto.PostEventDto;
import com.triple.test.review.dto.ReviewPointDto;
import com.triple.test.review.repository.ReviewPointRepository;
import com.triple.test.review.repository.ReviewPointRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewPointProcImpl implements ReviewPointProc {
    private final ReviewPointRepository pointRepository;
    private final ReviewPointRepositorySupport pointRepositorySupport;

    @Override
    @Transactional(rollbackFor = IllegalArgumentException.class)
    public ReviewPointDto add(PostEventDto postEventDto) {
        int beforePoint = 0;
        int afterPoint = getBonusPoint(postEventDto);

        pointRepository.save(ReviewPoint.builder().postEventDto(postEventDto).point(afterPoint).build());
        return getReviewPointDto(postEventDto, beforePoint, afterPoint);
    }

    @Override
    @Transactional(rollbackFor = IllegalArgumentException.class)
    public ReviewPointDto mod(PostEventDto postEventDto) {
        int beforePoint = getBeforePoint(postEventDto);
        int afterPoint = getBonusPoint(postEventDto);

        ReviewPoint beforeEntity = getBeforeEntity(postEventDto);
        beforeEntity.modifyPoint(afterPoint);

        pointRepository.save(beforeEntity);
        return getReviewPointDto(postEventDto, beforePoint, afterPoint);
    }

    @Override
    @Transactional
    public ReviewPointDto delete(PostEventDto postEventDto) {
        int beforePoint = getBeforePoint(postEventDto);
        int afterPoint = 0;

        ReviewPoint beforeEntity = getBeforeEntity(postEventDto);

        pointRepository.delete(beforeEntity);
        return getReviewPointDto(postEventDto, beforePoint, afterPoint);
    }

    @Override
    public ReviewPointDto getTotalPoints(String reviewId, String userId) {
        Integer points = pointRepositorySupport.findPointsByIds(reviewId, userId);
        return new ReviewPointDto(reviewId, userId, points);
    }

    /**
     * @param postEventDto ????????? ?????? ?????? ???????????? Json ?????????
     * @param beforePoint  ?????? ????????? ?????? ??????(0 ~ 3???)
     * @param afterPoint   ?????? ????????? ?????? ??????(0 ~ 3???)
     * @return ReviewPointDto ????????? ?????? ????????? ?????? ?????? Dto
     */
    private ReviewPointDto getReviewPointDto(PostEventDto postEventDto, int beforePoint, int afterPoint) {
        ReviewPointDto reviewPointDto = getTotalPoints(postEventDto.getReviewId(), postEventDto.getUserId());
        log.info("reviewId={}, userId={}, placeId={}, beforePoint={}, afterPoint={}, totalPoint={}", postEventDto.getReviewId(), postEventDto.getUserId(), postEventDto.getPlaceId(), beforePoint, afterPoint, getTotalPoints(postEventDto.getReviewId(), postEventDto.getUserId()).getTotalPoint());
        return reviewPointDto;
    }

    /**
     * @param postEventDto ?????? ?????? ????????? json ?????????
     * @return ?????? ?????? ?????? ??????
     */
    private int getBonusPoint(PostEventDto postEventDto) throws IllegalArgumentException {
        int result = 0;

        // ???????????? : 1??? ?????? ????????? ??????
        if (StringUtils.hasText(postEventDto.getContent())) {
            result += 1;
        }

        // ???????????? : 1??? ?????? ?????? ??????
        if (postEventDto.getAttachedPhotoIds().size() > 0) {
            result += 1;
        }

        // ??????????????? : ?????? ?????? ??? ?????? ??????
        int reviewCount = pointRepository.countByReviewIdAndPlaceId(
                postEventDto.getReviewId(),
                postEventDto.getPlaceId());
        if (reviewCount == 0 || (reviewCount == 1 && pointRepositorySupport.aleadyWrite(postEventDto))) {
            result += 1;
        }

        return result;
    }

    /**
     *
     * @param postEventDto ?????? ?????? ???????????? ???????????? Json ?????????
     * @return ??????, ????????? ????????? ?????? Entity
     * @throws IllegalArgumentException
     */
    private ReviewPoint getBeforeEntity(PostEventDto postEventDto) throws IllegalArgumentException {
        return pointRepository.findByReviewIdAndPlaceIdAndUserId(
                postEventDto.getReviewId(),
                postEventDto.getPlaceId(),
                postEventDto.getUserId());
    }

    private int getBeforePoint(PostEventDto postEventDto) throws IllegalArgumentException {
        Integer point = pointRepository.findByReviewIdAndPlaceIdAndUserId(
                postEventDto.getReviewId(),
                postEventDto.getPlaceId(),
                postEventDto.getUserId()
        ).getPoint();

        return point != null ? point : 0;
    }
}
