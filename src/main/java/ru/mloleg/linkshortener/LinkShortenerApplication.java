package ru.mloleg.linkshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.service.LinkInfoService;

import java.time.ZonedDateTime;

@SpringBootApplication
public class LinkShortenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkShortenerApplication.class, args);

        LinkInfoService linkService = new LinkInfoService();

        CreateShortLinkRequest shortLinkRequest =
                new CreateShortLinkRequest(
                        "www.google.com",
                        ZonedDateTime.now().plusHours(12),
                        "Google link",
                        true);

        System.out.println(linkService.getByShortLink(
                linkService.createLinkInfo(shortLinkRequest).getShortLink()));
    }
}
