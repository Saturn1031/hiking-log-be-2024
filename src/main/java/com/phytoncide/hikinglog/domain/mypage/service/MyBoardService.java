package com.phytoncide.hikinglog.domain.mypage.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.CursorSizeOutOfRangeException;
import com.phytoncide.hikinglog.domain.awsS3.AmazonS3Service;
import com.phytoncide.hikinglog.domain.boards.dto.BoardListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.boards.entity.ImageEntity;
import com.phytoncide.hikinglog.domain.boards.repository.BoardRepository;
import com.phytoncide.hikinglog.domain.boards.repository.CommentRepository;
import com.phytoncide.hikinglog.domain.boards.repository.ImageRepository;
import com.phytoncide.hikinglog.domain.boards.repository.LikesRepository;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import com.phytoncide.hikinglog.domain.mypage.repository.MyBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyBoardService {

    private final MyBoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    private final AmazonS3Service amazonS3Service;

    public MyBoardService(MyBoardRepository boardRepository, MemberRepository memberRepository, ImageRepository imageRepository, LikesRepository likesRepository, CommentRepository commentRepository, AmazonS3Service amazonS3Service) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.imageRepository = imageRepository;
        this.likesRepository = likesRepository;
        this.commentRepository = commentRepository;
        this.amazonS3Service = amazonS3Service;
    }

//    public List<BoardListResponseDTO.BoardResponseDTO> readMyBoards(AuthDetails authDetails) {
//        // 로직을 구현합니다. 예를 들어, 사용자 ID를 기반으로 게시글을 조회하는 로직입니다.
//        Integer userId = authDetails.getMemberEntity().getUid();
//        // 예시: repository를 통해 사용자 ID로 게시글을 조회합니다.
//        List<BoardEntity> myBoards = boardRepository.findByMemberEntityUid(userId);
//
//        return myBoards.stream()
//                .map(board -> new BoardListResponseDTO.BoardResponseDTO(board))
//                .collect(Collectors.toList());
//    }

    public List<BoardListResponseDTO.BoardResponseDTO> readMyBoards(Long size, Integer pageNumber, AuthDetails authDetails) {
        // size가 null인 경우 기본값 설정, 그리고 유효 범위 체크
        if (size == null) {
            size = 10L; // 기본값 설정 (예: 10)
        }

        if (size < 1 || size > 2147483647) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        // pageNumber가 null인 경우 기본값 설정
        if (pageNumber == null) {
            pageNumber = 0; // 기본값 설정 (예: 첫 번째 페이지)
        }

        Pageable pageable = PageRequest.of(pageNumber, limit);
        Integer userId = authDetails.getMemberEntity().getUid();

        // 사용자의 게시물만 조회
        List<BoardEntity> boardEntities = boardRepository.findNextPageByUserId(userId, pageable);

        // 게시물 목록을 DTO로 변환
        List<BoardListResponseDTO.BoardResponseDTO> boards = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            ImageEntity imageEntity = imageRepository.findByBoardEntity_Bid(boardEntity.getBid());
            Integer likeNum = likesRepository.countByBoardEntity_Bid(boardEntity.getBid()).intValue();
            boolean liked = likesRepository.existsByBoardEntity_BidAndMemberEntity_Uid(
                    boardEntity.getBid(), userId);
            Integer commentNum = commentRepository.countByBoardEntity_Bid(boardEntity.getBid()).intValue();

            boards.add(BoardListResponseDTO.BoardResponseDTO.toDTO(
                    boardEntity,
                    imageEntity != null ? imageEntity.getStoredUrl() : null,
                    likeNum,
                    liked,
                    commentNum));
        }
        return boards;
    }

    public boolean hasNextBoards(Long size, Integer pageNumber) {
        // size가 null인 경우 기본값 설정, 그리고 유효 범위 체크
        if (size == null) {
            size = 10L; // 기본값 설정 (예: 10)
        }

        if (size < 1 || size > 2147483647) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        // pageNumber가 null인 경우 기본값 설정
        if (pageNumber == null) {
            pageNumber = 0; // 기본값 설정 (예: 첫 번째 페이지)
        }

        Pageable pageable = PageRequest.of(pageNumber, limit);
        List<BoardEntity> boardEntities = boardRepository.findNextPageByUserId(2147483647, pageable);

        if (boardEntities.isEmpty()) {
            return false;
        }
        List<BoardEntity> nextboardEntities = boardRepository.findNextPageByUserId(boardEntities.get(boardEntities.size() - 1).getBid(), pageable);

        return !nextboardEntities.isEmpty();
    }

}
