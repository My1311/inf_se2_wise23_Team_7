package de.hbrs.se2.model.common;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("not null")
@Getter
public abstract class BaseEntity {

    @Id
    @NotNull
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final BaseEntity baseEntity = (BaseEntity) obj;
        return Objects.equals(this.id, baseEntity.id);
    }

}
