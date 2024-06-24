package ru.mloleg.linkshortener.repository;

import ru.mloleg.linkshortener.model.LinkInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkInfoRepository {
    Optional<LinkInfo> findByShortLink(String shortLink);

    LinkInfo save(LinkInfo linkInfo);

    List<LinkInfo> findAllShortLinks();

    Optional<LinkInfo> deleteById(UUID id);
}
