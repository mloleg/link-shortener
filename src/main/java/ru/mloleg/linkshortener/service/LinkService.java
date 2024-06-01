package ru.mloleg.linkshortener.service;

import org.apache.commons.lang3.RandomStringUtils;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.util.Constants;

public class LinkService {
    public String generateShortLink(CreateShortLinkRequest request) {
        return RandomStringUtils.randomAlphabetic(Constants.SHORT_LINK_LENGTH);
    }
}
