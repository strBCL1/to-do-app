package com.mycompany.todoapp.service.mapper;

import com.mycompany.todoapp.domain.ApplicationUser;
import com.mycompany.todoapp.domain.ToDoItem;
import com.mycompany.todoapp.service.dto.ApplicationUserDTO;
import com.mycompany.todoapp.service.dto.ToDoItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToDoItem} and its DTO {@link ToDoItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ToDoItemMapper extends EntityMapper<ToDoItemDTO, ToDoItem> {
    @Mapping(target = "applicationUser", source = "applicationUser", qualifiedByName = "applicationUserId")
    ToDoItemDTO toDto(ToDoItem s);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);
}
