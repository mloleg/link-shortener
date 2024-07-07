package ru.mloleg.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.mloleg.linkshortener.beanpostprocessor.LogExecutionTime;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.dto.FilterLinkInfoRequest;
import ru.mloleg.linkshortener.dto.LinkInfoResponse;
import ru.mloleg.linkshortener.dto.UpdateShortLinkRequest;
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
    public LinkInfoResponse createLinkInfo(CreateShortLinkRequest request) {
        LinkInfo linkInfo = linkInfoMapper.toLinkInfo(request);
        linkInfo.setShortLink(RandomStringUtils.randomAlphanumeric(linkShortenerProperty.getShortLinkLength()));
        linkInfo.setOpeningCount(0L);

        linkInfoRepository.save(linkInfo);

        return linkInfoMapper.toResponse(linkInfo);
    }

    @LogExecutionTime
    public LinkInfo getByShortLink(String shortLink) {
        LinkInfo linkInfo = linkInfoRepository.findByShortLinkAndActiveTrue(shortLink)
                                              .orElseThrow(() -> new NotFoundException(
                                                      "LinkInfo with shortLink {%s} was not found".formatted(shortLink)));

        linkInfoRepository.incrementOpeningCountByShortLink(shortLink);

        return linkInfo;
    }

    @Override
    public List<LinkInfoResponse> getAllShortLinks() {
        return linkInfoRepository.findAll()
                                 .stream()
                                 .map(linkInfoMapper::toResponse)
                                 .toList();
    }

    @Override
    public LinkInfoResponse deleteById(UUID id) {
        LinkInfoResponse deletedLink = linkInfoMapper.toResponse(linkInfoRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "LinkInfo with UUID {%s} was not found".formatted(id))));

        linkInfoRepository.deleteById(id);

        return deletedLink;
    }

    @Override
    public List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterRequest) {
        return linkInfoRepository.findByFilter(
                                         filterRequest.linkPart(),
                                         filterRequest.endTimeFrom(),
                                         filterRequest.endTimeTo(),
                                         filterRequest.descriptionPart(),
                                         filterRequest.active())
                                 .stream()
                                 .map(linkInfoMapper::toResponse)
                                 .toList();
    }

    @Override
    public LinkInfoResponse updateById(UpdateShortLinkRequest request) {
        LinkInfo toUpdate = linkInfoMapper.toLinkInfo(request);

        toUpdate.setShortLink(
                linkInfoRepository
                        .findById(toUpdate.getId())
                        .orElseThrow(() -> new NotFoundException(
                                "LinkInfo with UUID {%s} was not found".formatted(toUpdate.getId())))
                        .getShortLink());

        return linkInfoMapper.toResponse(linkInfoRepository.save(toUpdate));
    }
}
