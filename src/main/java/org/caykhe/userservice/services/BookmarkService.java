package org.caykhe.userservice.services;

import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.ApiException;
import org.caykhe.userservice.dtos.BookmarkDetailRequest;
import org.caykhe.userservice.models.Bookmark;
import org.caykhe.userservice.models.BookmarkDetail;
import org.caykhe.userservice.models.BookmarkDetailId;
import org.caykhe.userservice.models.User;
import org.caykhe.userservice.repositories.BookmarkDetailRepository;
import org.caykhe.userservice.repositories.BookmarkRepository;
import org.caykhe.userservice.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkDetailRepository bookmarkDetailRepository;
    private final UserRepository userRepository;


    public Bookmark createBookmark(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Bookmark bookmark = new Bookmark();
        user.ifPresent(bookmark::setUsername);
        return bookmarkRepository.save(bookmark);
    }

    public BookmarkDetail addBookmarkDetail(Bookmark bookmark, Integer targetId, Boolean type) {
        BookmarkDetailId id = new BookmarkDetailId();
        id.setBookmarkId(bookmark.getId());
        id.setTargetId(targetId);
        id.setType(type);

        BookmarkDetail bookmarkPost = new BookmarkDetail();
        bookmarkPost.setId(id);
        bookmarkPost.setBookmark(bookmark);
        bookmarkPost.setTargetId(targetId);
        bookmarkPost.setType(type);
        return bookmarkDetailRepository.save(bookmarkPost);

    }
    
    public BookmarkDetail bookmark(String username, BookmarkDetailRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByUsername(userOptional.get());
            Bookmark bookmark;
            bookmark = bookmarkOptional.orElseGet(() -> createBookmark(username));
            return addBookmarkDetail(bookmark, request.getTargetId(), request.getType());
        } else {
            throw new ApiException("User not found with username: " + username, HttpStatus.NOT_FOUND);
        }
    }

    public Optional<Bookmark> getBookmarkById(Integer id) {
        return bookmarkRepository.findById(id);
    }

    public void unBookmark(String username, BookmarkDetailRequest bookmarkPostRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found with username: " + username, HttpStatus.NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUsername(user)
                .orElseThrow(() -> new ApiException("Bookmark not found for user: " + username, HttpStatus.NOT_FOUND));

        Integer targetId = bookmarkPostRequest.getTargetId();
        Boolean type = bookmarkPostRequest.getType();

        BookmarkDetail bookmarkDetail = bookmarkDetailRepository.findByTargetIdAndAndType(targetId, type)
                .orElseThrow(() -> new ApiException("BookmarkDetail not found for targetId: " + targetId + " and type: " + type, HttpStatus.NOT_FOUND));

        bookmarkDetailRepository.delete(bookmarkDetail);

        if (bookmarkDetailRepository.findByBookmark(bookmark).isEmpty()) {
            bookmarkRepository.delete(bookmark);
        }
    }

    public Boolean isBookmark(String username, BookmarkDetailRequest bookmarkPostRequest) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Optional<Bookmark> bookmark = bookmarkRepository.findByUsername(user.get());
            if (bookmark.isEmpty()) {
                return false;
            } else {
                Integer targetId = bookmarkPostRequest.getTargetId();
                Boolean type = bookmarkPostRequest.getType();

                Optional<BookmarkDetail> bookmarkPost = bookmarkDetailRepository.findByTargetIdAndAndType(targetId, type);
                return bookmarkPost.isPresent();
            }
        } else {
            return false;
        }
    }

}
