package com.giodad.todolist.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoreAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAutoreAllPropertiesEquals(Autore expected, Autore actual) {
        assertAutoreAutoGeneratedPropertiesEquals(expected, actual);
        assertAutoreAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAutoreAllUpdatablePropertiesEquals(Autore expected, Autore actual) {
        assertAutoreUpdatableFieldsEquals(expected, actual);
        assertAutoreUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAutoreAutoGeneratedPropertiesEquals(Autore expected, Autore actual) {
        assertThat(actual)
            .as("Verify Autore auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAutoreUpdatableFieldsEquals(Autore expected, Autore actual) {
        assertThat(actual)
            .as("Verify Autore relevant properties")
            .satisfies(a -> assertThat(a.getNome()).as("check nome").isEqualTo(expected.getNome()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAutoreUpdatableRelationshipsEquals(Autore expected, Autore actual) {
        // empty method
    }
}
