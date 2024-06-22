package ru.mloleg.linkshortener.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.dto.CreateShortLinkResponse;
import ru.mloleg.linkshortener.dto.common.CommonRequest;
import ru.mloleg.linkshortener.dto.common.CommonResponse;
import ru.mloleg.linkshortener.service.LinkInfoService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/link-infos")
public class AdminController {
    private final LinkInfoService linkInfoService;
    @PostMapping
    public CommonResponse<CreateShortLinkResponse> postCreateShortLink(
            @RequestBody CommonRequest<CreateShortLinkRequest> request) {
        log.info("Request for shortLink creation: %s".formatted(request.body()));

        CreateShortLinkResponse shortLinkResponse = linkInfoService.createLinkInfo(request.body());

        return CommonResponse.<CreateShortLinkResponse>builder()
                .body(shortLinkResponse)
                .build();
    }
}
