package com.giodad.todolist.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.giodad.todolist.domain.Recensione} entity. This class is used
 * in {@link com.giodad.todolist.web.rest.RecensioneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recensiones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecensioneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descrizione;

    private LongFilter libroId;

    private LongFilter userId;

    private Boolean distinct;

    public RecensioneCriteria() {}

    public RecensioneCriteria(RecensioneCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.descrizione = other.optionalDescrizione().map(StringFilter::copy).orElse(null);
        this.libroId = other.optionalLibroId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RecensioneCriteria copy() {
        return new RecensioneCriteria(this);
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

    public StringFilter getDescrizione() {
        return descrizione;
    }

    public Optional<StringFilter> optionalDescrizione() {
        return Optional.ofNullable(descrizione);
    }

    public StringFilter descrizione() {
        if (descrizione == null) {
            setDescrizione(new StringFilter());
        }
        return descrizione;
    }

    public void setDescrizione(StringFilter descrizione) {
        this.descrizione = descrizione;
    }

    public LongFilter getLibroId() {
        return libroId;
    }

    public Optional<LongFilter> optionalLibroId() {
        return Optional.ofNullable(libroId);
    }

    public LongFilter libroId() {
        if (libroId == null) {
            setLibroId(new LongFilter());
        }
        return libroId;
    }

    public void setLibroId(LongFilter libroId) {
        this.libroId = libroId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final RecensioneCriteria that = (RecensioneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(descrizione, that.descrizione) &&
            Objects.equals(libroId, that.libroId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descrizione, libroId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecensioneCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescrizione().map(f -> "descrizione=" + f + ", ").orElse("") +
            optionalLibroId().map(f -> "libroId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
