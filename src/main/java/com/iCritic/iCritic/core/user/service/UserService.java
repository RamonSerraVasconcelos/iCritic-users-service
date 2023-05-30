package com.iCritic.iCritic.core.user.service;

import com.iCritic.iCritic.core.country.Country;
import com.iCritic.iCritic.core.country.repository.CountryRepository;
import com.iCritic.iCritic.core.enums.BanActionEnum;
import com.iCritic.iCritic.core.enums.Role;
import com.iCritic.iCritic.core.user.User;
import com.iCritic.iCritic.core.user.dto.UserBanDto;
import com.iCritic.iCritic.core.user.dto.UserLoginDto;
import com.iCritic.iCritic.core.user.dto.UserRequestDto;
import com.iCritic.iCritic.core.user.dto.UserResponseDto;
import com.iCritic.iCritic.core.user.mapper.UserMapper;
import com.iCritic.iCritic.core.user.repository.UserRepository;
import com.iCritic.iCritic.exception.ResourceConflictException;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
import com.iCritic.iCritic.exception.ResourceViolationException;
import com.iCritic.iCritic.infrastructure.database.UpdateUserBanListRepository;
import com.iCritic.iCritic.infrastructure.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Autowired
    private Validator validator;

    @Autowired
    private UpdateUserBanListRepository updateUserBanlistRepository;

    @Autowired
    private JwtGenerator jwtGenerator;

    public UserResponseDto save(@Valid UserRequestDto userRequestDto) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        User user = UserMapper.INSTANCE.userRequestDtoToUser(userRequestDto);

        boolean isUserDuplicated = userRepository.existsByEmail(user.getEmail());

        if(isUserDuplicated) {
            throw new ResourceConflictException("User email already exists");
        }

        Country country = countryRepository.findById(user.getCountryId()).orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        user.setCountry(country);

        String encodedPassword = bcrypt.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User createdUser = userRepository.save(user);

        return UserMapper.INSTANCE.userToUserResponseDto(createdUser);
    }

    public UserResponseDto update(String userTokenId, Long id, @Valid UserRequestDto userRequestDto) {
        if(!Objects.equals(userTokenId, id.toString())) {
            throw new ResourceConflictException("User id does not match");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> {
                if (!violation.getPropertyPath().toString().equals("email") && !violation.getPropertyPath().toString().equals("password")) {
                    throw new ResourceViolationException(violation);
                }
            });
        }

        Country country = countryRepository.findById(userRequestDto.getCountryId()).orElseThrow(() -> new ResourceNotFoundException("Country not found"));

        User userToUpdate = User.builder()
                .id(id)
                .name(userRequestDto.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .description(userRequestDto.getDescription())
                .country(country)
                .active(user.isActive())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();

        User updatedUser = userRepository.save(userToUpdate);

        return UserMapper.INSTANCE.userToUserResponseDto(updatedUser);
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::userToUserResponseDto).collect(Collectors.toList());
    }

    public UserResponseDto get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserMapper.INSTANCE.userToUserResponseDto(user);
    }

    public void changeRole(Long id, String role) {
        if(id == null) {
            throw new ResourceViolationException("Invalid id");
        }

        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        try {
            userRepository.updateRole(id, Role.valueOf(role));
        } catch (IllegalArgumentException ex) {
            throw new ResourceViolationException("Invalid role");
        }
    }

    public void changeStatus(Long id, UserBanDto banDto, BanActionEnum action) {
        if(id == null) {
            throw new ResourceViolationException("Invalid id");
        }

        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<ConstraintViolation<UserBanDto>> violations = validator.validate(banDto);
        if (!violations.isEmpty()) {
            throw new ResourceViolationException(violations);
        }

        boolean updateAction = action != BanActionEnum.BAN;

        userRepository.updateStatus(id, updateAction);
        updateUserBanlistRepository.updateBanList(id, banDto.getMotive(), action);
    }

    public UserLoginDto login(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail());

        if(!nonNull(user)) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        boolean isPasswordValid = bcrypt.matches(userRequestDto.getPassword(), user.getPassword());

        if(!isPasswordValid) {
            throw new ResourceViolationException("Invalid email or password");
        }

        String accessToken = jwtGenerator.generateToken(user);

        return UserLoginDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
