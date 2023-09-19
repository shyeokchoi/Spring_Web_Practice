package com.board.service.post;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.dto.post.UpdatePostDTO;
import com.board.enums.FileInfoParentTypeEnum;
import com.board.exception.AuthenticationException;
import com.board.mapper.post.PostMapper;
import com.board.service.storage.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final StorageService storageService;

    @Override
    public Integer insPost(InsPostDTO insPostDTO) {
        // 새로운 게시물 생성
        postMapper.insPost(insPostDTO);

        Integer newPostNo = insPostDTO.getNo();
        List<Integer> fileInfoNoList = insPostDTO.getFileInfoNoList();

        Queue<String> fileNamesForRollback = new LinkedList<>();

        for (Integer fileInfoNo : fileInfoNoList) {
            String fileName = storageService.selectFileSaveName(fileInfoNo);
            Path dst = Paths.get(newPostNo.toString());

            // 게시물에서 등록한 파일들 상태 업데이트
            // TEMP에서 NORMAL로 & 저장 path 설정 & 부모 no, 부모 type 설정
            storageService.changeFileStatus(fileInfoNo, fileName, newPostNo, FileInfoParentTypeEnum.POST);

            try {
                // 파일 부모 no.를 이름으로 갖는 하위폴더로 옮기기
                storageService.moveFile(null, dst.toString(), fileName);
                fileNamesForRollback.add(fileName);
            } catch (IOException ioe) {
                // 파일 옮기기 실패시, DB 롤백됨
                // DB 롤백에 맞춰서 이미 옮긴 파일들도 롤백
                while (!fileNamesForRollback.isEmpty()) {
                    String targetFileName = fileNamesForRollback.poll();

                    try {
                        storageService.moveFile(dst.toString(), null, targetFileName);
                    } catch (IOException rollbackIoe) {
                        throw new RuntimeException(
                                "ERROR : 파일을 옮기는데 실패해 롤백하는 과정에서 다시 한 번 실패했습니다.",
                                rollbackIoe);
                    }
                }

                // 파일을 옮기는 데 실패했다는 exception.
                throw new RuntimeException("ERROR : 파일을 옮기는 데 실패했습니다.", ioe);
            }
        }

        return newPostNo;
    }

    @Override
    public SelectPostDetailDTO selectPost(Integer postNo) {
        // 해당 포스트와 연결된 파일 no 리스트 받아오기
        List<Integer> fileNoList = postMapper.selectFileNoList(postNo);

        // 해당 postNo에 해당하는 게시물 검색
        SelectPostDetailDTO post = postMapper.selectPost(postNo);

        if (post != null) {
            // SelectPostDetailDTO 에 해당 게시글과 연결된 파일 이름들 넣어주기
            post.setFileNoList(fileNoList);
        }

        return post;
    }

    @Override
    public void updatePost(UpdatePostDTO updatePostDTO) {
        // 게시물 내용 업데이트
        postMapper.updatePost(updatePostDTO);
    }

    @Override
    public void deletePost(Integer currMemberNo, Integer postNo) {
        // 자신의 글만 삭제할 수 있음.
        if (postMapper.retvAuthorNo(postNo) != currMemberNo) {
            throw new AuthenticationException("자신의 글만 삭제할 수 있습니다");
        }
        postMapper.deletePost(postNo);
    }

    @Override
    public PagingResponseDTO<SelectPostListDTO> selectPostList(PagingRequestDTO pagingRequestDTO) {
        return selectPostList(null, pagingRequestDTO);
    }

    @Override
    public PagingResponseDTO<SelectPostListDTO> selectPostList(Integer authorNo, PagingRequestDTO pagingRequestDTO) {
        int totalRows = postMapper.selectTotalRows(authorNo, pagingRequestDTO);

        List<SelectPostListDTO> postList = postMapper.selectPostList(authorNo, pagingRequestDTO);

        PagingResponseDTO<SelectPostListDTO> pagingResponseDTO = new PagingResponseDTO<>(postList,
                pagingRequestDTO.getCurrPage(), pagingRequestDTO.getPageSize(), totalRows);

        return pagingResponseDTO;
    }

    private Integer selectPrevTempPostNo(Integer currMemberNo) {
        return postMapper.selectPrevTempPostNo(currMemberNo);
    }

    @Override
    public Integer insTempPost(InsPostDTO insPostDTO) {
        // 현재 접속한 멤버의 no.
        Integer currMemberNo = insPostDTO.getAuthorNo();

        // 기존에 임시저장해둔 게시글의 no. 없으면 null
        Integer prevTempPostNo = selectPrevTempPostNo(currMemberNo);

        // 기존에 임시저장해둔 게시글이 있으면 지운다. (임시저장은 한번에 하나만)
        if (prevTempPostNo != null) {
            deletePost(currMemberNo, prevTempPostNo);
        }

        // 임시저장
        return insPost(insPostDTO);
    }

    @Override
    public SelectPostDetailDTO selectTempPost(Integer memberNo) {
        // 임시저장된 글 no 불러오기
        Integer prevTempPostNo = selectPrevTempPostNo(memberNo);

        // 임시저장된 글이 없으면 예외처리
        if (prevTempPostNo == null) {
            throw new NoSuchElementException("임시저장된 글이 없습니다.");
        }

        // 임시저장된 글 정보 불러오기
        SelectPostDetailDTO tempPostDetail = selectPost(prevTempPostNo);

        // 기존 임시저장된 글 삭제
        deletePost(memberNo, prevTempPostNo);

        return tempPostDetail;
    }
}
