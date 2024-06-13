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
        stopWatch.start();

        try {
            return linkInfoService.createLinkInfo(request);
        } finally {
            stopWatch.stop();
            log.info("Method 'createLinkInfo' performance: %dns"
                    .formatted(stopWatch.getTotalTimeNanos()));
        }
    }

    @Override
    public LinkInfo getByShortLink(String shortLink) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return linkInfoService.getByShortLink(shortLink);
        } finally {
            stopWatch.stop();
            log.info("Method 'GetByShortLink' performance: %dns"
                    .formatted(stopWatch.getTotalTimeNanos()));
        }
    }
}
