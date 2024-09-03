package ru.mloleg.linkshortener.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mloleg.linkshortener.model.LinkInfo;
import ru.mloleg.linkshortener.service.LinkInfoService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LinkShortenerController {

    private final LinkInfoService linkInfoService;

    @GetMapping("/short-link/{shortLink}")
    public ResponseEntity<String> getByShortLink(@PathVariable String shortLink) {
        log.info("New request for redirect by shortLink {%s}".formatted(shortLink));

        LinkInfo linkInfo = linkInfoService.getByShortLink(shortLink);

        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header(HttpHeaders.LOCATION, linkInfo.getLink())
                .build();
    }
}
