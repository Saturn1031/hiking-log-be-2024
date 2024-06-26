package com.phytoncide.hikinglog.domain.boards.service;

import com.phytoncide.hikinglog.domain.awsS3.AmazonS3Service;
import com.phytoncide.hikinglog.domain.boards.dto.BoardListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.dto.BoardWriteDTO;
import com.phytoncide.hikinglog.domain.boards.dto.CommentListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.boards.entity.CommentEntity;
import com.phytoncide.hikinglog.domain.boards.entity.ImageEntity;
import com.phytoncide.hikinglog.domain.boards.repository.BoardRepository;
import com.phytoncide.hikinglog.domain.boards.repository.CommentRepository;
import com.phytoncide.hikinglog.domain.boards.repository.ImageRepository;
import com.phytoncide.hikinglog.domain.boards.repository.LikesRepository;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    private final AmazonS3Service amazonS3Service;

    public String createBoard(BoardWriteDTO boardWriteDTO, List<MultipartFile> files, AuthDetails authDetails) throws IOException {
        if (boardWriteDTO.getTitle().isEmpty()) {
            return "제목을 작성해주세요.";
        }
        if (boardWriteDTO.getContent().isEmpty()) {
            return "내용을 작성해주세요.";
        }

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        BoardEntity boardEntity = boardWriteDTO.toBoardEntity(memberEntity);
        boardRepository.save(boardEntity);

        if (files != null && !files.isEmpty()) {
            for (String imageName : boardWriteDTO.getImages()) {
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        String originalFileName = file.getOriginalFilename();

                        if (!imageName.equals(originalFileName)) {
                            continue;
                        }

                        String imageFileUrl = amazonS3Service.saveFile(file);

                        Integer position = boardWriteDTO.getImages().indexOf(imageName);

                        ImageEntity imageEntity = boardWriteDTO.toImageEntity(boardEntity, imageFileUrl, position);
                        imageRepository.save(imageEntity);
                    }
                }
            }
        }

        return "게시글 작성에 성공했습니다.";
    }

    public String deleteBoard(Integer boardId, AuthDetails authDetails) {
        Integer userId = authDetails.getMemberEntity().getUid();
        if (boardRepository.findById(boardId).isEmpty()) {
            return "유효하지 않은 게시글 아이디입니다.";
        }
        Integer boardUserId = boardRepository.findById(boardId).get().getMemberEntity().getUid();

        if (!boardUserId.equals(userId)) {
            return "게시글 삭제 권한이 없습니다.";
        }

        List<ImageEntity> storedImages = imageRepository.findAllByBoardEntity_Bid(boardId);
        if (!storedImages.isEmpty()) {
            for (ImageEntity image: storedImages) {
                Integer fileId = image.getIid();
                String imageUrl = image.getStoredUrl();

                amazonS3Service.deleteFile(imageUrl);

                imageRepository.deleteById(fileId);
            }
        }

        boardRepository.deleteById(boardId);

        return "게시글 삭제에 성공했습니다.";
    }

    public String updateBoard(Integer boardId, BoardWriteDTO boardWriteDTO, List<MultipartFile> files, AuthDetails authDetails) throws IOException {
        Integer userId = authDetails.getMemberEntity().getUid();
        if (boardRepository.findById(boardId).isEmpty()) {
            return "유효하지 않은 게시글 아이디입니다.";
        }
        Integer boardUserId = boardRepository.findById(boardId).get().getMemberEntity().getUid();

        if (!boardUserId.equals(userId)) {
            return "게시글 수정 권한이 없습니다.";
        }

        if (boardWriteDTO.getTitle().isEmpty()) {
            return "제목을 작성해주세요.";
        }
        if (boardWriteDTO.getContent().isEmpty()) {
            return "내용을 작성해주세요.";
        }

        BoardEntity boardEntity = boardRepository.findById(boardId).get();

        boardEntity.setTitle(boardWriteDTO.getTitle());
        boardEntity.setContent(boardWriteDTO.getContent());
        boardEntity.setTag(boardWriteDTO.getTag());

        List<ImageEntity> storedImages = imageRepository.findAllByBoardEntity_Bid(boardId);
        if (!storedImages.isEmpty()) {
            for (ImageEntity image: storedImages) {
                Integer fileId = image.getIid();
                String imageUrl = image.getStoredUrl();

                amazonS3Service.deleteFile(imageUrl);

                imageRepository.deleteById(fileId);
            }
        }

        if (files != null && !files.isEmpty()) {
            for (String imageName : boardWriteDTO.getImages()) {
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        String originalFileName = file.getOriginalFilename();

                        if (!imageName.equals(originalFileName)) {
                            continue;
                        }

                        String imageFileUrl = amazonS3Service.saveFile(file);

                        Integer position = boardWriteDTO.getImages().indexOf(imageName);

                        ImageEntity imageEntity = boardWriteDTO.toImageEntity(boardEntity, imageFileUrl, position);
                        imageRepository.save(imageEntity);
                    }
                }
            }
        }

        return "게시글 수정에 성공했습니다.";
    }

    public List<BoardListResponseDTO.BoardResponseDTO> readeBoards(Integer limit, Integer pageNumber, AuthDetails authDetails) {

        Pageable pageable = PageRequest.of(pageNumber, limit);
        List<BoardEntity> boardEntities = boardRepository.findNextPage(2147483647, pageable);

        List<BoardListResponseDTO.BoardResponseDTO> boards = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {

            List<String> images = new ArrayList<>();
            List<ImageEntity> imageEntities = imageRepository.findAllByBoardEntity_Bid(boardEntity.getBid());
            for (ImageEntity imageEntity : imageEntities) {
                images.add(imageEntity.getStoredUrl());
            }

            Integer likeNum = likesRepository.countByBoardEntity_Bid(boardEntity.getBid()).intValue();
            boolean liked = likesRepository.existsByBoardEntity_BidAndMemberEntity_Uid(boardEntity.getBid(), authDetails.getMemberEntity().getUid());

            Integer commentNum = commentRepository.countByBoardEntity_Bid(boardEntity.getBid()).intValue();

            Integer commentid;
            if (commentRepository.existsByBoardEntity_Bid(boardEntity.getBid())) {
                List<CommentEntity> comments = commentRepository.findAllByBoardEntity_Bid(boardEntity.getBid(), Sort.by(Sort.Direction.DESC, "createdAt"));
                commentid = comments.get(0).getCid();
            } else {
                commentid = -1;
            }

            boards.add(BoardListResponseDTO.BoardResponseDTO.toDTO(boardEntity, images, likeNum, liked, commentNum, commentid));
        }
        return boards;
    }

    public boolean hasNextBoards(Integer limit, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, limit);
        List<BoardEntity> boardEntities = boardRepository.findNextPage(2147483647, pageable);

        if (boardEntities.size() == 0) {
            return false;
        }
        List<BoardEntity> nextboardEntities = boardRepository.findNextPage(boardEntities.get(boardEntities.size() - 1).getBid(), pageable);

        return !nextboardEntities.isEmpty();
    }

    public String createComment(Integer boardId, String content, AuthDetails authDetails) {
        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);
        Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);

        if (content.isEmpty()) {
            return "댓글 내용을 작성해주세요.";
        }

        CommentEntity commentEntity = CommentEntity.builder()
                .content(content)
                .memberEntity(memberEntity)
                .boardEntity(boardEntity.get())
                .build();

        commentRepository.save(commentEntity);

        return "댓글 작성에 성공했습니다.";
    }

    public String deleteComment(Integer commentId, AuthDetails authDetails) {
        Integer userId = authDetails.getMemberEntity().getUid();
        Integer commentUserId = commentRepository.findById(commentId).get().getMemberEntity().getUid();
        if (!commentUserId.equals(userId)) {
            return "댓글 삭제 권한이 없습니다.";
        }

        commentRepository.deleteById(commentId);

        return "댓글 삭제에 성공했습니다.";
    }

    public List<CommentListResponseDTO.CommentResponseDTO> readeComments(Integer boardId, Integer limit, Integer pageNumber, AuthDetails authDetails) {

        Pageable pageable = PageRequest.of(pageNumber, limit);
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntities = commentRepository.findNextPage(2147483647, pageable, boardEntity);

        List<CommentListResponseDTO.CommentResponseDTO> comments = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            comments.add(CommentListResponseDTO.CommentResponseDTO.toDTO(commentEntity));
        }
        return comments;
    }

    public boolean hasNextComments(Integer boardId, Integer limit, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, limit);
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntities = commentRepository.findNextPage(2147483647, pageable, boardEntity);

        if (commentEntities.size() == 0) {
            return false;
        }
        List<BoardEntity> nextboardEntities = boardRepository.findNextPage(commentEntities.get(commentEntities.size() - 1).getCid(), pageable);

        return !nextboardEntities.isEmpty();
    }
}
