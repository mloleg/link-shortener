package ru.mloleg.linkshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.service.LinkService;

@SpringBootApplication
public class LinkShortenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkShortenerApplication.class, args);

        LinkService linkService = new LinkService();
        System.out.println(linkService.generateShortLink(new CreateShortLinkRequest()));
    }
}
