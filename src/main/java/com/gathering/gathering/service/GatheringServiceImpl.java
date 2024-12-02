package com.gathering.gathering.service;

import com.gathering.book.model.entity.Book;
import com.gathering.book.repository.BookRepository;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challenge.model.entity.ChallengeUser;
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
import org.springframework.security.core.userdetails.UserDetails;
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

    /**
     * 모임 생성
     */
    @Override
    @Transactional
    public void create(GatheringCreate gatheringCreate, UserDetails userDetails) {
        User user = userRepository.findByUsername("test");

        // Challenge 생성 ( 양방향 관계 설정 )
        ChallengeUser challengeUser = ChallengeUser.createChallengeUser(user);
        Challenge challenge = Challenge.createChallenge(gatheringCreate.getEndDate(), challengeUser);

        // Gathering 생성 ( 양방향 관계 설정 )
        Book book = bookRepository.findBookByBookIdAndCategoryId(gatheringCreate.getBookId(), gatheringCreate.getCategoryId());
        GatheringUser gatheringUser = GatheringUser.createGatheringUser(user, GatheringUserStatus.PARTICIPATING);
        Gathering gathering = Gathering.createGathering(
                gatheringCreate,
                challenge,
                book,
                gatheringUser,
                dateCalculateHolder,
                gatheringValidator);

        gatheringRepository.save(gathering);
    }

    /**
     * 모임 상세 조회
     */
    @Override
    public GatheringResponse getGatheringByGatheringId(Long gatheringId) {
        return GatheringResponse.fromEntity(gatheringRepository.getGatheringByGatheringId(gatheringId));
    }

    /**
     * 모임 참여
     */
    @Override
    @Transactional
    public void join(Long gatheringId, String userName) {
        User user = userRepository.findByUsername(userName);
        GatheringUser gatheringUser = GatheringUser.createGatheringUser(user, GatheringUserStatus.PARTICIPATING);
        gatheringRepository.getById(gatheringId).join(user.getId(), gatheringUser, gatheringValidator);
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
     * 모임 떠나기
     */
    @Override
    @Transactional
    public void leave(Long gatheringId, String username, GatheringUserStatus gatheringUserStatus) {
        Gathering gathering = gatheringRepository.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus);
        User user = userRepository.findByUsername(username);
        gathering.leave(user);
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
