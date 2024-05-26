package com.phytoncide.hikinglog.domain.boards.controller;

import com.phytoncide.hikinglog.domain.boards.dto.BoardListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.dto.BoardWriteDTO;
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
    public BoardListResponseDTO boardRead(@RequestBody CursorPageRequestDto cursorPageRequestDto) {
        Integer size = cursorPageRequestDto.getSize();
        Integer page = cursorPageRequestDto.getPage();
        List<BoardListResponseDTO.BoardResponseDTO> boardList = boardService.readeBoards(size, page);

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
}
