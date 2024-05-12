package com.board.service.post;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.common.PagingRequestWithSearchKeywordDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.PostDetailDTO;
import com.board.dto.post.PostSimpleDTO;
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
        PostDetailDTO postDetail = postMapper.selectOne(postNo);

        if (postDetail == null) {
            throw new NoDataFoundException("게시물이 존재하지 않습니다. 게시물 번호 : " + postNo);
        }
    }

    private void moveTempFilesAndUpdateFileInfo(
        Integer newPostNo, List<Integer> fileInfoNoList
    ) {
        Queue<String> fileNamesForRollback = new LinkedList<>();

        for (Integer fileInfoNo : fileInfoNoList) {
            String fileName = fileMapper.selectOne(fileInfoNo).getSaveName();
            Path dst = Paths.get(newPostNo.toString());

            storageService.changeFileStatus(fileInfoNo, fileName, newPostNo, FileInfoParentTypeEnum.POST);
            try {
                storageService.moveFile(null, dst.toString(), fileName);
                fileNamesForRollback.add(fileName);
            } catch (IOException ioe) {
                while (!fileNamesForRollback.isEmpty()) {
                    String targetFileName = fileNamesForRollback.poll();
                    try {
                        storageService.moveFile(dst.toString(), null, targetFileName);
                    } catch (IOException rollbackIoe) {
                        log.error("파일을 옮기는데 실패해 롤백하는 과정에서 다시 한 번 실패했습니다.", rollbackIoe);
                    }
                }
                throw new RuntimeException("파일을 옮기는 데 실패했습니다.", ioe);
            }
        }
    }

    @Override
    public Integer insPost(InsPostDTO insPostDTO) {
        postMapper.insPost(insPostDTO);
        Integer newPostNo = insPostDTO.getNo();
        List<Integer> fileInfoNoList = insPostDTO.getFileInfoNoList();
        moveTempFilesAndUpdateFileInfo(newPostNo, fileInfoNoList);
        return newPostNo;
    }

    @Override
    public PostDetailDTO selectPost(int postNo) {
        List<Integer> fileNoList = postMapper.selectFileNoList(postNo);
        PostDetailDTO post = postMapper.selectOne(postNo);
        if (post != null) {
            post.setFileNoList(fileNoList);
        }
        return post;
    }

    @Override
    public Integer updatePost(UpdatePostDTO updatePostDTO) {
        Integer newPostNo = updatePostDTO.getPostNo();
        checkIfPostFound(newPostNo);
        if (postMapper.retvAuthorNo(updatePostDTO.getPostNo()) != updatePostDTO.getModifierNo()) {
            throw new AuthenticationException("자신의 글만 수정할 수 있습니다");
        }
        postMapper.updatePost(updatePostDTO);

        List<Integer> newFileInfoNoList = updatePostDTO.getFileInfoNoList();
        List<Integer> curFileInfoNoList = postMapper.selectFileNoList(updatePostDTO.getPostNo());
        List<Integer> addedFileInfoNoList = SetOperations.findDifference(
            newFileInfoNoList, curFileInfoNoList
        );

        moveTempFilesAndUpdateFileInfo(newPostNo, addedFileInfoNoList);
        return updatePostDTO.getPostNo();
    }

    @Override
    public void deletePost(int currMemberNo, int postNo) {
        checkIfPostFound(postNo);
        if (postMapper.retvAuthorNo(postNo) != currMemberNo) {
            throw new AuthenticationException("자신의 글만 삭제할 수 있습니다");
        }
        List<Integer> fileInfoNoList = postMapper.selectFileNoList(postNo);
        postMapper.deletePost(postNo);
        postMapper.deleteCommentsRelatedToPost(postNo);

        try {
            storageService.recursiveDelete(Integer.toString(postNo));
        } catch (IOException ioe) {
            log.error("글 삭제 중 - 해당 글에 첨부된 파일 삭제 실패", ioe);
            return;
        }

        try {
            for (Integer fileInfoNo : fileInfoNoList) {
                storageService.deleteFileInfo(fileInfoNo);
            }
        } catch (Exception e) {
            log.error("글 삭제 중 - 해당 글에 첨부된 파일들의 file_info 삭제 실패");
        }
    }

    @Override
    public PagingResponseDTO<PostSimpleDTO> selectPostList(
        PagingRequestWithSearchKeywordDTO pagingRequestDTO) {
        return selectPostList(null, pagingRequestDTO);
    }

    @Override
    public PagingResponseDTO<PostSimpleDTO> selectPostList(
        Integer authorNo,
        PagingRequestWithSearchKeywordDTO pagingRequestDTO) {
        int totalRows = postMapper.selectTotalRows(authorNo, pagingRequestDTO);

        if (totalRows > 0) {
            List<PostSimpleDTO> postList = postMapper.selectPostList(authorNo, pagingRequestDTO);
            return new PagingResponseDTO<>(
                postList,
                pagingRequestDTO.getCurrPage(),
                pagingRequestDTO.getPageSize(),
                totalRows);
        }
        return null;
    }

    private Integer selectPrevTempPostNo(Integer currMemberNo) {
        return postMapper.selectPrevTempPostNo(currMemberNo);
    }

    @Override
    public Integer insTempPost(InsPostDTO insPostDTO) {
        Integer currMemberNo = insPostDTO.getAuthorNo();
        Integer prevTempPostNo = selectPrevTempPostNo(currMemberNo);

        if (prevTempPostNo != null) {
            deletePost(currMemberNo, prevTempPostNo);
        }
        return insPost(insPostDTO);
    }

    @Override
    public Integer selectTempPostNo(int memberNo) {
        Integer prevTempPostNo = selectPrevTempPostNo(memberNo);

        if (prevTempPostNo == null) {
            throw new NoDataFoundException("임시저장된 글이 없습니다.");
        }
        return prevTempPostNo;
    }

    @Override
    public Integer updateTempPost(int memberNo, UpdatePostDTO updatePostDTO) {
        checkIfPostFound(updatePostDTO.getPostNo());
        if (postMapper.retvAuthorNo(updatePostDTO.getPostNo()) != memberNo) {
            throw new AuthenticationException("자신의 임시글만 수정할 수 있습니다.");
        }
        return updatePost(updatePostDTO);
    }

    @Override
    public Integer finalizeTempPost(int memberNo, int postNo) {
        checkIfPostFound(postNo);
        if (postMapper.retvAuthorNo(postNo) != memberNo) {
            throw new AuthenticationException("자신의 임시글만 최종 등록할 수 있습니다.");
        }
        return postMapper.finalizeTempPost(postNo);
    }
}
