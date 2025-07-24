package com.giodad.todolist.service.mapper;

import static com.giodad.todolist.domain.RecensioneAsserts.*;
import static com.giodad.todolist.domain.RecensioneTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecensioneMapperTest {

    private RecensioneMapper recensioneMapper;

    @BeforeEach
    void setUp() {
        recensioneMapper = new RecensioneMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecensioneSample1();
        // Clear relationships to avoid circular mapping issues in test
        expected.setLibro(null);
        expected.setUser(null);
        var actual = recensioneMapper.toEntity(recensioneMapper.toDto(expected));
        assertRecensioneAllPropertiesEquals(expected, actual);
    }
}
