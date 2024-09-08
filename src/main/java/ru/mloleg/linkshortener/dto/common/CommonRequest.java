package ru.mloleg.linkshortener.dto.common;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class CommonRequest<T> {

    @Valid
    private T body;
}
