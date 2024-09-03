package ru.mloleg.linkshortener.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mloleg.linkshortener.service.LinkInfoService;

@Service
public class SchedulerService {

    @Autowired
    private LinkInfoService linkInfoService;

    @Async("deleteInactiveLinksExecutor")
    @Scheduled(cron = "${link-shortener.scheduled-time}")
    public void deleteInactiveLinks() {
        linkInfoService.deleteInactiveLinks();
    }
}
