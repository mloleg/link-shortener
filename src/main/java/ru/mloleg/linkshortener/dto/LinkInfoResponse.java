package ru.mloleg.linkshortener.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record LinkInfoResponse(
        UUID id,
        String link,
        LocalDateTime endTime,
        String description,
        Boolean active,
        String shortLink,
        Long openingCount) {

}
