package com.giodad.todolist.service.mapper;

import static com.giodad.todolist.domain.LibroAsserts.*;
import static com.giodad.todolist.domain.LibroTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibroMapperTest {

    private LibroMapper libroMapper;

    @BeforeEach
    void setUp() {
        libroMapper = new LibroMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLibroSample1();
        var actual = libroMapper.toEntity(libroMapper.toDto(expected));
        assertLibroAllPropertiesEquals(expected, actual);
    }
}
