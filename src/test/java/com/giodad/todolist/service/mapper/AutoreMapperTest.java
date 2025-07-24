package com.giodad.todolist.service.mapper;

import static com.giodad.todolist.domain.AutoreAsserts.*;
import static com.giodad.todolist.domain.AutoreTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AutoreMapperTest {

    private AutoreMapper autoreMapper;

    @BeforeEach
    void setUp() {
        autoreMapper = Mappers.getMapper(AutoreMapper.class);
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAutoreSample1();
        var actual = autoreMapper.toEntity(autoreMapper.toDto(expected));
        assertAutoreAllPropertiesEquals(expected, actual);
    }
}
