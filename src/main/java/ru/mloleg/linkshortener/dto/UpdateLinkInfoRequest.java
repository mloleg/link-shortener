package ru.mloleg.linkshortener.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UpdateLinkInfoRequest(
        @NotNull(message = "ID must not be null")
        UUID id,
        @NotEmpty(message = "Link must not be null")
        @Size(min = 10, max = 4096, message = "Link length should be between 10 and 4096")
        @Pattern(regexp = "https?://.+\\..+", message = "Link does not match URL pattern")
        String link,
        @Future(message = "Expiration date should be in future")
        LocalDateTime endTime,
        @NotEmpty(message = "Description should not be empty")
        String description,
        @NotNull(message = "Active flag should not be empty")
        Boolean active) {

}