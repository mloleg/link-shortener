package ru.mloleg.linkshortener.dto.common;

import lombok.Builder;

@Builder
public record CommonResponse<T>(T body) {
}
