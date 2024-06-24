package ru.mloleg.linkshortener.dto.common;

import lombok.Builder;

@Builder
public record ValidationError(String field, String message) {
}
