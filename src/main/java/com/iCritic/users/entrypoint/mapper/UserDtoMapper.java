package com.iCritic.users.entrypoint.mapper;

import com.iCritic.users.config.properties.AzureStorageProperties;
import com.iCritic.users.core.model.User;
import com.iCritic.users.entrypoint.entity.CountryResponseDto;
import com.iCritic.users.entrypoint.entity.UserRequestDto;
import com.iCritic.users.entrypoint.entity.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class UserDtoMapper {

    @Autowired
    private AzureStorageProperties azureStorageProperties;

    public User userRequestDtoToUser(UserRequestDto userRequestDto) {
        return User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .description(userRequestDto.getDescription())
                .password(userRequestDto.getPassword())
                .countryId(userRequestDto.getCountryId())
                .build();
    }

    public UserResponseDto userToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .description(user.getDescription())
                .active(user.isActive())
                .role(user.getRole())
                .country(CountryResponseDto.builder().id(user.getCountry().getId()).name(user.getCountry().getName()).build())
                .profilePictureUrl(nonNull(user.getProfilePicture()) ? buildImageUrl(user.getProfilePicture().getName()) : null)
                .createdAt(user.getCreatedAt())
                .build();
    }

    private String buildImageUrl(String imageName) {
        return azureStorageProperties.getContainerHostUrl() + "/" + imageName;
    }
}
