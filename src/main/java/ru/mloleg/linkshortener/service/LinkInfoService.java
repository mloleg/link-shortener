package ru.mloleg.linkshortener.service;

import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.dto.CreateShortLinkResponse;
import ru.mloleg.linkshortener.model.LinkInfo;

public interface LinkInfoService {

    CreateShortLinkResponse createLinkInfo(CreateShortLinkRequest request);

    LinkInfo getByShortLink(String shortLink);

}
