package com.giodad.todolist.service.mapper;

import com.giodad.todolist.domain.Autore;
import com.giodad.todolist.domain.Libro;
import com.giodad.todolist.service.dto.AutoreDTO;
import com.giodad.todolist.service.dto.LibroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Libro} and its DTO {@link LibroDTO}.
 */
@Mapper(componentModel = "spring")
public interface LibroMapper extends EntityMapper<LibroDTO, Libro> {
    @Mapping(target = "autore", source = "autore", qualifiedByName = "autoreId")
    LibroDTO toDto(Libro s);

    @Named("autoreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AutoreDTO toDtoAutoreId(Autore autore);
}
