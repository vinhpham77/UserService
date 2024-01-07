package org.caykhe.userservice.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.ApiException;
import org.caykhe.userservice.models.Follow;
import org.caykhe.userservice.models.User;
import org.caykhe.userservice.repositories.FollowRepository;
import org.caykhe.userservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    final FollowRepository followRepository;
    final UserService userService;
    final EntityManager entityManager;
    final UserRepository userRepository;

    public Optional<Follow> getFollow(String followed) {
        User followerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User followedUser = userService.getUserByUsername(followed);

        return followRepository.findByFollowerAndFollowed(followerUser, followedUser);
    }

    @Transactional
    public Follow follow(String followed) {
        User follower = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (follower.getUsername().equals(followed)) {
            throw new ApiException("Không thể theo dõi chính mình", HttpStatus.BAD_REQUEST);
        } else if (getFollow(followed).isPresent()) {
            throw new ApiException("Đã theo dõi trước đó", HttpStatus.BAD_REQUEST);
        }

        User managedFollower = entityManager.merge(follower);
        var followedUser = userService.getUserByUsername(followed);
        User managedFollowed = entityManager.merge(followedUser);


        Follow follow = Follow.builder()
                .follower(managedFollower)
                .followed(managedFollowed)
                .build();
        System.out.println(follow.toString());
        try {
            return followRepository.save(follow);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ApiException("Có lỗi xảy ra khi theo dõi. Vui lòng thử lại!", HttpStatus.BAD_REQUEST);
        }
    }

    public void unfollow(String followed) {
        var follow = getFollow(followed);

        if (follow.isEmpty()) {
            throw new ApiException("Chưa theo dõi", HttpStatus.BAD_REQUEST);
        }

        followRepository.delete(follow.get());
    }

    public List<String> getFollowedByFollower() {
        User followerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Follow> follows = followRepository.findByFollower(followerUser)
                .orElseThrow(() -> new ApiException("Tài khoản không tồn tại", HttpStatus.NOT_FOUND));
        return follows.stream().map(follow -> follow.getFollowed().getUsername()).collect(Collectors.toList());
    }

    public int countFollowerBy(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {

            List<Follow> follows = followRepository.findByFollowed(user.get());
            return follows.size();
        } else {
            throw new ApiException("User không tồn tại", HttpStatus.NOT_FOUND);
        }
    }
}
