package ru.mloleg.linkshortener.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.linkshortener.model.LinkInfo;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkInfoRepository extends JpaRepository<LinkInfo, UUID> {

    @Query("""
            FROM LinkInfo li
            WHERE (:linkPart IS NULL OR li.link LIKE %:linkPart%)
            AND (cast(:endTimeFrom as date) IS NULL OR li.endTime >= :endTimeFrom)
            AND (cast(:endTimeTo as date) IS NULL OR li.endTime <= :endTimeTo)
            AND (:descriptionPart IS NULL OR li.description LIKE %:descriptionPart%)
            AND (:active IS NULL OR li.active = :active)
            """)
    List<LinkInfo> findByFilter(String linkPart,
                                ZonedDateTime endTimeFrom,
                                ZonedDateTime endTimeTo,
                                String descriptionPart,
                                Boolean active,
                                Pageable pageable);

    Optional<LinkInfo> findByShortLinkAndActiveTrueAndEndTimeIsAfter(String shortLink, ZonedDateTime endTime);

    @Query("""
            UPDATE LinkInfo li
            SET li.openingCount = li.openingCount + 1
            WHERE li.shortLink = :shortLink
            """)
    @Modifying
    @Transactional
    void incrementOpeningCountByShortLink(String shortLink);

    @Modifying
    @Transactional
    @Query("DELETE FROM LinkInfo WHERE updateTime < :beforeTime")
    int deleteByActiveFalseAndUpdateTimeIsBefore(ZonedDateTime beforeTime);
}
