package ru.mloleg.linkshortener.service;

import org.apache.commons.lang3.RandomStringUtils;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.exception.NotFoundException;
import ru.mloleg.linkshortener.model.LinkInfo;
import ru.mloleg.linkshortener.repository.LinkInfoRepository;
import ru.mloleg.linkshortener.repository.impl.LinkInfoRepositoryImpl;
import ru.mloleg.linkshortener.util.Constants;

public class LinkInfoService {
    private final LinkInfoRepository linkInfoRepository = new LinkInfoRepositoryImpl();

    public LinkInfo createLinkInfo(CreateShortLinkRequest request) {
        LinkInfo linkInfo = new LinkInfo();

        linkInfo.setLink(request.getLink());
        linkInfo.setEndTime(request.getEndTime());
        linkInfo.setDescription(request.getDescription());
        linkInfo.setActive(request.getActive());
        linkInfo.setShortLink(RandomStringUtils.randomAlphanumeric(Constants.SHORT_LINK_LENGTH));

        return linkInfoRepository.save(linkInfo);
    }

    public LinkInfo getByShortLink(String shortLink) {
        return linkInfoRepository.findByShortLink(shortLink)
                .orElseThrow(() -> new NotFoundException(
                        "LinkInfo with shortLink(%s) was not found".formatted(shortLink)));
    }
}
