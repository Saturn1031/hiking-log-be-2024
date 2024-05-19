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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
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
}
