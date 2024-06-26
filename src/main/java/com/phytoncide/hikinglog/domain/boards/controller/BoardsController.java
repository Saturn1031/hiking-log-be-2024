package com.phytoncide.hikinglog.domain.boards.controller;

import com.phytoncide.hikinglog.domain.boards.dto.BoardListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.dto.BoardWriteDTO;
import com.phytoncide.hikinglog.domain.boards.dto.CommentListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.dto.CursorPageRequestDto;
import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.boards.service.BoardService;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/boards")
@RequiredArgsConstructor
public class BoardsController {
    private final BoardService boardService;

    @PostMapping("")
    public String boardWrite(@RequestPart("data") BoardWriteDTO boardWriteDTO,
                             @RequestPart(value = "images", required = false) List<MultipartFile> images,
                             @AuthenticationPrincipal AuthDetails authDetails) throws IOException {
        return boardService.createBoard(boardWriteDTO, images, authDetails);
    }

    @DeleteMapping("/{boardId}")
    public String boardDelete(@PathVariable("boardId") Integer boardId,
                              @AuthenticationPrincipal AuthDetails authDetails) {
        return boardService.deleteBoard(boardId, authDetails);
    }

    @PatchMapping("/{boardId}")
    public String boardUpdate(@PathVariable("boardId") Integer boardId,
                              @RequestPart("data") BoardWriteDTO boardWriteDTO,
                              @RequestPart(value = "images", required = false) List<MultipartFile> images,
                              @AuthenticationPrincipal AuthDetails authDetails) throws IOException {
        return boardService.updateBoard(boardId, boardWriteDTO, images, authDetails);
    }

    @GetMapping("")
    public BoardListResponseDTO boardRead(@RequestBody CursorPageRequestDto cursorPageRequestDto,
                                          @AuthenticationPrincipal AuthDetails authDetails) {
        Integer size = cursorPageRequestDto.getSize();
        Integer page = cursorPageRequestDto.getPage();
        List<BoardListResponseDTO.BoardResponseDTO> boardList = boardService.readeBoards(size, page, authDetails);

        if (!boardService.hasNextBoards(size, page)) {
            return BoardListResponseDTO.builder()
                    .boardList(boardList)
                    .hasNext(false)
                    .build();
        }

        return BoardListResponseDTO.builder()
                .boardList(boardList)
                .hasNext(true)
                .build();
    }

    @PostMapping("/{boardId}/comments")
    public String commentWrite(@PathVariable("boardId") Integer boardId,
                               @RequestBody Map<String,String> req,
                               @AuthenticationPrincipal AuthDetails authDetails) {
        return boardService.createComment(boardId, req.get("content"), authDetails);
    }

    @DeleteMapping("/{boardId}/comments/{commentId}")
    public String commentDelete(@PathVariable("commentId") Integer commentId,
                              @AuthenticationPrincipal AuthDetails authDetails) {
        return boardService.deleteComment(commentId, authDetails);
    }

    @GetMapping("/{boardId}/comments")
    public CommentListResponseDTO commentRead(@PathVariable("boardId") Integer boardId,
                                              @RequestBody CursorPageRequestDto cursorPageRequestDto,
                                              @AuthenticationPrincipal AuthDetails authDetails) {
        Integer size = cursorPageRequestDto.getSize();
        Integer page = cursorPageRequestDto.getPage();
        List<CommentListResponseDTO.CommentResponseDTO> commentList = boardService.readeComments(boardId, size, page, authDetails);

        if (!boardService.hasNextComments(boardId, size, page)) {
            return CommentListResponseDTO.builder()
                    .commentList(commentList)
                    .hasNext(false)
                    .build();
        }

        return CommentListResponseDTO.builder()
                .commentList(commentList)
                .hasNext(true)
                .build();
    }
}
