package net.cryptic_game.backend.dto.website;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

import net.getnova.framework.core.Validatable;
import net.getnova.framework.core.exception.ValidationException;

@Data
@AllArgsConstructor
public class TeamDepartment implements Validatable {

    private final UUID id;
    private final String name;
    private final String description;

    public TeamDepartment(
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description
    ) {
        this.id = null;
        this.name = name;
        this.description = description;
    }

    @Override
    public void validate() throws ValidationException {
        if (this.name == null || this.name.isBlank()) {
            throw new ValidationException("name", "NOT_BLANK");
        }
    }
}
