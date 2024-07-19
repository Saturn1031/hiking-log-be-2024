package com.phytoncide.hikinglog.domain.mypage.controller;

import com.phytoncide.hikinglog.base.code.ResponseCode;
import com.phytoncide.hikinglog.base.dto.ResponseDTO;
import com.phytoncide.hikinglog.domain.boards.dto.BoardListResponseDTO;
import com.phytoncide.hikinglog.domain.boards.dto.CursorPageRequestDto;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.mypage.service.MyBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
public class MyBoardController {
    private final MyBoardService boardService;

    public MyBoardController(MyBoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/my")
    public ResponseEntity<ResponseDTO> readMyBoards(@AuthenticationPrincipal AuthDetails authDetails) {
        List<BoardListResponseDTO.BoardResponseDTO> boardList = boardService.readMyBoards(authDetails);

        BoardListResponseDTO res;
        res = BoardListResponseDTO.builder()
                .boardList(boardList)
                .build();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_BOARD_READ.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_BOARD_READ, res));
    }

}
