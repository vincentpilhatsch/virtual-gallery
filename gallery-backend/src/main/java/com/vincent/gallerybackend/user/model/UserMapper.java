package com.vincent.gallerybackend.user.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userDAOtoDTO(UserEntity userEntity);

    UserEntity userDTOtoDAO(User user);

    @Mapping(target = "id", source = "oldUser.id")
    @Mapping(target = "firstName", source = "newUser.firstName")
    @Mapping(target = "lastName", source = "newUser.lastName")
    @Mapping(target = "email", source = "newUser.email")
    @Mapping(target = "password", source = "newUser.password")
    @Mapping(target = "city", source = "newUser.city")
    @Mapping(target = "phone", source = "newUser.phone")
    @Mapping(target = "language", source = "newUser.language")
    @Mapping(target = "role", source = "newUser.role")
    @Mapping(target = "favoriteArtworkIds", source = "newUser.favoriteArtworkIds")
    @Mapping(target = "acceptedEventIds", source = "newUser.acceptedEventIds")
    @Mapping(target = "artworkRequestIds", source = "newUser.artworkRequestIds")
    UserEntity updateUserDTOtoDAO(UserEntity oldUser, User newUser);

}
