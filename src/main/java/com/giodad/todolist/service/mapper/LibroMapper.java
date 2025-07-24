package com.giodad.todolist.service.mapper;

import com.giodad.todolist.domain.Libro;
import com.giodad.todolist.service.dto.LibroDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { AutoreMapper.class }, nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS)
public interface LibroMapper extends EntityMapper<LibroDTO, Libro> {
    @Mapping(target = "autore", source = "autore", qualifiedByName = "autoreComplete")
    LibroDTO toDto(Libro s);

    @Mapping(target = "recensionis", ignore = true)
    @Mapping(target = "removeRecensioni", ignore = true)
    Libro toEntity(LibroDTO libroDTO);

    @Override
    @Mapping(target = "recensionis", ignore = true)
    @Mapping(target = "removeRecensioni", ignore = true)
    void partialUpdate(@MappingTarget Libro entity, LibroDTO dto);

    @Named("libroComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titolo", source = "titolo")
    @Mapping(target = "prezzo", source = "prezzo")
    LibroDTO toDtoLibroComplete(Libro libro);

    /**
     * Mapping per lista di libri senza relazioni (performance ottimizzata).
     */
    @Named("libroSummaryList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titolo", source = "titolo")
    @Mapping(target = "prezzo", source = "prezzo")
    java.util.List<LibroDTO> toDtoLibroSummaryList(java.util.List<Libro> libri);
}
