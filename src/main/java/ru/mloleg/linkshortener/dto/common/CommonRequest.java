package ru.mloleg.linkshortener.dto.common;

import lombok.Builder;

@Builder
public record CommonRequest<T>(T body) {
}
