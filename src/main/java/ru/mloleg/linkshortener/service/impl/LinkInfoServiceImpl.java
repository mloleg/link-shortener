package ru.mloleg.linkshortener.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.exception.NotFoundException;
import ru.mloleg.linkshortener.model.LinkInfo;
import ru.mloleg.linkshortener.repository.LinkInfoRepository;
import ru.mloleg.linkshortener.service.LinkInfoService;


@Service("linkInfoService")
public class LinkInfoServiceImpl implements LinkInfoService {
    private final LinkInfoRepository linkInfoRepository;

    @Value("${link-shortener.short-link-length}")
    private int shortLinkLength;

    @Autowired
    public LinkInfoServiceImpl(LinkInfoRepository linkInfoRepository) {
        this.linkInfoRepository = linkInfoRepository;
    }

    public LinkInfo createLinkInfo(CreateShortLinkRequest request) {
        LinkInfo linkInfo = new LinkInfo();

        linkInfo.setLink(request.getLink());
        linkInfo.setEndTime(request.getEndTime());
        linkInfo.setDescription(request.getDescription());
        linkInfo.setActive(request.getActive());
        linkInfo.setShortLink(RandomStringUtils.randomAlphanumeric(shortLinkLength));

        return linkInfoRepository.save(linkInfo);
    }

    public LinkInfo getByShortLink(String shortLink) {
        return linkInfoRepository.findByShortLink(shortLink)
                .orElseThrow(() -> new NotFoundException(
                        "LinkInfo with shortLink {%s} was not found".formatted(shortLink)));
    }
}
