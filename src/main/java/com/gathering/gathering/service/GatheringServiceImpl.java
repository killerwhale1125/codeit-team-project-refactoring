package com.gathering.gathering.service;

import com.gathering.book.model.entity.Book;
import com.gathering.book.repository.BookRepository;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challenge.model.entity.ChallengeUser;
import com.gathering.challenge.repository.ChallengeRepository;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.gathering.validator.GatheringValidator;
import com.gathering.user.model.dto.response.UserResponseDto;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserRepository;
import com.gathering.util.holder.DateCalculateHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringServiceImpl implements GatheringService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final DateCalculateHolder dateCalculateHolder;
    private final BookRepository bookRepository;
    private final GatheringValidator gatheringValidator;
    private final ChallengeRepository challengeRepository;

    /**
     * 모임 생성
     */
    @Override
    @Transactional
    public void create(GatheringCreate gatheringCreate, String username) {
        User user = userRepository.findByUsername(username);
        Book book = bookRepository.findBookByBookIdAndCategoryId(gatheringCreate.getBookId(), gatheringCreate.getCategoryId());
        book.incrementSelectedCount();
        // Gathering 생성 ( 양방향 관계 설정 )
        Gathering gathering = Gathering.createGathering(
                gatheringCreate,
                Challenge.createChallenge(gatheringCreate, ChallengeUser.createChallengeUser(user)),    // Challenge 생성 ( 양방향 관계 설정 )
                book,
                GatheringUser.createGatheringUser(user, GatheringUserStatus.PARTICIPATING),
                dateCalculateHolder,
                gatheringValidator);

        gatheringRepository.save(gathering);
    }

    /**
     * 모임 참여
     */
    @Override
    @Transactional
    public void join(Long gatheringId, String userName) {
        User user = userRepository.findByUsername(userName);
        Gathering gathering = gatheringRepository.getById(gatheringId);
        Gathering.join(gathering, user, GatheringUser.createGatheringUser(user, GatheringUserStatus.PARTICIPATING), gatheringValidator);
        Challenge.join(gathering.getChallenge(), ChallengeUser.createChallengeUser(user));
    }

    /**
     * 모임 떠나기
     */
    @Override
    @Transactional
    public void leave(Long gatheringId, String username, GatheringUserStatus gatheringUserStatus) {
        Gathering gathering = gatheringRepository.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus);
        User user = userRepository.findByUsername(username);
        Challenge.leave(gathering.getChallenge(), user);
        Gathering.leave(gathering, user);
    }

    /**
     * 모임 삭제
     */
    @Override
    @Transactional
    public void delete(Long gatheringId, String userName) {
        User user = userRepository.findByUsername(userName);
        Gathering gathering = gatheringRepository.getById(gatheringId);
        gatheringValidator.validateOwner(gathering.getOwnerId(), user.getId());
        gatheringRepository.delete(gathering);
    }

    /**
     * 모임 상세 조회
     */
    @Override
    public GatheringResponse getGatheringByGatheringId(Long gatheringId) {
        return GatheringResponse.fromEntity(gatheringRepository.getGatheringByGatheringId(gatheringId));
    }

    /**
     * 모임 참여 상태에 따른 유저 조회
     */
    @Override
    public List<UserResponseDto> findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringUserStatus) {
        return gatheringRepository.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus).getGatheringUsers().stream()
                .map(gatheringUser -> UserResponseDto.fromEntity(gatheringUser.getUser()))
                .collect(Collectors.toList());
    }

}
