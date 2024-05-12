package com.board.service.member;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.common.ReqInfoDTO;
import com.board.dto.member.IdPwDTO;
import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberAuthDTO;
import com.board.dto.member.MemberDetailDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.dto.member.UpdateMemberDetailDTO;
import com.board.enums.MemberAuthStatusEnum;
import com.board.mapper.member.MemberMapper;
import com.board.service.auth.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final AuthService authService;

    @Override
    public Integer insMember(InsMemberDTO insMemberDTO) {
        memberMapper.insMember(insMemberDTO);
        return insMemberDTO.getNo();
    }

    @Override
    public SigninResponseDTO signin(IdPwDTO idPwDTO, ReqInfoDTO reqInfoDTO) {
        Integer memberNo = authService.checkIdPw(idPwDTO);
        memberMapper.expireMemberAuth(memberNo);
        String accessToken = UUID.randomUUID().toString();
        MemberAuthDTO memberAuthDTO = MemberAuthDTO.builder()
                .memberNo(memberNo)
                .accessToken(accessToken)
                .status(MemberAuthStatusEnum.VALID)
                .ipAddr(reqInfoDTO.getIpAddr())
                .userAgent(reqInfoDTO.getUserAgent())
                .build();
        memberMapper.insMemberAuth(memberAuthDTO);
        return new SigninResponseDTO(accessToken);
    }

    @Override
    public void signout(Integer memberNo) {
        memberMapper.expireMemberAuth(memberNo);
    }

    @Override
    public MemberDetailDTO selectMemberDetail(int memberNo) {
        return memberMapper.selectMemberDetail(memberNo);
    }

    @Override
    public void updateMemberDetail(UpdateMemberDetailDTO updateMemberDetailDTO) {
        memberMapper.updateMemberDetail(updateMemberDetailDTO);
    }

    @Override
    public void withdraw(Integer memberNo) {
        memberMapper.expireMemberAuth(memberNo);
        memberMapper.withdraw(memberNo);
    }
}
