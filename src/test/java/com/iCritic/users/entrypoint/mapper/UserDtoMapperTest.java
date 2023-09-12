package com.iCritic.users.entrypoint.mapper;

import com.iCritic.users.config.properties.AzureStorageProperties;
import com.iCritic.users.core.fixture.UserFixture;
import com.iCritic.users.core.model.User;
import com.iCritic.users.entrypoint.fixture.UserRequestDtoFixture;
import com.iCritic.users.entrypoint.entity.UserRequestDto;
import com.iCritic.users.entrypoint.entity.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDtoMapperTest {

    @InjectMocks
    private UserDtoMapper userDtoMapper;

    @Mock
    private AzureStorageProperties azureStorageProperties;

    @Test
    void givenUserRequestDto_convertUserToUser_thenReturnUser() {
        UserRequestDto userRequestDto = UserRequestDtoFixture.load();

        User user = userDtoMapper.userRequestDtoToUser(userRequestDto);

        assertNotNull(user);
        assertEquals(user.getName(), userRequestDto.getName());
        assertEquals(user.getEmail(), userRequestDto.getEmail());
        assertEquals(user.getPassword(), userRequestDto.getPassword());
        assertEquals(user.getDescription(), userRequestDto.getDescription());
        assertEquals(user.getCountryId(), userRequestDto.getCountryId());
    }

    @Test
    void givenUser_convertUserToUserResponseDto_thenReturnUserResponseDto() {
        User user = UserFixture.load();

        String containeHostUrl = "http://testhost.com";

        when(azureStorageProperties.getContainerHostUrl()).thenReturn(containeHostUrl);

        UserResponseDto userResponseDto = userDtoMapper.userToUserResponseDto(user);

        assertNotNull(userResponseDto);
        assertEquals(userResponseDto.getId(), user.getId());
        assertEquals(userResponseDto.getName(), user.getName());
        assertEquals(userResponseDto.getEmail(), user.getEmail());
        assertEquals(userResponseDto.getDescription(), user.getDescription());
        assertEquals(userResponseDto.isActive(), user.isActive());
        assertEquals(userResponseDto.getRole(), user.getRole());
        assertEquals(userResponseDto.getCountry().getId(), user.getCountry().getId());
        assertEquals(userResponseDto.getCountry().getName(), user.getCountry().getName());
        assertEquals(userResponseDto.getProfilePictureUrl(), containeHostUrl + "/" + user.getProfilePicture().getName());
    }
}
