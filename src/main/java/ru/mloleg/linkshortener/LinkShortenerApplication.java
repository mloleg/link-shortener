package ru.mloleg.linkshortener;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.service.LinkInfoService;

import java.time.ZonedDateTime;

@SpringBootApplication
public class LinkShortenerApplication {
    private final LinkInfoService linkInfoService;
    private final LinkInfoService linkInfoServiceProxy;

    @Autowired
    public LinkShortenerApplication(
            @Qualifier("linkInfoService") LinkInfoService linkInfoService,
            @Qualifier("linkInfoServiceProxy") LinkInfoService linkInfoServiceProxy) {
        this.linkInfoService = linkInfoService;
        this.linkInfoServiceProxy = linkInfoServiceProxy;
    }

    @PostConstruct
    public void test() {
        CreateShortLinkRequest shortLinkRequest =
                new CreateShortLinkRequest(
                        "www.google.com",
                        ZonedDateTime.now().plusHours(12),
                        "Google link",
                        true);

        System.out.println(linkInfoService.getByShortLink(
                linkInfoService.createLinkInfo(shortLinkRequest).getShortLink()));

        System.out.println(linkInfoServiceProxy.getByShortLink(
                linkInfoServiceProxy.createLinkInfo(shortLinkRequest).getShortLink()));
    }

    public static void main(String[] args) {
        SpringApplication.run(LinkShortenerApplication.class, args);
    }
}
