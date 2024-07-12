package ru.mloleg.linkshortener.dto;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record FilterLinkInfoRequest(
        UUID id,
        String linkPart,
        ZonedDateTime endTimeFrom,
        ZonedDateTime endTimeTo,
        String descriptionPart,
        Boolean active,
        PageableRequest pageableRequest) {
}
