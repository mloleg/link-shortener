package ru.mloleg.linkshortener.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mloleg.linkshortener.beanpostprocessor.LogExecutionTime;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.dto.CreateShortLinkResponse;
import ru.mloleg.linkshortener.exception.NotFoundException;
import ru.mloleg.linkshortener.model.LinkInfo;
import ru.mloleg.linkshortener.property.LinkShortenerProperty;
import ru.mloleg.linkshortener.repository.LinkInfoRepository;
import ru.mloleg.linkshortener.service.LinkInfoService;


@Service
public class LinkInfoServiceImpl implements LinkInfoService {
    private final LinkInfoRepository linkInfoRepository;
    private final LinkShortenerProperty linkShortenerProperty;

    @Autowired
    public LinkInfoServiceImpl(LinkInfoRepository linkInfoRepository, LinkShortenerProperty linkShortenerProperty) {
        this.linkInfoRepository = linkInfoRepository;
        this.linkShortenerProperty = linkShortenerProperty;
    }

    @LogExecutionTime
    public CreateShortLinkResponse createLinkInfo(CreateShortLinkRequest request) {
        LinkInfo linkInfo = new LinkInfo();

        linkInfo.setLink(request.link());
        linkInfo.setEndTime(request.endTime());
        linkInfo.setDescription(request.description());
        linkInfo.setActive(request.active());
        linkInfo.setShortLink(RandomStringUtils.randomAlphanumeric(linkShortenerProperty.shortLinkLength()));

        linkInfoRepository.save(linkInfo);

        return CreateShortLinkResponse.builder()
                .id(linkInfo.getId())
                .link(request.link())
                .endTime(request.endTime())
                .description(request.description())
                .active(request.active())
                .shortLink(linkInfo.getShortLink())
                .build();
    }

    @LogExecutionTime
    public LinkInfo getByShortLink(String shortLink) {
        return linkInfoRepository.findByShortLink(shortLink)
                .orElseThrow(() -> new NotFoundException(
                        "LinkInfo with shortLink {%s} was not found".formatted(shortLink)));
    }
}
