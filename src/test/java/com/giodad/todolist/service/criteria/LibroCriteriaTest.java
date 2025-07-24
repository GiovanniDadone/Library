package com.giodad.todolist.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LibroCriteriaTest {

    @Test
    void newLibroCriteriaHasAllFiltersNullTest() {
        var libroCriteria = new LibroCriteria();
        assertThat(libroCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void libroCriteriaFluentMethodsCreatesFiltersTest() {
        var libroCriteria = new LibroCriteria();

        setAllFilters(libroCriteria);

        assertThat(libroCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void libroCriteriaCopyCreatesNullFilterTest() {
        var libroCriteria = new LibroCriteria();
        var copy = libroCriteria.copy();

        assertThat(libroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(libroCriteria)
        );
    }

    @Test
    void libroCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var libroCriteria = new LibroCriteria();
        setAllFilters(libroCriteria);

        var copy = libroCriteria.copy();

        assertThat(libroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(libroCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var libroCriteria = new LibroCriteria();

        assertThat(libroCriteria).hasToString("LibroCriteria{}");
    }

    private static void setAllFilters(LibroCriteria libroCriteria) {
        libroCriteria.id();
        libroCriteria.titolo();
        libroCriteria.prezzo();
        libroCriteria.autoreId();
        libroCriteria.recensioniId();
        libroCriteria.distinct();
    }

    private static Condition<LibroCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitolo()) &&
                condition.apply(criteria.getPrezzo()) &&
                condition.apply(criteria.getAutoreId()) &&
                condition.apply(criteria.getRecensioniId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LibroCriteria> copyFiltersAre(LibroCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitolo(), copy.getTitolo()) &&
                condition.apply(criteria.getPrezzo(), copy.getPrezzo()) &&
                condition.apply(criteria.getAutoreId(), copy.getAutoreId()) &&
                condition.apply(criteria.getRecensioniId(), copy.getRecensioniId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
