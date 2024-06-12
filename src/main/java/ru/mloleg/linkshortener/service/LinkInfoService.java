package ru.mloleg.linkshortener.service;

import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.model.LinkInfo;

public interface LinkInfoService {

    LinkInfo createLinkInfo(CreateShortLinkRequest request);

    LinkInfo getByShortLink(String shortLink);
}
