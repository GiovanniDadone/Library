package com.giodad.todolist.service.mapper;

import com.giodad.todolist.domain.Libro;
import com.giodad.todolist.domain.Recensione;
import com.giodad.todolist.domain.User;
import com.giodad.todolist.service.dto.LibroDTO;
import com.giodad.todolist.service.dto.RecensioneDTO;
import com.giodad.todolist.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recensione} and its DTO {@link RecensioneDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecensioneMapper extends EntityMapper<RecensioneDTO, Recensione> {
    @Mapping(target = "libro", source = "libro", qualifiedByName = "libroId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    RecensioneDTO toDto(Recensione s);

    @Named("libroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LibroDTO toDtoLibroId(Libro libro);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
