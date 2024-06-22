package ru.mloleg.linkshortener.dto;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record CreateShortLinkResponse(UUID id,
                                      String link,
                                      ZonedDateTime endTime,
                                      String description,
                                      Boolean active,
                                      String shortLink) {
}
