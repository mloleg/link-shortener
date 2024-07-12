package ru.mloleg.linkshortener.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mloleg.linkshortener.dto.*;
import ru.mloleg.linkshortener.dto.common.CommonRequest;
import ru.mloleg.linkshortener.dto.common.CommonResponse;
import ru.mloleg.linkshortener.service.LinkInfoService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/link-infos")
public class LinkInfoController {
    private final LinkInfoService linkInfoService;

    @PostMapping
    public CommonResponse<LinkInfoResponse> postCreateShortLink(@RequestBody @Valid
                                                                CommonRequest<CreateShortLinkRequest> request) {
        return CommonResponse.<LinkInfoResponse>builder()
                             .body(linkInfoService.createLinkInfo(request.body()))
                             .build();
    }

    @PostMapping("/filter")
    public CommonResponse<List<LinkInfoResponse>> filter(@RequestBody @Valid
                                                         CommonRequest<FilterLinkInfoRequest> request) {
        List<LinkInfoResponse> linkInfoResponses = linkInfoService.findByFilter(request.body());

        return CommonResponse.<List<LinkInfoResponse>>builder()
                             .body(linkInfoResponses)
                             .build();
    }

    @PatchMapping
    public CommonResponse<LinkInfoResponse> update(@RequestBody @Valid
                                                   CommonRequest<UpdateLinkInfoRequest> request) {
        LinkInfoResponse linkInfoResponses = linkInfoService.updateById(request.body());

        return CommonResponse.<LinkInfoResponse>builder()
                             .body(linkInfoResponses)
                             .build();
    }

    @DeleteMapping
    public CommonResponse<LinkInfoResponse> deleteById(@RequestBody @Valid
                                                       CommonRequest<IdRequest> id) {
        return CommonResponse.<LinkInfoResponse>builder()
                             .body(linkInfoService.deleteById(id.body()
                                                                .id()))
                             .build();
    }
}
