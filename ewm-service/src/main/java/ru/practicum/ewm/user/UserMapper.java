package ru.practicum.ewm.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.UserInDto;
import ru.practicum.ewm.user.model.dto.UserOutDto;
import ru.practicum.ewm.user.model.dto.UserShortDto;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", expression = "java(null)")
    User mapToUser(UserInDto UserInDto);

    UserOutDto mapToUserOutDto(User user);

    UserShortDto mapToUserShortDto(User user);
}
