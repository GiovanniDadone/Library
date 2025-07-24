package com.giodad.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Autore.
 */
@Entity
@Table(name = "autore")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Autore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "autore")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "autore", "recensionis" }, allowSetters = true)
    private Set<Libro> libris = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Autore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Autore nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Libro> getLibris() {
        return this.libris;
    }

    public void setLibris(Set<Libro> libros) {
        if (this.libris != null) {
            this.libris.forEach(i -> i.setAutore(null));
        }
        if (libros != null) {
            libros.forEach(i -> i.setAutore(this));
        }
        this.libris = libros;
    }

    public Autore libris(Set<Libro> libros) {
        this.setLibris(libros);
        return this;
    }

    public Autore addLibri(Libro libro) {
        this.libris.add(libro);
        libro.setAutore(this);
        return this;
    }

    public Autore removeLibri(Libro libro) {
        this.libris.remove(libro);
        libro.setAutore(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Autore)) {
            return false;
        }
        return getId() != null && getId().equals(((Autore) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Autore{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
