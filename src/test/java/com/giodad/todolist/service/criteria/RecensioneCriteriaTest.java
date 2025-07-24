package com.giodad.todolist.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RecensioneCriteriaTest {

    @Test
    void newRecensioneCriteriaHasAllFiltersNullTest() {
        var recensioneCriteria = new RecensioneCriteria();
        assertThat(recensioneCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recensioneCriteriaFluentMethodsCreatesFiltersTest() {
        var recensioneCriteria = new RecensioneCriteria();

        setAllFilters(recensioneCriteria);

        assertThat(recensioneCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recensioneCriteriaCopyCreatesNullFilterTest() {
        var recensioneCriteria = new RecensioneCriteria();
        var copy = recensioneCriteria.copy();

        assertThat(recensioneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recensioneCriteria)
        );
    }

    @Test
    void recensioneCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recensioneCriteria = new RecensioneCriteria();
        setAllFilters(recensioneCriteria);

        var copy = recensioneCriteria.copy();

        assertThat(recensioneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recensioneCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recensioneCriteria = new RecensioneCriteria();

        assertThat(recensioneCriteria).hasToString("RecensioneCriteria{}");
    }

    private static void setAllFilters(RecensioneCriteria recensioneCriteria) {
        recensioneCriteria.id();
        recensioneCriteria.descrizione();
        recensioneCriteria.libroId();
        recensioneCriteria.userId();
        recensioneCriteria.distinct();
    }

    private static Condition<RecensioneCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescrizione()) &&
                condition.apply(criteria.getLibroId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecensioneCriteria> copyFiltersAre(RecensioneCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescrizione(), copy.getDescrizione()) &&
                condition.apply(criteria.getLibroId(), copy.getLibroId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
