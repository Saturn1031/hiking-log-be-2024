package com.phytoncide.hikinglog.domain.boards.service;

import com.phytoncide.hikinglog.base.code.ErrorCode;
import com.phytoncide.hikinglog.base.exception.*;
import com.phytoncide.hikinglog.domain.awsS3.AmazonS3Service;
import com.phytoncide.hikinglog.domain.boards.dto.*;
import com.phytoncide.hikinglog.domain.boards.entity.*;
import com.phytoncide.hikinglog.domain.boards.repository.*;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
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
    private final NotificationRepository notificationRepository;

    private final AmazonS3Service amazonS3Service;

    public String createBoard(BoardWriteDTO boardWriteDTO, MultipartFile image, AuthDetails authDetails) throws IOException {
        if (boardWriteDTO.getContent().isEmpty()) {
            throw new BoardsContentException(ErrorCode.CONTENT_IS_EMPTY);
        }

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        BoardEntity boardEntity = boardWriteDTO.toBoardEntity(memberEntity);
        boardRepository.save(boardEntity);

        if (!image.isEmpty()) {
            String imageFileUrl = amazonS3Service.saveFile(image);

            ImageEntity imageEntity = boardWriteDTO.toImageEntity(boardEntity, imageFileUrl);
            imageRepository.save(imageEntity);
        }

        return "게시글 작성에 성공했습니다.";
    }

    public String deleteBoard(Integer boardId, AuthDetails authDetails) {
        Integer userId = authDetails.getMemberEntity().getUid();
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }
        Integer boardUserId = boardRepository.findById(boardId).get().getMemberEntity().getUid();

        if (!boardUserId.equals(userId)) {
            throw new BoardsDeleteException(ErrorCode.NOT_PERMITTED_TO_DELETE);
        }

        ImageEntity storedImage = imageRepository.findByBoardEntity_Bid(boardId);
        if (storedImage != null) {
            Integer fileId = storedImage.getIid();
            String imageUrl = storedImage.getStoredUrl();

            amazonS3Service.deleteFile(imageUrl);

            imageRepository.deleteById(fileId);
        }

        boardRepository.deleteById(boardId);

        return "게시글 삭제에 성공했습니다.";
    }

    public String updateBoard(Integer boardId, BoardWriteDTO boardWriteDTO, MultipartFile image, AuthDetails authDetails) throws IOException {
        Integer userId = authDetails.getMemberEntity().getUid();
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }
        Integer boardUserId = boardRepository.findById(boardId).get().getMemberEntity().getUid();

        if (!boardUserId.equals(userId)) {
            throw new BoardsDeleteException(ErrorCode.NOT_PERMITTED_TO_DELETE);
        }

        if (boardWriteDTO.getContent().isEmpty()) {
            throw new BoardsContentException(ErrorCode.CONTENT_IS_EMPTY);
        }

        BoardEntity boardEntity = boardRepository.findById(boardId).get();

        boardEntity.setContent(boardWriteDTO.getContent());
        boardEntity.setTag(boardWriteDTO.getTag());

        ImageEntity storedImage = imageRepository.findByBoardEntity_Bid(boardId);
        if (storedImage != null) {
            Integer fileId = storedImage.getIid();
            String imageUrl = storedImage.getStoredUrl();

            amazonS3Service.deleteFile(imageUrl);

            imageRepository.deleteById(fileId);
        }

        if (!image.isEmpty()) {
            String imageFileUrl = amazonS3Service.saveFile(image);

            ImageEntity imageEntity = boardWriteDTO.toImageEntity(boardEntity, imageFileUrl);
            imageRepository.save(imageEntity);
        }

        return "게시글 수정에 성공했습니다.";
    }

    public BoardListResponseDTO.BoardResponseDTO readBoard(Integer boardId, AuthDetails authDetails) {
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }
        BoardEntity boardEntity = boardRepository.findById(boardId).get();

        ImageEntity imageEntity = imageRepository.findByBoardEntity_Bid(boardEntity.getBid());

        Integer likeNum = likesRepository.countByBoardEntity_Bid(boardEntity.getBid()).intValue();
        boolean liked = likesRepository.existsByBoardEntity_BidAndMemberEntity_Uid(boardEntity.getBid(), authDetails.getMemberEntity().getUid());

        Integer commentNum = commentRepository.countByBoardEntity_Bid(boardEntity.getBid()).intValue();

        if (imageEntity == null) {
            return BoardListResponseDTO.BoardResponseDTO.toDTO(boardEntity, "", likeNum, liked, commentNum);
        }
        return BoardListResponseDTO.BoardResponseDTO.toDTO(boardEntity, imageEntity.getStoredUrl(), likeNum, liked, commentNum);
    }

    public List<BoardListResponseDTO.BoardResponseDTO> readBoards(Long size, Integer pageNumber, AuthDetails authDetails) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);
        List<BoardEntity> boardEntities = boardRepository.findNextPage(2147483647, pageable);

        List<BoardListResponseDTO.BoardResponseDTO> boards = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {

            ImageEntity imageEntity = imageRepository.findByBoardEntity_Bid(boardEntity.getBid());

            Integer likeNum = likesRepository.countByBoardEntity_Bid(boardEntity.getBid()).intValue();
            boolean liked = likesRepository.existsByBoardEntity_BidAndMemberEntity_Uid(boardEntity.getBid(), authDetails.getMemberEntity().getUid());

            Integer commentNum = commentRepository.countByBoardEntity_Bid(boardEntity.getBid()).intValue();

            if (imageEntity == null) {
                boards.add(BoardListResponseDTO.BoardResponseDTO.toDTO(boardEntity, "", likeNum, liked, commentNum));
            } else {
                boards.add(BoardListResponseDTO.BoardResponseDTO.toDTO(boardEntity, imageEntity.getStoredUrl(), likeNum, liked, commentNum));
            }
        }
        return boards;
    }

    public boolean hasNextBoards(Long size, Integer pageNumber) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);
        List<BoardEntity> boardEntities = boardRepository.findNextPage(2147483647, pageable);

        if (boardEntities.isEmpty()) {
            return false;
        }
        List<BoardEntity> nextboardEntities = boardRepository.findNextPage(boardEntities.get(boardEntities.size() - 1).getBid(), pageable);

        return !nextboardEntities.isEmpty();
    }

    public String createComment(Integer boardId, String content, AuthDetails authDetails) {
        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }
        Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);

        if (content.isEmpty()) {
            throw new BoardsContentException(ErrorCode.CONTENT_IS_EMPTY);
        }

        CommentEntity commentEntity = CommentEntity.builder()
                .content(content)
                .memberEntity(memberEntity)
                .boardEntity(boardEntity.get())
                .build();

        commentRepository.save(commentEntity);

        return "댓글 작성에 성공했습니다.";
    }

    public String deleteComment(Integer boardId, Integer commentId, AuthDetails authDetails) {
        Integer userId = authDetails.getMemberEntity().getUid();
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }
        if (commentRepository.findById(commentId).isEmpty()) {
            throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }
        Integer commentUserId = commentRepository.findById(commentId).get().getMemberEntity().getUid();
        if (!commentUserId.equals(userId)) {
            throw new BoardsDeleteException(ErrorCode.NOT_PERMITTED_TO_DELETE);
        }

        commentRepository.deleteById(commentId);

        return "댓글 삭제에 성공했습니다.";
    }

    public List<CommentListResponseDTO.CommentResponseDTO> readComments(Integer boardId, Long size, Integer pageNumber, AuthDetails authDetails) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntities = commentRepository.findNextPage(2147483647, pageable, boardEntity);

        List<CommentListResponseDTO.CommentResponseDTO> comments = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            comments.add(CommentListResponseDTO.CommentResponseDTO.toDTO(commentEntity));
        }
        return comments;
    }

    public boolean hasNextComments(Integer boardId, Long size, Integer pageNumber) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntities = commentRepository.findNextPage(2147483647, pageable, boardEntity);

        if (commentEntities.isEmpty()) {
            return false;
        }
        List<BoardEntity> nextboardEntities = boardRepository.findNextPage(commentEntities.get(commentEntities.size() - 1).getCid(), pageable);

        return !nextboardEntities.isEmpty();
    }

    public String createLike(Integer boardId, AuthDetails authDetails) {
        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }
        BoardEntity boardEntity = boardRepository.findById(boardId).get();

        LikesEntity likesEntity = LikesEntity.builder()
                .boardEntity(boardEntity)
                .memberEntity(memberEntity)
                .build();

        likesRepository.save(likesEntity);

        return "게시글 좋아요 등록에 성공했습니다.";
    }

    public String deleteLike(Integer boardId, AuthDetails authDetails) {
        Integer userId = authDetails.getMemberEntity().getUid();
        if (boardRepository.findById(boardId).isEmpty()) {
            throw new BoardsNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        if (!likesRepository.existsByBoardEntity_BidAndMemberEntity_Uid(boardId, userId)) {
            throw new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND);
        }

        LikesEntity likesEntity = likesRepository.findByBoardEntity_BidAndMemberEntity_Uid(boardId, userId);

        likesRepository.delete(likesEntity);

        return "게시글 좋아요 삭제에 성공했습니다.";
    }

    public String createNotification(NotificationCreateDTO notificationCreateDTO) {
        Integer notifiedUserId = notificationCreateDTO.getNotifiedUserId();
        if (memberRepository.findById(notifiedUserId).isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        if (notificationCreateDTO.getTitle().isEmpty()) {
            throw new BoardsTitleException(ErrorCode.TITLE_IS_EMPTY);
        }

        if (notificationCreateDTO.getContent().isEmpty()) {
            throw new BoardsContentException(ErrorCode.CONTENT_IS_EMPTY);
        }

        MemberEntity memberEntity = memberRepository.findById(notifiedUserId).get();

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .title(notificationCreateDTO.getTitle())
                .content(notificationCreateDTO.getContent())
                .url(notificationCreateDTO.getUrl())
                .notifiedUid(memberEntity)
                .build();

        notificationRepository.save(notificationEntity);

        return "알림 저장에 성공했습니다.";
    }

    public List<NotificationListResponseDTO.NotificationResponseDTO> readNotifications(Long size, Integer pageNumber, AuthDetails authDetails) {
        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);
        List<NotificationEntity> notificationEntities = notificationRepository.findNextPage(2147483647, pageable, memberEntity);

        List<NotificationListResponseDTO.NotificationResponseDTO> notifications = new ArrayList<>();
        for (NotificationEntity notificationEntity : notificationEntities) {
            notifications.add(NotificationListResponseDTO.NotificationResponseDTO.toDTO(notificationEntity));
        }
        return notifications;
    }

    public boolean hasNextNotifications(MemberEntity memberEntity, Long size, Integer pageNumber) {
        if (size > 2147483647 || size < 1) {
            throw new CursorSizeOutOfRangeException(ErrorCode.CURSOR_SIZE_OUT_OF_RANGE);
        }
        int limit = size.intValue();

        Pageable pageable = PageRequest.of(pageNumber, limit);
        List<NotificationEntity> notificationEntities = notificationRepository.findNextPage(2147483647, pageable, memberEntity);

        if (notificationEntities.isEmpty()) {
            return false;
        }
        List<NotificationEntity> nextNotificationEntities = notificationRepository.findNextPage(notificationEntities.get(notificationEntities.size() - 1).getNid(), pageable, memberEntity);

        return !nextNotificationEntities.isEmpty();
    }

    public String deleteNotification(Integer notificationId, AuthDetails authDetails) {
        if (notificationRepository.findById(notificationId).isEmpty()) {
            throw new NotificationNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }

        Integer userId = authDetails.getMemberEntity().getUid();

        Integer notificationUserId = notificationRepository.findById(notificationId).get().getNotifiedUid().getUid();
        if (!notificationUserId.equals(userId)) {
            throw new BoardsDeleteException(ErrorCode.NOT_PERMITTED_TO_DELETE);
        }

        notificationRepository.deleteById(notificationId);

        return "알림 삭제에 성공했습니다.";
    }
}
