package ru.mloleg.linkshortener.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.mloleg.linkshortener.beanpostprocessor.LogExecutionTime;
import ru.mloleg.linkshortener.dto.*;
import ru.mloleg.linkshortener.exception.NotFoundException;
import ru.mloleg.linkshortener.exception.NotFoundPageException;
import ru.mloleg.linkshortener.mapper.LinkInfoMapper;
import ru.mloleg.linkshortener.model.LinkInfo;
import ru.mloleg.linkshortener.property.LinkShortenerProperty;
import ru.mloleg.linkshortener.repository.LinkInfoRepository;
import ru.mloleg.linkshortener.service.LinkInfoService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {
    private final LinkInfoRepository linkInfoRepository;
    private final LinkInfoMapper linkInfoMapper;
    private final LinkShortenerProperty linkShortenerProperty;


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
        LinkInfo linkInfo = linkInfoRepository
                .findByShortLinkAndActiveTrueAndEndTimeIsAfter(shortLink, ZonedDateTime.now())
                .orElseThrow(() -> new NotFoundPageException(
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
        Pageable pageable = createPageable(filterRequest);

        return linkInfoRepository.findByFilter(
                                         filterRequest.linkPart(),
                                         filterRequest.endTimeFrom(),
                                         filterRequest.endTimeTo(),
                                         filterRequest.descriptionPart(),
                                         filterRequest.active(),
                                         pageable)
                                 .stream()
                                 .map(linkInfoMapper::toResponse)
                                 .toList();
    }

    private static Pageable createPageable(FilterLinkInfoRequest filterRequest) {
        PageableRequest pageableRequest = filterRequest.pageableRequest();

        List<Sort.Order> sorts = pageableRequest.getSorts()
                                                .stream()
                                                .map(o -> new Sort.Order(
                                                        Sort.Direction.fromString(o.getDirection()),
                                                        o.getField()))
                                                .toList();

        Sort sort = CollectionUtils.isEmpty(sorts)
                ? Sort.unsorted()
                : Sort.by(sorts);

        return PageRequest.of(pageableRequest.getNumber() - 1, pageableRequest.getSize(), sort);
    }

    @Override
    public LinkInfoResponse updateById(UpdateLinkInfoRequest request) {
        LinkInfo toUpdate = linkInfoRepository.findById(request.id())
                                              .orElseThrow(() -> new NotFoundException(
                                                      "LinkInfo with UUID {%s} was not found".formatted(request.id())));

        if (request.link() != null) {
            toUpdate.setLink(request.link());
        }

        if (request.active() != null) {
            toUpdate.setActive(request.active());
        }

        if (request.description() != null) {
            toUpdate.setDescription(request.description());
        }

        if (request.endTime() != null) {
            toUpdate.setEndTime(request.endTime());
        }

        return linkInfoMapper.toResponse(toUpdate);
    }
}
