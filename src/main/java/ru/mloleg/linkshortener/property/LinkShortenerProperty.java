package ru.mloleg.linkshortener.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "link-shortener")
public record LinkShortenerProperty(String systemId,
                                    int shortLinkLength,
                                    boolean enableLogExecTime) {

}
