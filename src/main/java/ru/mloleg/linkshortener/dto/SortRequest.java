package ru.mloleg.linkshortener.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SortRequest {

    @NotEmpty
    private String field;
    @Pattern(regexp = "ASC|DESC")
    private String direction = "ASC";
}
