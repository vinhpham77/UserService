package org.caykhe.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.ResultCount;
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
}
