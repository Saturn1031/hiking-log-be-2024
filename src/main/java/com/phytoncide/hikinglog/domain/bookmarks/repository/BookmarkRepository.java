package com.phytoncide.hikinglog.domain.bookmarks.repository;

import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.bookmarks.entity.BookmarkEntity;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.global.enums.BookmarkType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Integer> {
    BookmarkEntity findByStoreEntity_SidAndMemberEntity_Uid(Integer sid, Integer uid);

    BookmarkEntity findByOnlineOutdoorMallEntity_OidAndMemberEntity_Uid(Integer oid, Integer uid);

    BookmarkEntity findByMountainEntity_MidAndMemberEntity_Uid(Integer mid, Integer uid);

    @Query("SELECT p FROM BookmarkEntity p WHERE p.id < :cursor AND p.memberEntity = :uid ORDER BY p.id DESC")
    List<BookmarkEntity> findNextPage(@Param("cursor") Integer cursor, Pageable pageable, @Param("uid") MemberEntity uid);

    @Query("SELECT p FROM BookmarkEntity p WHERE p.id < :cursor AND p.memberEntity = :uid AND p.bookmarkType = :type ORDER BY p.id DESC")
    List<BookmarkEntity> findNextPageType(@Param("cursor") Integer cursor, Pageable pageable, @Param("uid") MemberEntity uid, @Param("type") BookmarkType type);
}
