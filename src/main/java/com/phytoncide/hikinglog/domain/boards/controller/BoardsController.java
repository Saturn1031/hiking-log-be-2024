package com.phytoncide.hikinglog.domain.boards.controller;

import com.phytoncide.hikinglog.domain.boards.dto.BoardImageUploadDTO;
import com.phytoncide.hikinglog.domain.boards.dto.BoardWriteDTO;
import com.phytoncide.hikinglog.domain.boards.service.BoardService;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/boards")
@RequiredArgsConstructor
public class BoardsController {
    private final BoardService boardService;

    @PostMapping("")
    public String boardWrite(@RequestPart("data") BoardWriteDTO boardWriteDTO,
                             @ModelAttribute BoardImageUploadDTO boardImageUploadDTO,
                             @AuthenticationPrincipal AuthDetails authDetails) throws IOException {
        System.out.println(authDetails);
        return boardService.createBoard(boardWriteDTO, boardImageUploadDTO, authDetails);
    }
}
