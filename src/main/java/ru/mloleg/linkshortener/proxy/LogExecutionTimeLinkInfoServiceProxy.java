package ru.mloleg.linkshortener.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.model.LinkInfo;
import ru.mloleg.linkshortener.service.LinkInfoService;

@Slf4j
@Service("linkInfoServiceProxy")
public class LogExecutionTimeLinkInfoServiceProxy implements LinkInfoService {
    private final LinkInfoService linkInfoService;

    @Autowired
    public LogExecutionTimeLinkInfoServiceProxy(LinkInfoService linkInfoService) {
        this.linkInfoService = linkInfoService;
    }

    @Override
    public LinkInfo createLinkInfo(CreateShortLinkRequest request) {
        StopWatch stopWatch = new StopWatch();

        log.info("Starting to measure performance of method 'createLinkInfo'...");
        stopWatch.start();
        LinkInfo linkInfo = linkInfoService.createLinkInfo(request);
        stopWatch.stop();
        log.info("Method 'createLinkInfo' performance: %dns"
                .formatted(stopWatch.getTotalTimeNanos()));

        return linkInfo;
    }

    @Override
    public LinkInfo getByShortLink(String shortLink) {
        StopWatch stopWatch = new StopWatch();

        log.info("Starting to measure performance of method 'getByShortLink'...");
        stopWatch.start();
        LinkInfo linkInfo = linkInfoService.getByShortLink(shortLink);
        stopWatch.stop();
        log.info("Method 'GetByShortLink' performance: %dns"
                .formatted(stopWatch.getTotalTimeNanos()));

        return linkInfo;
    }
}
