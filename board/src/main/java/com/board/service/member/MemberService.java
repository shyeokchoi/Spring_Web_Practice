package com.board.service.member;

import com.board.dto.member.InsMemberDTO;

public interface MemberService {
    /**
     * 회원가입
     * 
     * @param insMemberDTO
     * @return 회원가입된 멤버의 no
     */
    public Integer insMember(InsMemberDTO insMemberDTO);
}
