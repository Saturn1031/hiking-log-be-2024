package com.phytoncide.hikinglog.domain.mypage.service;

import com.phytoncide.hikinglog.domain.awsS3.AmazonS3Service;
import com.phytoncide.hikinglog.domain.boards.dto.BoardListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
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
import org.springframework.stereotype.Service;

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

    public List<BoardListResponseDTO.BoardResponseDTO> readMyBoards(Integer size, Integer page, AuthDetails authDetails) {
        // 로직을 구현합니다. 예를 들어, 사용자 ID를 기반으로 게시글을 조회하는 로직입니다.
        Integer userId = authDetails.getMemberEntity().getUid();
        // 예시: repository를 통해 사용자 ID로 게시글을 조회합니다.
        List<BoardEntity> myBoards = boardRepository.findByMemberEntityUid(userId, PageRequest.of(page, size));

        return myBoards.stream()
                .map(board -> new BoardListResponseDTO.BoardResponseDTO(board))
                .collect(Collectors.toList());
    }

    public boolean hasNextMyBoards(Integer size, Integer page, AuthDetails authDetails) {
        // 다음 페이지가 있는지 확인하는 로직을 구현합니다.
        Integer userId = authDetails.getMemberEntity().getUid();
        // 예시: repository를 통해 사용자 ID로 게시글을 조회하여 다음 페이지 유무를 확인합니다.
        Page<BoardEntity> myBoardsPage = (Page<BoardEntity>) boardRepository.findByMemberEntityUid(userId, PageRequest.of(page + 1, size));
        return myBoardsPage.hasContent();
    }

}
