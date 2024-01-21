package org.caykhe.userservice.services;

import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.ApiException;
import org.caykhe.userservice.dtos.ResultCount;
import org.caykhe.userservice.dtos.UserDto;
import org.caykhe.userservice.dtos.UserStats;
import org.caykhe.userservice.models.Follow;
import org.caykhe.userservice.models.User;
import org.caykhe.userservice.repositories.FollowRepository;
import org.caykhe.userservice.repositories.UserRepository;
import org.caykhe.userservice.utils.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public ResultCount<UserStats> getFollowings(String follower, Integer page, Integer size) {
        User followerUser = getUserByUsername(follower);
        Pageable pageable = PaginationUtils.getPageable(page - 1, size, "followed");
        Page<Follow> followersPage = followRepository.findAllByFollower(followerUser, pageable);

        return countAndAddStates(followersPage, true);
    }

    public ResultCount<UserStats> getFollowers(String followed, Integer page, Integer size) {
        User followedUser = getUserByUsername(followed);
        Pageable pageable = PaginationUtils.getPageable(page - 1, size, "follower");
        Page<Follow> followedsPage = followRepository.findAllByFollowed(followedUser, pageable);

        return countAndAddStates(followedsPage, false);
    }

    private ResultCount<UserStats> countAndAddStates(Page<Follow> followePage, boolean isFollowing) {
        long count = followePage.getTotalElements();
        Stream<User> users;

        if (isFollowing) {
            users = followePage.stream().map(Follow::getFollowed);
        } else {
            users = followePage.stream().map(Follow::getFollower);
        }

        List<UserStats> userStats = users.map(this::addStats).toList();
        
        return new ResultCount<>(userStats, count);
    }

    public List<User> getUsersByUsernames(List<String> usernames) {
        List<String> distinctUsernames = usernames.stream().distinct().toList();

        return userRepository.findAllByUsernameIn(distinctUsernames);
    }

    public User update(String username, UserDto userDto) {
        var requester = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!requester.equals(username)) {
            throw new ApiException("Bạn không có quyền cập nhật thông tin người dùng khác", HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApiException("Không tìm thấy người dùng @" + username, HttpStatus.NOT_FOUND));
        Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());

        if (userByEmail.isPresent() && !userByEmail.get().getUsername().equals(username)) {
            throw new ApiException("Email đã được sử dụng", HttpStatus.BAD_REQUEST);
        }

        user.setEmail(userDto.getEmail().trim());
        user.setGender(userDto.getGender());
        user.setBirthdate(userDto.getBirthdate());
        user.setBio(getUserProperty(userDto.getBio()));
        user.setDisplayName(getUserProperty(userDto.getDisplayName()));
        user.setAvatarUrl(getUserProperty(userDto.getAvatarUrl()));

        return userRepository.save(user);
    }

    public UserStats addStats(User user) {
        int followingCount = followRepository.countByFollower(user);
        int followerCount = followRepository.countByFollowed(user);

        return UserStats.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .role(user.getRole())
                .followingCount(followingCount)
                .followerCount(followerCount)
                .build();
    }

    String getUserProperty(String raw) {
        if (raw == null) {
            return null;
        }

        String trimmed = raw.trim();

        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed;
    }
}
