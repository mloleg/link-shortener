package ru.mloleg.linkshortener.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.linkshortener.AbstractTest;
import ru.mloleg.linkshortener.model.LinkInfo;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LinkShortenerControllerTest extends AbstractTest {

    @Test
    @Transactional
    void when_redirect_expect_success() throws Exception {
        String shortLink = "a1b2v3";
        String link = "http://google.com";

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setLink(link);
        linkInfo.setShortLink(shortLink);
        linkInfo.setActive(true);

        linkInfoRepository.save(linkInfo);

        mockMvc.perform(get("/api/v1/short-link/" + shortLink))
                .andExpect(status().isTemporaryRedirect())
                .andExpect(header().string(HttpHeaders.LOCATION, link));

        Mockito.verify(linkInfoRepository)
                .findByShortLinkAndActiveTrueAndEndTimeIsNullOrEndTimeIsAfter(
                        eq(shortLink), any(LocalDateTime.class));
    }

}
