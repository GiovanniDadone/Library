package com.giodad.todolist.service.mapper;

import com.giodad.todolist.domain.Autore;
import com.giodad.todolist.service.dto.AutoreDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN
)
public interface AutoreMapper extends EntityMapper<AutoreDTO, Autore> {
    AutoreDTO toDto(Autore autore);

    @Mapping(target = "libris", ignore = true)
    @Mapping(target = "removeLibri", ignore = true)
    Autore toEntity(AutoreDTO autoreDTO);

    @Override
    @Mapping(target = "libris", ignore = true)
    @Mapping(target = "removeLibri", ignore = true)
    void partialUpdate(@MappingTarget Autore entity, AutoreDTO dto);

    @Named("autoreComplete")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AutoreDTO toDtoAutoreComplete(Autore autore);
}
