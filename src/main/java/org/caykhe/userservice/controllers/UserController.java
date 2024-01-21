package org.caykhe.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.*;
import org.caykhe.userservice.models.User;
import org.caykhe.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    final private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        Optional<List<User>> userListOptional = userService.getAllUser();

        if (userListOptional.isPresent()) {
            List<User> userList = userListOptional.get();
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
    }

    @PostMapping("/list")
    public ResponseEntity<List<User>> getUsersByIds(@RequestBody List<String> usernames) {
        List<User> users = userService.getUsersByUsernames(usernames);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{follower}/followings")
    public ResponseEntity<?> getFollowings(@PathVariable String follower, Integer page, Integer size) {
        ResultCount<User> followings = userService.getFollowings(follower, page, size);
        return new ResponseEntity<>(followings, HttpStatus.OK);
    }

    @GetMapping("/{followed}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable String followed, Integer page, Integer size) {
        ResultCount<User> followers = userService.getFollowers(followed, page, size);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> update(@PathVariable String username, @Valid @RequestBody UserDto newUser) {
        if (newUser == null) {
            throw new ApiException("Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        User user = userService.update(username, newUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        User success = userService.changePassword(changePasswordRequest);
        if (success != null) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ApiException("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody UserNameRequest userNameRequest) {

        Optional<User> userOptional = userService.getByUsername(userNameRequest.username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User không tồn tại", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody RequestRessetPass requestRessetPass) {
        userService.resetPass(requestRessetPass);
        return ResponseEntity.noContent().build();
    }

}
