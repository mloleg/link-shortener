package ru.mloleg.linkshortener.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IdRequest {

    @NotNull(message = "UUID should not be empty")
    private UUID id;
}
