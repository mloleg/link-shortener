package ru.mloleg.linkshortener.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "link-shortener")
public final class LinkShortenerProperty {
    private String systemId;
    private int shortLinkLength;
    private boolean enableLogExecTime;

}
