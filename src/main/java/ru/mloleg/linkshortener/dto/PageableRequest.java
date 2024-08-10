package ru.mloleg.linkshortener.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageableRequest {

    @Positive
    private Integer number = 1;
    @Positive
    private Integer size = 10;
    private List<SortRequest> sorts = new ArrayList<>();
}
