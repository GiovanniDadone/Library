package com.giodad.todolist.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.giodad.todolist.domain.Libro} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LibroDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String titolo;

    @NotNull
    private Double prezzo;

    @NotNull
    private AutoreDTO autore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public AutoreDTO getAutore() {
        return autore;
    }

    public void setAutore(AutoreDTO autore) {
        this.autore = autore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibroDTO)) {
            return false;
        }

        LibroDTO libroDTO = (LibroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, libroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibroDTO{" +
            "id=" + getId() +
            ", titolo='" + getTitolo() + "'" +
            ", prezzo=" + getPrezzo() +
            ", autore=" + getAutore() +
            "}";
    }
}
