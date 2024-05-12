package com.board.service.member;

import com.board.dto.common.ReqInfoDTO;
import com.board.dto.member.IdPwDTO;
import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberDetailDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.dto.member.UpdateMemberDetailDTO;

public interface MemberService {
    public Integer insMember(InsMemberDTO insMemberDTO);

    public SigninResponseDTO signin(IdPwDTO idPwDTO, ReqInfoDTO reqInfoDTO);

    public void signout(Integer memberNo);

    public MemberDetailDTO selectMemberDetail(int memberNo);

    public void updateMemberDetail(UpdateMemberDetailDTO updateMemberDetailDTO);

    public void withdraw(Integer memberNo);
}
