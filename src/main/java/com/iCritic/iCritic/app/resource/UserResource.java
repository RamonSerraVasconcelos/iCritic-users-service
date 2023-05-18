package com.iCritic.iCritic.app.resource;

import com.iCritic.iCritic.core.user.User;
import com.iCritic.iCritic.core.user.dto.UserRequestDto;
import com.iCritic.iCritic.core.user.dto.UserResponseDto;
import com.iCritic.iCritic.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponseDto> loadAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        return userService.update(id, userRequestDto);
    }
}
