package com.atif.CompleteProject.mapper;

import com.atif.CompleteProject.dto.UserDTO;
import com.atif.CompleteProject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}
