package ru.mloleg.linkshortener.dto.common;

import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record CommonRequest<T>(@Valid T body) {
}
