package ru.mloleg.linkshortener.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.dto.CreateShortLinkResponse;
import ru.mloleg.linkshortener.dto.common.CommonRequest;
import ru.mloleg.linkshortener.dto.common.CommonResponse;
import ru.mloleg.linkshortener.service.LinkInfoService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/link-infos")
public class LinkInfoController {
    private final LinkInfoService linkInfoService;

    @PostMapping
    public CommonResponse<CreateShortLinkResponse> postCreateShortLink(
            @RequestBody @Valid CommonRequest<CreateShortLinkRequest> request) {
        log.info("Request for shortLink creation: %s".formatted(request.body()));

        CreateShortLinkResponse shortLinkResponse = linkInfoService.createLinkInfo(request.body());

        return CommonResponse.<CreateShortLinkResponse>builder()
                             .body(shortLinkResponse)
                             .build();
    }

    @GetMapping
    public CommonResponse<List<CreateShortLinkResponse>> getAllShortLinks() {
        log.info("Request for all short links");

        return CommonResponse.<List<CreateShortLinkResponse>>builder()
                             .body(linkInfoService.getAllShortLinks())
                             .build();
    }

    @DeleteMapping
    public CommonResponse<CreateShortLinkResponse> deleteById(@RequestBody CommonRequest<UUID> id) {
        log.info("Request for shortLink deletion by UUID: %s".formatted(id));

        return CommonResponse.<CreateShortLinkResponse>builder()
                             .body(linkInfoService.deleteById(id.body()))
                             .build();
    }
}
