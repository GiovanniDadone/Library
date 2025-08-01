package com.giodad.todolist.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityAllPropertiesEquals(Authority expected, Authority actual) {
        assertAuthorityAutoGeneratedPropertiesEquals(expected, actual);
        assertAuthorityAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityAllUpdatablePropertiesEquals(Authority expected, Authority actual) {
        assertAuthorityUpdatableFieldsEquals(expected, actual);
        assertAuthorityUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityAutoGeneratedPropertiesEquals(Authority expected, Authority actual) {
        // empty method
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityUpdatableFieldsEquals(Authority expected, Authority actual) {
        assertThat(actual)
            .as("Verify Authority relevant properties")
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityUpdatableRelationshipsEquals(Authority expected, Authority actual) {
        // empty method
    }
}
