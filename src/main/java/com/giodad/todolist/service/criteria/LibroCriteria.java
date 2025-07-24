package com.giodad.todolist.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.giodad.todolist.domain.Libro} entity. This class is used
 * in {@link com.giodad.todolist.web.rest.LibroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /libros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LibroCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titolo;

    private DoubleFilter prezzo;

    private LongFilter autoreId;

    private LongFilter recensioniId;

    private Boolean distinct;

    public LibroCriteria() {}

    public LibroCriteria(LibroCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titolo = other.optionalTitolo().map(StringFilter::copy).orElse(null);
        this.prezzo = other.optionalPrezzo().map(DoubleFilter::copy).orElse(null);
        this.autoreId = other.optionalAutoreId().map(LongFilter::copy).orElse(null);
        this.recensioniId = other.optionalRecensioniId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LibroCriteria copy() {
        return new LibroCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitolo() {
        return titolo;
    }

    public Optional<StringFilter> optionalTitolo() {
        return Optional.ofNullable(titolo);
    }

    public StringFilter titolo() {
        if (titolo == null) {
            setTitolo(new StringFilter());
        }
        return titolo;
    }

    public void setTitolo(StringFilter titolo) {
        this.titolo = titolo;
    }

    public DoubleFilter getPrezzo() {
        return prezzo;
    }

    public Optional<DoubleFilter> optionalPrezzo() {
        return Optional.ofNullable(prezzo);
    }

    public DoubleFilter prezzo() {
        if (prezzo == null) {
            setPrezzo(new DoubleFilter());
        }
        return prezzo;
    }

    public void setPrezzo(DoubleFilter prezzo) {
        this.prezzo = prezzo;
    }

    public LongFilter getAutoreId() {
        return autoreId;
    }

    public Optional<LongFilter> optionalAutoreId() {
        return Optional.ofNullable(autoreId);
    }

    public LongFilter autoreId() {
        if (autoreId == null) {
            setAutoreId(new LongFilter());
        }
        return autoreId;
    }

    public void setAutoreId(LongFilter autoreId) {
        this.autoreId = autoreId;
    }

    public LongFilter getRecensioniId() {
        return recensioniId;
    }

    public Optional<LongFilter> optionalRecensioniId() {
        return Optional.ofNullable(recensioniId);
    }

    public LongFilter recensioniId() {
        if (recensioniId == null) {
            setRecensioniId(new LongFilter());
        }
        return recensioniId;
    }

    public void setRecensioniId(LongFilter recensioniId) {
        this.recensioniId = recensioniId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LibroCriteria that = (LibroCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titolo, that.titolo) &&
            Objects.equals(prezzo, that.prezzo) &&
            Objects.equals(autoreId, that.autoreId) &&
            Objects.equals(recensioniId, that.recensioniId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titolo, prezzo, autoreId, recensioniId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibroCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitolo().map(f -> "titolo=" + f + ", ").orElse("") +
            optionalPrezzo().map(f -> "prezzo=" + f + ", ").orElse("") +
            optionalAutoreId().map(f -> "autoreId=" + f + ", ").orElse("") +
            optionalRecensioniId().map(f -> "recensioniId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
