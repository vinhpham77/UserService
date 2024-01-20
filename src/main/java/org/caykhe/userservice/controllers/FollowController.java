package org.caykhe.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.services.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {
    private final FollowService followService;

    @GetMapping("/is-following/{followed}")
    public ResponseEntity<?> isFollowing(@PathVariable String followed) {
        boolean isFollowing = followService.getFollow(followed).isPresent();
        return new ResponseEntity<>(isFollowing, HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity<?> follow(@RequestBody String followed) {
        return new ResponseEntity<>(followService.follow(followed), HttpStatus.OK);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollow(@RequestBody String followed) {
        followService.unfollow(followed);
        return new ResponseEntity<>("Bỏ theo dõi thành công", HttpStatus.OK);
    }

    @GetMapping("/totalFollower/{followed}")
    public ResponseEntity<?> getTotalPost(@PathVariable String followed) {
        return new ResponseEntity<>(followService.countFollowerBy(followed), HttpStatus.OK);
    }

    @GetMapping("/get/followed")
    public ResponseEntity<?> getFollowed() {
        return new ResponseEntity<>(followService.getFollowed(), HttpStatus.OK);
    }

    @GetMapping("/stats/{username}")
    public ResponseEntity<?> getStats(@PathVariable String username) {
        return new ResponseEntity<>(followService.getStats(username), HttpStatus.OK);
    }
}
