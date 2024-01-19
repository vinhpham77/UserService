package org.caykhe.userservice.services;

import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.ApiException;
import org.caykhe.userservice.dtos.ResultCount;
import org.caykhe.userservice.models.Follow;
import org.caykhe.userservice.models.User;
import org.caykhe.userservice.repositories.FollowRepository;
import org.caykhe.userservice.repositories.UserRepository;
import org.caykhe.userservice.utils.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public UserDetailsService userDetailsService() {
        return username -> userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng @" + username));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng @" + username, HttpStatus.NOT_FOUND));
    }

    public Optional<List<User>> getAllUser() {
        return Optional.of(userRepository.findAll());
    }

    public ResultCount<User> getFollowings(String follower, Integer page, Integer size) {
        User followerUser = getUserByUsername(follower);
        Pageable pageable = PaginationUtils.getPageable(page - 1, size, "followed");
        Page<Follow> followersPage = followRepository.findAllByFollower(followerUser, pageable);

        return countAndAddStates(followersPage, true);
    }

    public ResultCount<User> getFollowers(String followed, Integer page, Integer size) {
        User followedUser = getUserByUsername(followed);
        Pageable pageable = PaginationUtils.getPageable(page - 1, size, "follower");
        Page<Follow> followedsPage = followRepository.findAllByFollowed(followedUser, pageable);

        return countAndAddStates(followedsPage, false);
    }

    private ResultCount<User> countAndAddStates(Page<Follow> followePage, boolean isFollowing) {
        long count = followePage.getTotalElements();
        Stream<User> users;

        if (isFollowing) {
            users = followePage.stream().map(Follow::getFollowed);
        } else {
            users = followePage.stream().map(Follow::getFollower);
        }

        return new ResultCount<>(users.toList(), count);
    }

    public List<User> getUsersByUsernames(List<String> usernames) {
        List<String> distinctUsernames = usernames.stream().distinct().toList();
        
        return userRepository.findAllByUsernameIn(distinctUsernames);
    }
}
