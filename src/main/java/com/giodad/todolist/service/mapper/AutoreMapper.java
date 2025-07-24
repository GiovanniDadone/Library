package com.giodad.todolist.service.mapper;

import com.giodad.todolist.domain.Autore;
import com.giodad.todolist.service.dto.AutoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Autore} and its DTO {@link AutoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface AutoreMapper extends EntityMapper<AutoreDTO, Autore> {}
