package ru.mloleg.linkshortener.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {

    @Positive
    private Integer number = 1;
    @Positive
    private Integer size = 10;
    private List<SortRequest> sorts = new ArrayList<>();
}
