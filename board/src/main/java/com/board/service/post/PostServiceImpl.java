package com.board.service.post;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
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
import com.board.exception.NoDataFoundException;
import com.board.mapper.file.FileMapper;
import com.board.mapper.post.PostMapper;
import com.board.service.storage.StorageService;
import com.board.util.SetOperations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final FileMapper fileMapper;
    private final StorageService storageService;

    private void checkIfPostFound(int postNo) {
        SelectPostDetailDTO postDetail = postMapper.selectPost(postNo);

        if (postDetail == null) {
            throw new NoDataFoundException("게시물이 존재하지 않습니다. 게시물 번호 : " + postNo);
        }
    }

    private void moveTempFilesAndUpdateFileInfo(Integer newPostNo, List<Integer> fileInfoNoList) {
        Queue<String> fileNamesForRollback = new LinkedList<>();

        for (Integer fileInfoNo : fileInfoNoList) {
            String fileName = fileMapper.selectOne(fileInfoNo).getSaveName();
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
                        log.error("파일을 옮기는데 실패해 롤백하는 과정에서 다시 한 번 실패했습니다.", rollbackIoe);
                    }
                }

                // 파일을 옮기는 데 실패했다는 exception.
                throw new RuntimeException("파일을 옮기는 데 실패했습니다.", ioe);
            }
        }
    }

    @Override
    public Integer insPost(InsPostDTO insPostDTO) {
        // 새로운 게시물 생성
        postMapper.insPost(insPostDTO);

        Integer newPostNo = insPostDTO.getNo();
        List<Integer> fileInfoNoList = insPostDTO.getFileInfoNoList();

        moveTempFilesAndUpdateFileInfo(newPostNo, fileInfoNoList);

        return newPostNo;
    }

    @Override
    public SelectPostDetailDTO selectPost(int postNo) {
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
    public Integer updatePost(UpdatePostDTO updatePostDTO) {
        Integer newPostNo = updatePostDTO.getPostNo();

        // postNo 유효성 체크
        checkIfPostFound(newPostNo);

        // 자기 자신의 게시글만 수정 가능
        if (postMapper.retvAuthorNo(updatePostDTO.getPostNo()) != updatePostDTO.getModifierNo()) {
            throw new AuthenticationException("자신의 글만 수정할 수 있습니다");
        }

        // 게시글 내용 업데이트
        postMapper.updatePost(updatePostDTO);

        // 기존 파일 리스트와 새로운 파일 리스트
        List<Integer> newFileInfoNoList = updatePostDTO.getFileInfoNoList();
        List<Integer> curFileInfoNoList = postMapper.selectFileNoList(updatePostDTO.getPostNo());

        // 게시글이 수정되며 추가된 파일 리스트
        List<Integer> addedFileInfoNoList = SetOperations.findDifference(newFileInfoNoList, curFileInfoNoList);

        // 추가된 파일들 옮기고 file_info 업데이트
        moveTempFilesAndUpdateFileInfo(newPostNo, addedFileInfoNoList);

        return updatePostDTO.getPostNo();
    }

    @Override
    public void deletePost(int currMemberNo, int postNo) {
        // postNo 유효성 체크
        checkIfPostFound(postNo);

        // 자신의 글만 삭제할 수 있음
        if (postMapper.retvAuthorNo(postNo) != currMemberNo) {
            throw new AuthenticationException("자신의 글만 삭제할 수 있습니다");
        }

        // 글에 첨부된 파일 목록 불러오기. DB에서 글 삭제 전에 해야하는 작업
        List<Integer> fileInfoNoList = postMapper.selectFileNoList(postNo);

        // 글 삭제
        postMapper.deletePost(postNo);

        // 연관된 file들 삭제
        try {
            storageService.recursiveDelete(Integer.toString(postNo));
        } catch (IOException ioe) {
            log.error("글 삭제 중 - 해당 글에 첨부된 파일 삭제 실패", ioe);
            return;
        }

        // file_info들 삭제
        try {
            for (Integer fileInfoNo : fileInfoNoList) {
                storageService.deleteFileInfo(fileInfoNo);
            }
        } catch (Exception e) {
            log.error("글 삭제 중 - 해당 글에 첨부된 파일들의 file_info 삭제 실패");
        }

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
    public Integer selectTempPostNo(int memberNo) {
        // 임시저장된 글 no 불러오기
        Integer prevTempPostNo = selectPrevTempPostNo(memberNo);

        // 임시저장된 글이 없으면 예외처리
        if (prevTempPostNo == null) {
            throw new NoDataFoundException("임시저장된 글이 없습니다.");
        }

        return prevTempPostNo;
    }

    @Override
    public Integer updateTempPost(int memberNo, UpdatePostDTO updatePostDTO) {
        // postNo 유효성 체크
        checkIfPostFound(updatePostDTO.getPostNo());

        // 자신의 임시 글만 수정할 수 있음.
        if (postMapper.retvAuthorNo(updatePostDTO.getPostNo()) != memberNo) {
            throw new AuthenticationException("자신의 임시글만 수정할 수 있습니다.");
        }

        return updatePost(updatePostDTO);
    }

    @Override
    public Integer finalizeTempPost(int memberNo, int postNo) {
        // postNo 유효성 체크
        checkIfPostFound(postNo);

        // 자신의 임시 글만 최종등록할 수 있음.
        if (postMapper.retvAuthorNo(postNo) != memberNo) {
            throw new AuthenticationException("자신의 임시글만 최종 등록할 수 있습니다.");
        }

        return postMapper.finalizeTempPost(postNo);
    }
}
