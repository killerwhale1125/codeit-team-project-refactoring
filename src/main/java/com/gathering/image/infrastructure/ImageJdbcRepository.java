package com.gathering.image.infrastructure;

import com.gathering.image.domain.ImageDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageJdbcRepository {
    private static final String BULK_INSERT_SQL = "INSERT INTO " +
            "`IMAGE`(`IMAGE_NAME` , `IMAGE_URL`, `CREATED_TIME`, `MODIFIED_TIME`, `IS_REMOVED`) " +
            "VALUES(?, ?, ?, ?, ?)";

//    private static final String BULK_DELETE_SQL = "DELETE FROM `IMAGE` WHERE `GATHERING_ID` = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * JdbcTemplate가 JPA보다 성능상 이점이 많아서 JdbcTemplate 사용
     */
    public void saveAll(List<ImageDomain> images) {
        jdbcTemplate.batchUpdate(BULK_INSERT_SQL,
                images,
                images.size(),
                (ps, image) -> {
                    LocalDateTime now = LocalDateTime.now();
                    ps.setString(1, image.getName());
                    ps.setString(2, image.getUrl());
                    ps.setTimestamp(3, Timestamp.valueOf(now));
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                    ps.setBoolean(5, image.isRemoved());
                });
    }

    /**
     * 특정 gatheringId에 해당하는 이미지 벌크 삭제
     */
//    public void deleteAllByGatheringId(Long gatheringId) {
//        jdbcTemplate.update(BULK_DELETE_SQL, gatheringId);
//    }
}
