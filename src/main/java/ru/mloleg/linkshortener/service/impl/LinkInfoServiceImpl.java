package ru.mloleg.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.mloleg.linkshortener.beanpostprocessor.LogExecutionTime;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.dto.CreateShortLinkResponse;
import ru.mloleg.linkshortener.exception.NotFoundException;
import ru.mloleg.linkshortener.mapper.LinkInfoMapper;
import ru.mloleg.linkshortener.model.LinkInfo;
import ru.mloleg.linkshortener.property.LinkShortenerProperty;
import ru.mloleg.linkshortener.repository.LinkInfoRepository;
import ru.mloleg.linkshortener.service.LinkInfoService;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {
    private final LinkInfoRepository linkInfoRepository;
    private final LinkShortenerProperty linkShortenerProperty;
    private final LinkInfoMapper linkInfoMapper;

    @LogExecutionTime
    public CreateShortLinkResponse createLinkInfo(CreateShortLinkRequest request) {
        LinkInfo linkInfo = linkInfoMapper.toLinkInfo(request);
        linkInfo.setShortLink(RandomStringUtils.randomAlphanumeric(linkShortenerProperty.getShortLinkLength()));
        linkInfo.setOpeningCount(0L);

        linkInfoRepository.save(linkInfo);

        return linkInfoMapper.toResponse(linkInfo);
    }

    @LogExecutionTime
    public LinkInfo getByShortLink(String shortLink) {
        return linkInfoRepository.findByShortLink(shortLink)
                                 .orElseThrow(() -> new NotFoundException(
                                         "LinkInfo with shortLink {%s} was not found".formatted(shortLink)));
    }

    @Override
    public List<CreateShortLinkResponse> getAllShortLinks() {
        return linkInfoRepository.findAllShortLinks()
                                 .stream()
                                 .map(linkInfoMapper::toResponse)
                                 .toList();
    }


    public CreateShortLinkResponse deleteById(UUID id) {
        return linkInfoMapper.toResponse(linkInfoRepository
                .deleteById(id)
                .orElseThrow(() -> new NotFoundException(
                        "LinkInfo with UUID {%s} was not found".formatted(id))));
    }
}
