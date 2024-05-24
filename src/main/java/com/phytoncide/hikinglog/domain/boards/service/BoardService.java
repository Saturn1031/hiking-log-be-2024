package com.phytoncide.hikinglog.domain.boards.service;

import com.phytoncide.hikinglog.domain.boards.dto.BoardWriteDTO;
import com.phytoncide.hikinglog.domain.boards.entity.BoardEntity;
import com.phytoncide.hikinglog.domain.boards.entity.ImageEntity;
import com.phytoncide.hikinglog.domain.boards.repository.BoardRepository;
import com.phytoncide.hikinglog.domain.boards.repository.ImageRepository;
import com.phytoncide.hikinglog.domain.member.config.AuthDetails;
import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;

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

                        String extention = originalFileName.substring(originalFileName.lastIndexOf("."));

                        UUID uuid = UUID.randomUUID();
                        String storedFileName = uuid + "_" + originalFileName;

                        Integer position = boardWriteDTO.getImages().indexOf(imageName);

                        // S3에 파일 저장하는 로직 추가
//                InputStream is = file.getInputStream();
//                byte[] bytes = IOUtils.toByteArray(is);
//
//                ObjectMetadata metadata = new ObjectMetadata();
//                metadata.setContentType("image/" + extention);
//                metadata.setContentLength(bytes.length);
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//
//                try{
//                    PutObjectRequest putObjectRequest =
//                            new PutObjectRequest(bucketName, storedFileName, byteArrayInputStream, metadata)
//                                    .withCannedAcl(CannedAccessControlList.PublicRead);
//                    amazonS3.putObject(putObjectRequest); // put image to S3
//                }catch (Exception e){
//                    throw new S3Exception(ErrorCode.PUT_OBJECT_EXCEPTION);
//                }finally {
//                    byteArrayInputStream.close();
//                    is.close();
//                }

                        ImageEntity imageEntity = boardWriteDTO.toImageEntity(boardEntity, originalFileName, storedFileName, position);
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
                 String filename = image.getStoredFileName();
                 Integer fileId = image.getIid();
                 // S3에서 이미지 삭제하는 로직 추가

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

        String email = authDetails.getUsername();
        MemberEntity memberEntity = memberRepository.findByEmail(email);

        BoardEntity boardEntity = boardRepository.findById(boardId).get();

        boardEntity.setTitle(boardWriteDTO.getTitle());
        boardEntity.setContent(boardWriteDTO.getContent());
        boardEntity.setTag(boardWriteDTO.getTag());

        List<ImageEntity> storedImages = imageRepository.findAllByBoardEntity_Bid(boardId);
        if (!storedImages.isEmpty()) {
            for (ImageEntity image: storedImages) {
                String filename = image.getStoredFileName();
                Integer fileId = image.getIid();
                // S3에서 이미지 삭제하는 로직 추가

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

                        String extention = originalFileName.substring(originalFileName.lastIndexOf("."));

                        UUID uuid = UUID.randomUUID();
                        String storedFileName = uuid + "_" + originalFileName;

                        Integer position = boardWriteDTO.getImages().indexOf(imageName);

                        // S3에 파일 저장하는 로직 추가
//                InputStream is = file.getInputStream();
//                byte[] bytes = IOUtils.toByteArray(is);
//
//                ObjectMetadata metadata = new ObjectMetadata();
//                metadata.setContentType("image/" + extention);
//                metadata.setContentLength(bytes.length);
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//
//                try{
//                    PutObjectRequest putObjectRequest =
//                            new PutObjectRequest(bucketName, storedFileName, byteArrayInputStream, metadata)
//                                    .withCannedAcl(CannedAccessControlList.PublicRead);
//                    amazonS3.putObject(putObjectRequest); // put image to S3
//                }catch (Exception e){
//                    throw new S3Exception(ErrorCode.PUT_OBJECT_EXCEPTION);
//                }finally {
//                    byteArrayInputStream.close();
//                    is.close();
//                }

                        ImageEntity imageEntity = boardWriteDTO.toImageEntity(boardEntity, originalFileName, storedFileName, position);
                        imageRepository.save(imageEntity);
                    }
                }
            }
        }

        return "게시글 수정에 성공했습니다.";
    }
}
