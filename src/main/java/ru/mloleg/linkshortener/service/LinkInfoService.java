package ru.mloleg.linkshortener.service;

import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.dto.FilterLinkInfoRequest;
import ru.mloleg.linkshortener.dto.LinkInfoResponse;
import ru.mloleg.linkshortener.dto.UpdateLinkInfoRequest;
import ru.mloleg.linkshortener.model.LinkInfo;

import java.util.List;
import java.util.UUID;

public interface LinkInfoService {

    LinkInfoResponse createLinkInfo(CreateShortLinkRequest request);

    LinkInfo getByShortLink(String shortLink);

    List<LinkInfoResponse> getAllShortLinks();

    LinkInfoResponse deleteById(UUID id);

    List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterLinkInfoRequest);

    LinkInfoResponse updateById(UpdateLinkInfoRequest request);

    void deleteInactiveLinks();
}
