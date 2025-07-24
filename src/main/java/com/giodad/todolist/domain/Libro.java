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
 * A Libro.
 */
@Entity
@Table(name = "libro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "titolo", length = 200, nullable = false)
    private String titolo;

    @NotNull
    @Column(name = "prezzo", nullable = false)
    private Double prezzo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "libris" }, allowSetters = true)
    private Autore autore;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "libro")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "libro", "user" }, allowSetters = true)
    private Set<Recensione> recensionis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Libro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return this.titolo;
    }

    public Libro titolo(String titolo) {
        this.setTitolo(titolo);
        return this;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Double getPrezzo() {
        return this.prezzo;
    }

    public Libro prezzo(Double prezzo) {
        this.setPrezzo(prezzo);
        return this;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Autore getAutore() {
        return this.autore;
    }

    public void setAutore(Autore autore) {
        this.autore = autore;
    }

    public Libro autore(Autore autore) {
        this.setAutore(autore);
        return this;
    }

    public Set<Recensione> getRecensionis() {
        return this.recensionis;
    }

    public void setRecensionis(Set<Recensione> recensiones) {
        if (this.recensionis != null) {
            this.recensionis.forEach(i -> i.setLibro(null));
        }
        if (recensiones != null) {
            recensiones.forEach(i -> i.setLibro(this));
        }
        this.recensionis = recensiones;
    }

    public Libro recensionis(Set<Recensione> recensiones) {
        this.setRecensionis(recensiones);
        return this;
    }

    public Libro addRecensioni(Recensione recensione) {
        this.recensionis.add(recensione);
        recensione.setLibro(this);
        return this;
    }

    public Libro removeRecensioni(Recensione recensione) {
        this.recensionis.remove(recensione);
        recensione.setLibro(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Libro)) {
            return false;
        }
        return getId() != null && getId().equals(((Libro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Libro{" +
            "id=" + getId() +
            ", titolo='" + getTitolo() + "'" +
            ", prezzo=" + getPrezzo() +
            "}";
    }
}
