package com.giodad.todolist.service.mapper;

import com.giodad.todolist.domain.Recensione;
import com.giodad.todolist.service.dto.RecensioneDTO;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    uses = { LibroMapper.class, UserMapper.class },
    nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN
)
public interface RecensioneMapper extends EntityMapper<RecensioneDTO, Recensione> {
    @Mapping(target = "libro", source = "libro", qualifiedByName = "libroComplete")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    RecensioneDTO toDto(Recensione s);

    Recensione toEntity(RecensioneDTO recensioneDTO);

    @Override
    void partialUpdate(@MappingTarget Recensione entity, RecensioneDTO dto);
}
