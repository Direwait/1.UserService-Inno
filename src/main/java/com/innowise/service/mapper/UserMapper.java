package com.innowise.service.mapper;

import com.innowise.dao.model.UserModel;
import com.innowise.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface UserMapper {


    @Mapping(target = "cards", source = "cardDtos")
    UserModel dtoToModel(UserDto userDto);

    @Mapping(target = "cardDtos", source = "cards")
    UserDto modelToDto(UserModel userModel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "cards", ignore = true)
    void updateFromDto(UserDto userDto, @MappingTarget UserModel userModel);
}
