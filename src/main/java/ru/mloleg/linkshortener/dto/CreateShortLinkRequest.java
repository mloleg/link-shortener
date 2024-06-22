package ru.mloleg.linkshortener.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Builder
public record CreateShortLinkRequest(String link,
                                    ZonedDateTime endTime,
                                    String description,
                                    Boolean active) {

}
