package com.mycompany.todoapp.service.mapper;

import com.mycompany.todoapp.domain.ApplicationUser;
import com.mycompany.todoapp.domain.User;
import com.mycompany.todoapp.service.dto.ApplicationUserDTO;
import com.mycompany.todoapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ApplicationUserDTO toDto(ApplicationUser s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
