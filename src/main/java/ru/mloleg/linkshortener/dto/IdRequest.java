package ru.mloleg.linkshortener.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class IdRequest {

    @NotNull(message = "UUID should not be empty")
    private UUID id;

    public @NotNull(message = "UUID should not be empty") UUID id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (IdRequest) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "IdRequest[" +
                "id=" + id + ']';
    }
}
