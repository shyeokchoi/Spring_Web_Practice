package com.board.service.post;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.common.PagingDTO;
import com.board.dto.common.SearchDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.dto.post.UpdatePostDTO;
import com.board.enums.FileInfoParentTypeEnum;
import com.board.exception.AlreadyDeletedException;
import com.board.exception.AuthenticationException;
import com.board.mapper.file.FileMapper;
import com.board.mapper.post.PostMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final FileMapper fileMapper;

    /**
     * 이미 삭제된 게시물인지 확인하는 함수
     * 
     * @param postNo 확인의 대상 post no.
     */
    private void checkIfAlreadyDeleted(Integer postNo) {
        if (postMapper.isAlreadyDeleted(postNo)) {
            throw new AlreadyDeletedException("이미 삭제된 게시물입니다");
        }
    }

    @Override
    public Integer insPost(InsPostDTO insPostDTO) {
        // 새로운 게시물 생성
        postMapper.insPost(insPostDTO);

        // 게시물에서 등록한 file들 전부 상태 업데이트.
        for (Integer fileNo : insPostDTO.getFileNoList()) {
            fileMapper.changeFileStatus(fileNo, insPostDTO.getNo(), FileInfoParentTypeEnum.POST);
        }

        return insPostDTO.getNo();
    }

    @Override
    public SelectPostDetailDTO selectPost(Integer postNo) {
        // 이미 삭제되진 않았는지 확인
        checkIfAlreadyDeleted(postNo);

        // 해당 포스트와 연결된 파일 no 리스트 받아오기
        List<Integer> fileNoList = postMapper.selectFileNoList(postNo);

        // 해당 postNo에 해당하는 게시물 검색
        SelectPostDetailDTO post = postMapper.selectPost(postNo);

        // SelectPostDetailDTO 에 해당 게시글과 연결된 파일 이름들 넣어주기
        post.setFileNoList(fileNoList);
        return post;
    }

    @Override
    public void updatePost(UpdatePostDTO updatePostDTO) {
        // 이미 삭제되진 않았는지 확인
        checkIfAlreadyDeleted(updatePostDTO.getPostNo());

        // 게시물 내용 업데이트
        postMapper.updatePost(updatePostDTO);
    }

    @Override
    public void deletePost(Integer currMemberNo, Integer postNo) {
        // 이미 삭제되진 않았는지 확인
        checkIfAlreadyDeleted(postNo);

        // 자신의 글만 삭제할 수 있음.
        if (postMapper.retvAuthorNo(postNo) != currMemberNo) {
            throw new AuthenticationException("자신의 글만 삭제할 수 있습니다");
        }
        postMapper.deletePost(postNo);
    }

    @Override
    public List<SelectPostListDTO> selectPostList(Integer authorNo, PagingDTO pagingDTO, SearchDTO searchDTO) {
        return postMapper.selectPostList(authorNo, pagingDTO, searchDTO);
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
