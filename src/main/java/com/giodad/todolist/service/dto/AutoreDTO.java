package com.giodad.todolist.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.giodad.todolist.domain.Autore} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AutoreDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoreDTO)) {
            return false;
        }

        AutoreDTO autoreDTO = (AutoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, autoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutoreDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
