package com.iCritic.iCritic.app.resource;

import com.iCritic.iCritic.core.enums.BanActionEnum;
import com.iCritic.iCritic.core.user.dto.UserBanDto;
import com.iCritic.iCritic.core.user.dto.UserRequestDto;
import com.iCritic.iCritic.core.user.dto.UserResponseDto;
import com.iCritic.iCritic.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> changeRole(@PathVariable Long id, @RequestBody UserRequestDto userDto) {
        userService.changeRole(id, userDto.getRole());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/ban")
    public ResponseEntity<Void> ban(@PathVariable Long id, @RequestBody UserBanDto banDto) {
        userService.changeStatus(id, banDto, BanActionEnum.BAN);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unban")
    public ResponseEntity<Void> unban(@PathVariable Long id, @RequestBody UserBanDto banDto) {
        userService.changeStatus(id, banDto, BanActionEnum.UNBAN);

        return ResponseEntity.ok().build();
    }
}
