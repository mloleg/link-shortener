package ru.mloleg.linkshortener.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record FilterLinkInfoRequest(
        UUID id,
        String linkPart,
        LocalDateTime endTimeFrom,
        LocalDateTime endTimeTo,
        String descriptionPart,
        Boolean active,
        PageableRequest pageableRequest) {

}
