package com.phytoncide.hikinglog.domain.bookmarks.controller;

import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.boards.dto.BoardListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.dto.CursorPageRequestDto;
import com.phytoncide.hikinglog.domain.bookmarks.dto.*;
import com.phytoncide.hikinglog.domain.bookmarks.service.BookmarksService;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/bookmarks")
@RequiredArgsConstructor
public class BookmarksController {
    private final BookmarksService bookmarksService;

    @PostMapping("/restaurant/{storeId}")
    public ResponseEntity<ResponseDTO> restaurantBookmarkCreate(
            @PathVariable("storeId") Integer storeId,
            @RequestBody StoreBookmarkCreateDTO storeBookmarkCreateDto,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        String res = bookmarksService.restaurantBookmarkCreate(storeId, storeBookmarkCreateDto, authDetails);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RESTAURANT_BOOKMARK_CREATE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RESTAURANT_BOOKMARK_CREATE, res));
    }

    @PostMapping("/accommodation/{storeId}")
    public ResponseEntity<ResponseDTO> accommodationBookmarkCreate(
            @PathVariable("storeId") Integer storeId,
            @RequestBody StoreBookmarkCreateDTO storeBookmarkCreateDto,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        String res = bookmarksService.accommodationBookmarkCreate(storeId, storeBookmarkCreateDto, authDetails);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_ACCOMMODATION_BOOKMARK_CREATE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ACCOMMODATION_BOOKMARK_CREATE, res));
    }

    @PostMapping("/onlinestore/{storeId}")
    public ResponseEntity<ResponseDTO> onlinestoreBookmarkCreate(
            @PathVariable("storeId") Integer storeId,
            @AuthenticationPrincipal AuthDetails authDetails
    ) throws IOException, ParseException {

        String res = bookmarksService.onlinestoreBookmarkCreate(storeId, authDetails);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_ONLINESTORE_BOOKMARK_CREATE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ONLINESTORE_BOOKMARK_CREATE, res));
    }

    @PostMapping("/mountain/{mountainId}")
    public ResponseEntity<ResponseDTO> mountainBookmarkCreate(
            @PathVariable("mountainId") Integer mountainId,
            @RequestBody MountainBookmarkCreateDTO mountainBookmarkCreateDto,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        String res = bookmarksService.mountainBookmarkCreate(mountainId, mountainBookmarkCreateDto, authDetails);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_MOUNTAIN_BOOKMARK_CREATE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_MOUNTAIN_BOOKMARK_CREATE, res));
    }

    @DeleteMapping("/store/{storeId}")
    public ResponseEntity<ResponseDTO> storeBookmarkDelete(
            @PathVariable("storeId") Integer storeId,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        String res = bookmarksService.storeBookmarkDelete(storeId, authDetails);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_STORE_BOOKMARK_DELETE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_STORE_BOOKMARK_DELETE, res));
    }

    @DeleteMapping("/onlinestore/{storeId}")
    public ResponseEntity<ResponseDTO> onlinestoreBookmarkDelete(
            @PathVariable("storeId") Integer storeId,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        String res = bookmarksService.onlinestoreBookmarkDelete(storeId, authDetails);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_ONLINESTORE_BOOKMARK_DELETE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ONLINESTORE_BOOKMARK_DELETE, res));
    }

    @DeleteMapping("/mountain/{mountainId}")
    public ResponseEntity<ResponseDTO> mountainBookmarkDelete(
            @PathVariable("mountainId") Integer mountainId,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        String res = bookmarksService.mountainBookmarkDelete(mountainId, authDetails);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_MOUNTAIN_BOOKMARK_DELETE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_MOUNTAIN_BOOKMARK_DELETE, res));
    }

    @GetMapping("")
    public ResponseEntity<ResponseDTO> bookmarkReadAll(
            CursorPageRequestDto cursorPageRequestDto,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        Long size = cursorPageRequestDto.getSize();
        Integer page = cursorPageRequestDto.getPage();

        List<BookmarkListResponseDTO.BookmarkResponseDTO> bookmarkList = bookmarksService.readBookmarks(size, page, authDetails);

        BookmarkListResponseDTO res;
        if (!bookmarksService.hasNextBookmarks(size, page, authDetails)) {
            res = BookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(false)
                    .build();
        } else {
            res = BookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(true)
                    .build();
        }

        return ResponseEntity
                .status(ResponseCode.SUCCESS_BOOKMARK_READ_ALL.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_BOOKMARK_READ_ALL, res));
    }

    @GetMapping("/mountain")
    public ResponseEntity<ResponseDTO> bookmarkReadMountain(
            CursorPageRequestDto cursorPageRequestDto,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        Long size = cursorPageRequestDto.getSize();
        Integer page = cursorPageRequestDto.getPage();

        List<MountainBookmarkListResponseDTO.MountainBookmarkResponseDTO> bookmarkList = bookmarksService.readBookmarksMountain(size, page, authDetails);

        MountainBookmarkListResponseDTO res;
        if (!bookmarksService.hasNextBookmarks(size, page, authDetails)) {
            res = MountainBookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(false)
                    .build();
        } else {
            res = MountainBookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(true)
                    .build();
        }

        return ResponseEntity
                .status(ResponseCode.SUCCESS_BOOKMARK_READ_MOUNTAIN.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_BOOKMARK_READ_MOUNTAIN, res));
    }

    @GetMapping("/restaurant")
    public ResponseEntity<ResponseDTO> bookmarkReadRestaurant(
            CursorPageRequestDto cursorPageRequestDto,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        Long size = cursorPageRequestDto.getSize();
        Integer page = cursorPageRequestDto.getPage();

        List<StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO> bookmarkList = bookmarksService.readBookmarksRestaurant(size, page, authDetails);

        StoreBookmarkListResponseDTO res;
        if (!bookmarksService.hasNextBookmarks(size, page, authDetails)) {
            res = StoreBookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(false)
                    .build();
        } else {
            res = StoreBookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(true)
                    .build();
        }

        return ResponseEntity
                .status(ResponseCode.SUCCESS_BOOKMARK_READ_RESTAURANT.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_BOOKMARK_READ_RESTAURANT, res));
    }

    @GetMapping("/accommodation")
    public ResponseEntity<ResponseDTO> bookmarkReadAccommodation(
            CursorPageRequestDto cursorPageRequestDto,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        Long size = cursorPageRequestDto.getSize();
        Integer page = cursorPageRequestDto.getPage();

        List<StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO> bookmarkList = bookmarksService.readBookmarksAccommodation(size, page, authDetails);

        StoreBookmarkListResponseDTO res;
        if (!bookmarksService.hasNextBookmarks(size, page, authDetails)) {
            res = StoreBookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(false)
                    .build();
        } else {
            res = StoreBookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(true)
                    .build();
        }

        return ResponseEntity
                .status(ResponseCode.SUCCESS_BOOKMARK_READ_ACCOMMODATION.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_BOOKMARK_READ_ACCOMMODATION, res));
    }

    @GetMapping("/onlinestore")
    public ResponseEntity<ResponseDTO> bookmarkReadOnlinestore(
            CursorPageRequestDto cursorPageRequestDto,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        Long size = cursorPageRequestDto.getSize();
        Integer page = cursorPageRequestDto.getPage();

        List<OnlinestoreBookmarkListResponseDTO.OnlinestoreBookmarkResponseDTO> bookmarkList = bookmarksService.readBookmarksOnlinestore(size, page, authDetails);

        OnlinestoreBookmarkListResponseDTO res;
        if (!bookmarksService.hasNextBookmarks(size, page, authDetails)) {
            res = OnlinestoreBookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(false)
                    .build();
        } else {
            res = OnlinestoreBookmarkListResponseDTO.builder()
                    .bookmarkList(bookmarkList)
                    .hasNext(true)
                    .build();
        }

        return ResponseEntity
                .status(ResponseCode.SUCCESS_BOOKMARK_READ_ONLINESTORE.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_BOOKMARK_READ_ONLINESTORE, res));
    }
}
