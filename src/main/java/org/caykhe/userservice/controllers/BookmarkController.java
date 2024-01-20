package org.caykhe.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.BookmarkDetailRequest;
import org.caykhe.userservice.models.Bookmark;
import org.caykhe.userservice.models.BookmarkDetail;
import org.caykhe.userservice.services.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    final BookmarkService bookmarkService;
    @PostMapping("/createBookmark/{username}")
    public ResponseEntity<?> createBookmark(@PathVariable String username) {
        return new ResponseEntity<>(bookmarkService.createBookmark(username), HttpStatus.CREATED);
    }

    @PostMapping("/createBookmarkPost/{bookmarkId}")
    public ResponseEntity<?> createBookmarkPost(@PathVariable Integer bookmarkId, @RequestBody BookmarkDetailRequest request) {
        Optional<Bookmark> bookmark = bookmarkService.getBookmarkById(bookmarkId);
        if (bookmark.isPresent()) {
            BookmarkDetail bookmarkDetail = bookmarkService.addBookmarkDetail(bookmark.get(), request.getTargetId(), request.getType());
            return new ResponseEntity<>(bookmarkDetail, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Lỗi Không tìm thấy bookmark cần tạo", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bookmark/{username}")
    public ResponseEntity<?> bookmark(@PathVariable String username, @RequestBody BookmarkDetailRequest request) {
        return new ResponseEntity<>(bookmarkService.bookmark(username, request), HttpStatus.OK);
    }

    @DeleteMapping("/unBookmark")
    public ResponseEntity<?> unBookmark(@RequestParam String username,
                                        @RequestBody BookmarkDetailRequest bookmarkDetailRequest) {
        bookmarkService.unBookmark(username, bookmarkDetailRequest);
        return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
    }

    @PostMapping("/isBookmark")
    public Boolean isBookmark(
            @RequestParam String username,
            @RequestBody BookmarkDetailRequest bookmarkDetailRequest) {
        return bookmarkService.isBookmark(username, bookmarkDetailRequest);
    }
}
