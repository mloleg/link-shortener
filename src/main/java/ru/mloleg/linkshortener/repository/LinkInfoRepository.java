package ru.mloleg.linkshortener.repository;

import ru.mloleg.linkshortener.model.LinkInfo;

import java.util.Optional;

public interface LinkInfoRepository {
    Optional<LinkInfo> findByShortLink(String shortLink);

    LinkInfo save(LinkInfo linkInfo);
}
