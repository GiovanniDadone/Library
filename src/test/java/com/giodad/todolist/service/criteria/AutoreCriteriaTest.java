package com.giodad.todolist.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AutoreCriteriaTest {

    @Test
    void newAutoreCriteriaHasAllFiltersNullTest() {
        var autoreCriteria = new AutoreCriteria();
        assertThat(autoreCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void autoreCriteriaFluentMethodsCreatesFiltersTest() {
        var autoreCriteria = new AutoreCriteria();

        setAllFilters(autoreCriteria);

        assertThat(autoreCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void autoreCriteriaCopyCreatesNullFilterTest() {
        var autoreCriteria = new AutoreCriteria();
        var copy = autoreCriteria.copy();

        assertThat(autoreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(autoreCriteria)
        );
    }

    @Test
    void autoreCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var autoreCriteria = new AutoreCriteria();
        setAllFilters(autoreCriteria);

        var copy = autoreCriteria.copy();

        assertThat(autoreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(autoreCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var autoreCriteria = new AutoreCriteria();

        assertThat(autoreCriteria).hasToString("AutoreCriteria{}");
    }

    private static void setAllFilters(AutoreCriteria autoreCriteria) {
        autoreCriteria.id();
        autoreCriteria.nome();
        autoreCriteria.libriId();
        autoreCriteria.distinct();
    }

    private static Condition<AutoreCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getLibriId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AutoreCriteria> copyFiltersAre(AutoreCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getLibriId(), copy.getLibriId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
