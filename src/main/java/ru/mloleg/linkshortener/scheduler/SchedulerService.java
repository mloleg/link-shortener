package ru.mloleg.linkshortener.scheduler;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mloleg.linkshortener.service.LinkInfoService;

@Service
@NoArgsConstructor
public class SchedulerService {

    @Autowired
    private LinkInfoService linkInfoService;

    @Async("simpleExecutor")
    @Scheduled(cron = "${link-shortener.scheduled-time}")
    public void deleteInactiveLinks() {
        linkInfoService.deleteInactiveLinks();
    }
}
