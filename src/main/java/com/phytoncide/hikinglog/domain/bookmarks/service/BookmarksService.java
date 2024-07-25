package com.phytoncide.hikinglog.domain.bookmarks.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.*;
import com.phytoncide.hikinglog.domain.bookmarks.dto.*;
import com.phytoncide.hikinglog.domain.bookmarks.entity.BookmarkEntity;
import com.phytoncide.hikinglog.domain.bookmarks.repository.BookmarkRepository;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import com.phytoncide.hikinglog.domain.mountain.entity.MountainEntity;
import com.phytoncide.hikinglog.domain.mountain.repository.MountainRepository;
import com.phytoncide.hikinglog.domain.store.entity.OnlineOutdoorMallEntity;
import com.phytoncide.hikinglog.domain.store.entity.StoreEntity;
import com.phytoncide.hikinglog.domain.store.repository.OnlineOutdoorMallRepository;
import com.phytoncide.hikinglog.domain.store.repository.StoreRepository;
import com.phytoncide.hikinglog.global.enums.BookmarkType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarksService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final OnlineOutdoorMallRepository onlineOutdoorMallRepository;
    private final MountainRepository mountainRepository;
    private final BookmarkRepository bookmarkRepository;

    public String restaurantBookmarkCreate(Integer storeId, StoreBookmarkCreateDTO storeBookmarkCreateDto, AuthDetails authDetails) {
        if (storeBookmarkCreateDto.getName().isEmpty()) {
            throw new BookmarkStoreNameException(ErrorCode.BOOKMARK_STORE_NAME_IS_EMPTY);
        }
        if (storeBookmarkCreateDto.getLocation().isEmpty()) {
            throw new BookmarkStoreLocationException(ErrorCode.BOOKMARK_STORE_LOCATION_IS_EMPTY);
        }

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        StoreEntity storeEntity = storeRepository.findByContentId(storeId);

        if (storeEntity == null) {
            storeEntity = storeBookmarkCreateDto.toStoreEntity(storeId);
            storeRepository.save(storeEntity);
        }

        if (bookmarkRepository.findByStoreEntity_SidAndMemberEntity_Uid(storeEntity.getSid(), memberEntity.getUid()) != null) {
            throw new BookmarkExistsException(ErrorCode.BOOKMARK_EXISTS);
        }

        BookmarkEntity bookmarkEntity = storeBookmarkCreateDto.toBookmarkEntity(memberEntity, storeEntity, BookmarkType.RESTAURANT);
        bookmarkRepository.save(bookmarkEntity);

        return "음식점 북마크 추가에 성공했습니다.";
    }

    public String accommodationBookmarkCreate(Integer storeId, StoreBookmarkCreateDTO storeBookmarkCreateDto, AuthDetails authDetails) {
        if (storeBookmarkCreateDto.getName().isEmpty()) {
            throw new BookmarkStoreNameException(ErrorCode.BOOKMARK_STORE_NAME_IS_EMPTY);
        }
        if (storeBookmarkCreateDto.getLocation().isEmpty()) {
            throw new BookmarkStoreLocationException(ErrorCode.BOOKMARK_STORE_LOCATION_IS_EMPTY);
        }

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        StoreEntity storeEntity = storeRepository.findByContentId(storeId);

        if (storeEntity == null) {
            storeEntity = storeBookmarkCreateDto.toStoreEntity(storeId);
            storeRepository.save(storeEntity);
        }

        if (bookmarkRepository.findByStoreEntity_SidAndMemberEntity_Uid(storeEntity.getSid(), memberEntity.getUid()) != null) {
            throw new BookmarkExistsException(ErrorCode.BOOKMARK_EXISTS);
        }

        BookmarkEntity bookmarkEntity = storeBookmarkCreateDto.toBookmarkEntity(memberEntity, storeEntity, BookmarkType.ACCOMMODATION);
        bookmarkRepository.save(bookmarkEntity);

        return "숙박시설 북마크 추가에 성공했습니다.";
    }

    public String onlinestoreBookmarkCreate(Integer storeId, OnlinestoreBookmarkCreateDTO onlinestoreBookmarkCreateDto, AuthDetails authDetails) {
        if (onlinestoreBookmarkCreateDto.getName().isEmpty()) {
            throw new BookmarkOnlineMallNameException(ErrorCode.BOOKMARK_ONLINEMALL_NAME_IS_EMPTY);
        }
        if (onlinestoreBookmarkCreateDto.getLink().isEmpty()) {
            throw new BookmarkOnlineMallLinkException(ErrorCode.BOOKMARK_ONLINEMALL_LINK_IS_EMPTY);
        }

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        OnlineOutdoorMallEntity onlineOutdoorMallEntity = onlineOutdoorMallRepository.findByStoreId(storeId);

        if (onlineOutdoorMallEntity == null) {
            onlineOutdoorMallEntity = onlinestoreBookmarkCreateDto.toOnlinestoreMallEntity(storeId);
            onlineOutdoorMallRepository.save(onlineOutdoorMallEntity);
        }

        if (bookmarkRepository.findByOnlineOutdoorMallEntity_OidAndMemberEntity_Uid(onlineOutdoorMallEntity.getOid(), memberEntity.getUid()) != null) {
            throw new BookmarkExistsException(ErrorCode.BOOKMARK_EXISTS);
        }

        BookmarkEntity bookmarkEntity = onlinestoreBookmarkCreateDto.toBookmarkEntity(memberEntity, onlineOutdoorMallEntity, BookmarkType.ONLINEOUTDOORMALL);
        bookmarkRepository.save(bookmarkEntity);

        return "등산용품 가게 북마크 추가에 성공했습니다.";
    }

    public String mountainBookmarkCreate(Integer mountainId, MountainBookmarkCreateDTO mountainBookmarkCreateDto, AuthDetails authDetails) {
        if (mountainBookmarkCreateDto.getName().isEmpty()) {
            throw new BookmarkMountainNameException(ErrorCode.BOOKMARK_MOUNTAIN_NAME_IS_EMPTY);
        }
        if (mountainBookmarkCreateDto.getLocation().isEmpty()) {
            throw new BookmarkMountainLocationException(ErrorCode.BOOKMARK_MOUNTAIN_LOCATION_IS_EMPTY);
        }

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        MountainEntity mountainEntity = mountainRepository.findByMntilistno(mountainId);

        if (mountainEntity == null) {
            mountainEntity = mountainBookmarkCreateDto.toMountainEntity(mountainId);
            mountainRepository.save(mountainEntity);
        }

        if (bookmarkRepository.findByMountainEntity_MidAndMemberEntity_Uid(mountainEntity.getMid(), memberEntity.getUid()) != null) {
            throw new BookmarkExistsException(ErrorCode.BOOKMARK_EXISTS);
        }

        BookmarkEntity bookmarkEntity = mountainBookmarkCreateDto.toBookmarkEntity(memberEntity, mountainEntity, BookmarkType.MOUNTAIN);
        bookmarkRepository.save(bookmarkEntity);

        return "산 북마크 추가에 성공했습니다.";
    }

    public String storeBookmarkDelete(Integer storeId, AuthDetails authDetails) {
        StoreEntity storeEntity = storeRepository.findByContentId(storeId);
        if (storeEntity == null) {
            throw new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND);
        }

        Integer userId = authDetails.getMemberEntity().getUid();

        Integer sId = storeEntity.getSid();

        BookmarkEntity bookmarkEntity = bookmarkRepository.findByStoreEntity_SidAndMemberEntity_Uid(sId, userId);

        if (bookmarkEntity == null) {
            throw new BookmarkNotFoundException(ErrorCode.BOOKMARK_NOT_FOUND);
        }

        bookmarkRepository.delete(bookmarkEntity);

        return "가게 북마크 삭제에 성공했습니다.";
    }

    public String onlinestoreBookmarkDelete(Integer storeId, AuthDetails authDetails) {
        OnlineOutdoorMallEntity storeEntity = onlineOutdoorMallRepository.findByStoreId(storeId);
        if (storeEntity == null) {
            throw new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND);
        }

        Integer userId = authDetails.getMemberEntity().getUid();

        Integer oId = storeEntity.getOid();

        BookmarkEntity bookmarkEntity = bookmarkRepository.findByOnlineOutdoorMallEntity_OidAndMemberEntity_Uid(oId, userId);

        if (bookmarkEntity == null) {
            throw new BookmarkNotFoundException(ErrorCode.BOOKMARK_NOT_FOUND);
        }

        bookmarkRepository.delete(bookmarkEntity);

        return "등산용품 가게 북마크 삭제에 성공했습니다.";
    }

    public String mountainBookmarkDelete(Integer mountainId, AuthDetails authDetails) {
        MountainEntity mountainEntity = mountainRepository.findByMntilistno(mountainId);
        if (mountainEntity == null) {
            throw new MountainNotFoundException(ErrorCode.MOUNTAIN_NOT_FOUND);
        }

        Integer userId = authDetails.getMemberEntity().getUid();

        Integer mId = mountainEntity.getMid();

        BookmarkEntity bookmarkEntity = bookmarkRepository.findByMountainEntity_MidAndMemberEntity_Uid(mId, userId);

        if (bookmarkEntity == null) {
            throw new BookmarkNotFoundException(ErrorCode.BOOKMARK_NOT_FOUND);
        }

        bookmarkRepository.delete(bookmarkEntity);

        return "산 북마크 삭제에 성공했습니다.";
    }

    public List<BookmarkListResponseDTO.BookmarkResponseDTO> readBookmarks(Long size, Integer pageNumber, AuthDetails authDetails) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        List<BookmarkEntity> bookmarkEntities = bookmarkRepository.findNextPage(2147483647, pageable, memberEntity);

        List<BookmarkListResponseDTO.BookmarkResponseDTO> bookmarks = new ArrayList<>();

        for (BookmarkEntity bookmarkEntity : bookmarkEntities) {
            if (bookmarkEntity.getBookmarkType().equals(BookmarkType.MOUNTAIN)) {
                MountainEntity mountainEntity = bookmarkEntity.getMountainEntity();
                bookmarks.add(BookmarkListResponseDTO.BookmarkResponseDTO.mountainToDTO(bookmarkEntity, mountainEntity));
            } else if (bookmarkEntity.getBookmarkType().equals(BookmarkType.RESTAURANT) || bookmarkEntity.getBookmarkType().equals(BookmarkType.ACCOMMODATION)) {
                StoreEntity storeEntity = bookmarkEntity.getStoreEntity();
                bookmarks.add(BookmarkListResponseDTO.BookmarkResponseDTO.storeToDTO(bookmarkEntity, storeEntity));
            } else if (bookmarkEntity.getBookmarkType().equals(BookmarkType.ONLINEOUTDOORMALL)) {
                OnlineOutdoorMallEntity onlineOutdoorMallEntity = bookmarkEntity.getOnlineOutdoorMallEntity();
                bookmarks.add(BookmarkListResponseDTO.BookmarkResponseDTO.onlineOutdoorMallToDTO(bookmarkEntity, onlineOutdoorMallEntity));
            }
        }

        return bookmarks;
    }

    public List<MountainBookmarkListResponseDTO.MountainBookmarkResponseDTO> readBookmarksMountain(Long size, Integer pageNumber, AuthDetails authDetails) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        List<BookmarkEntity> bookmarkEntities = bookmarkRepository.findNextPageType(2147483647, pageable, memberEntity, BookmarkType.MOUNTAIN);

        List<MountainBookmarkListResponseDTO.MountainBookmarkResponseDTO> bookmarks = new ArrayList<>();

        for (BookmarkEntity bookmarkEntity : bookmarkEntities) {
            MountainEntity mountainEntity = bookmarkEntity.getMountainEntity();
            bookmarks.add(MountainBookmarkListResponseDTO.MountainBookmarkResponseDTO.toDTO(bookmarkEntity, mountainEntity));
        }

        return bookmarks;
    }

    public List<StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO> readBookmarksRestaurant(Long size, Integer pageNumber, AuthDetails authDetails) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        List<BookmarkEntity> bookmarkEntities = bookmarkRepository.findNextPageType(2147483647, pageable, memberEntity, BookmarkType.RESTAURANT);

        List<StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO> bookmarks = new ArrayList<>();

        for (BookmarkEntity bookmarkEntity : bookmarkEntities) {
            StoreEntity storeEntity = bookmarkEntity.getStoreEntity();
            bookmarks.add(StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO.toDTO(bookmarkEntity, storeEntity));
        }

        return bookmarks;
    }

    public List<StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO> readBookmarksAccommodation(Long size, Integer pageNumber, AuthDetails authDetails) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        List<BookmarkEntity> bookmarkEntities = bookmarkRepository.findNextPageType(2147483647, pageable, memberEntity, BookmarkType.ACCOMMODATION);

        List<StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO> bookmarks = new ArrayList<>();

        for (BookmarkEntity bookmarkEntity : bookmarkEntities) {
            StoreEntity storeEntity = bookmarkEntity.getStoreEntity();
            bookmarks.add(StoreBookmarkListResponseDTO.StoreBookmarkResponseDTO.toDTO(bookmarkEntity, storeEntity));
        }

        return bookmarks;
    }

    public List<OnlinestoreBookmarkListResponseDTO.OnlinestoreBookmarkResponseDTO> readBookmarksOnlinestore(Long size, Integer pageNumber, AuthDetails authDetails) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        List<BookmarkEntity> bookmarkEntities = bookmarkRepository.findNextPageType(2147483647, pageable, memberEntity, BookmarkType.ONLINEOUTDOORMALL);

        List<OnlinestoreBookmarkListResponseDTO.OnlinestoreBookmarkResponseDTO> bookmarks = new ArrayList<>();

        for (BookmarkEntity bookmarkEntity : bookmarkEntities) {
            OnlineOutdoorMallEntity onlineOutdoorMallEntity = bookmarkEntity.getOnlineOutdoorMallEntity();
            bookmarks.add(OnlinestoreBookmarkListResponseDTO.OnlinestoreBookmarkResponseDTO.toDTO(bookmarkEntity, onlineOutdoorMallEntity));
        }

        return bookmarks;
    }

    public boolean hasNextBookmarks(Long size, Integer pageNumber, AuthDetails authDetails) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        List<BookmarkEntity> bookmarkEntities = bookmarkRepository.findNextPage(2147483647, pageable, memberEntity);

        if (bookmarkEntities.isEmpty()) {
            return false;
        }
        List<BookmarkEntity> nextBookmarkEntities = bookmarkRepository.findNextPage(bookmarkEntities.get(bookmarkEntities.size() - 1).getBmarkid(), pageable, memberEntity);

        return !nextBookmarkEntities.isEmpty();
    }

}
