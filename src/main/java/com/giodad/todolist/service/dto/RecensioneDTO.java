package com.giodad.todolist.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.giodad.todolist.domain.Recensione} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecensioneDTO implements Serializable {

    private Long id;

    private String descrizione;

    @NotNull
    private LibroDTO libro;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LibroDTO getLibro() {
        return libro;
    }

    public void setLibro(LibroDTO libro) {
        this.libro = libro;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecensioneDTO)) {
            return false;
        }

        RecensioneDTO recensioneDTO = (RecensioneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recensioneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecensioneDTO{" +
            "id=" + getId() +
            ", descrizione='" + getDescrizione() + "'" +
            ", libro=" + getLibro() +
            ", user=" + getUser() +
            "}";
    }
}
