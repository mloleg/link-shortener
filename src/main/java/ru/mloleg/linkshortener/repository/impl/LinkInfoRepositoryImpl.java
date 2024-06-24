package ru.mloleg.linkshortener.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mloleg.linkshortener.model.LinkInfo;
import ru.mloleg.linkshortener.repository.LinkInfoRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LinkInfoRepositoryImpl implements LinkInfoRepository {

    private final Map<String, LinkInfo> data = new ConcurrentHashMap<>();

    @Override
    public Optional<LinkInfo> findByShortLink(String shortLink) {
        return Optional.ofNullable(data.get(shortLink));
    }

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        linkInfo.setId(UUID.randomUUID());

        data.put(linkInfo.getShortLink(), linkInfo);

        return linkInfo;
    }

    @Override
    public List<LinkInfo> findAllShortLinks() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<LinkInfo> deleteById(UUID id) {
        Optional<LinkInfo> linkToDelete = data.values()
                                              .stream()
                                              .filter(o -> o.getId()
                                                            .equals(id))
                                              .findAny();

        return linkToDelete.map(linkInfo -> data.remove(linkInfo.getShortLink()));
    }
}
