package ru.mloleg.linkshortener.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Builder
public record IdRequest(
        @NotNull(message = "UUID should not be empty")
        UUID id) {

}
