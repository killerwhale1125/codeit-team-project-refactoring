package com.gathering.gathering.service;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.book.repository.BookRepository;
import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.challenge.repository.ChallengeRepository;
import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.challengeuser.repository.ChallengeUserRepository;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringUpdate;
import com.gathering.gathering.model.dto.MyPageGatheringsCountResponse;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.gathering.util.GatheringActions;
import com.gathering.gathering.validator.GatheringValidator;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.gatheringuser.model.entity.GatheringUser;
import com.gathering.gatheringuser.repository.GatheringUserRepository;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.image.model.entity.EntityType;
import com.gathering.image.service.ImageService;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.response.UserResponseDto;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserRepository;
import com.gathering.util.string.UUIDUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class GatheringServiceImpl implements GatheringService {

    private final GatheringRepository gatheringRepository;
    private final GatheringUserRepository gatheringUserRepository;
    private final UserRepository userRepository;
    private final GatheringActions gatheringActions;
    private final BookRepository bookRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeRepository challengeRepository;
    private final ImageService imageService;
    private final GatheringValidator gatheringValidator;
    private final UUIDUtils uuidUtils;

    @Override
    @Transactional
    public GatheringDomain create(GatheringCreate gatheringCreate, List<MultipartFile> files, String username) {
        UserDomain user = userRepository.findByUsername(username);
        ChallengeDomain challenge = createChallengeForUserAndSave(gatheringCreate, user);
        BookDomain book = bookRepository.findById(gatheringCreate.getBookId());
        List<ImageDomain> images = imageService.uploadImage(files, EntityType.GATHERING, uuidUtils);
        return createGatheringAndSave(gatheringCreate, user, challenge, book, images, gatheringValidator);
    }

    @Override
    @Transactional
    public void join(Long gatheringId, String userName) {
        UserDomain user = userRepository.findByUsername(userName);

        GatheringDomain gathering = gatheringRepository.getGatheringAndGatheringUsersById(gatheringId);
        GatheringDomain.join(gathering, user, gatheringValidator);
        gatheringRepository.save(gathering);
        gatheringUserRepository.save(GatheringUserDomain.create(user, gathering));
        challengeUserRepository.save(ChallengeUserDomain.create(user, gathering.getChallenge()));
    }

    @Override
    @Transactional
    public void leave(Long gatheringId, String username, GatheringUserStatus gatheringUserStatus) {
        gatheringActions.leaveGathering(gatheringId, username, gatheringUserStatus);
    }

    @Override
    @Transactional
    public void wish(Long gatheringId, String username) {
        UserDomain user = userRepository.findByUsername(username);
        User.addWish(user, gatheringRepository.findIdById(gatheringId));
        userRepository.save(user);
    }

    @Override
    public void update(GatheringUpdate gatheringUpdate, String username) {

    }

    @Override
    public LocalDate calculateEndDate(LocalDate startDate, GatheringWeek gatheringWeek) {
        return startDate.plusDays(gatheringWeek.getWeek());
    }

    @Override
    public MyPageGatheringsCountResponse getMyPageGatheringsCount(String username) {
        UserDomain user = userRepository.findByUsername(username);
        return gatheringActions.getMyPageGatheringsCount(user);
    }

    @Override
    @Transactional
    public void readBook(String username, long gatheringId) {
        gatheringActions.readBookGathering(gatheringId, username);
    }

    @Override
    @Transactional
    public void delete(Long gatheringId, String userName) {
        gatheringRepository.delete(gatheringActions.deleteGathering(gatheringId, userName));
    }

    @Override
    public List<UserResponseDto> findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringUserStatus) {
        List<GatheringUser> gatheringUsers = gatheringRepository.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus).getGatheringUsers();
        return gatheringActions.mapToUserResponseDtos(gatheringUsers);
    }

    private GatheringDomain createGatheringAndSave(GatheringCreate gatheringCreate, UserDomain user, ChallengeDomain challenge, BookDomain book, List<ImageDomain> images, GatheringValidator gatheringValidator) {
        GatheringDomain gathering = gatheringRepository.save(
                GatheringDomain.create(
                        gatheringCreate,
                        challenge,
                        book,
                        images,
                        user,
                        gatheringValidator)
        );

        gatheringUserRepository.save(GatheringUserDomain.create(user, gathering));
        return gathering;
    }

    private ChallengeDomain createChallengeForUserAndSave(GatheringCreate gatheringCreate, UserDomain user) {
        ChallengeDomain challenge = challengeRepository.save(ChallengeDomain.create(gatheringCreate));
        challengeUserRepository.save(ChallengeUserDomain.create(user, challenge));
        return challenge;
    }

}
