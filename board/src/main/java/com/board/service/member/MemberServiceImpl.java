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
import com.board.enums.MemberStatusEnum;
import com.board.mapper.member.MemberMapper;
import com.board.service.auth.AuthService;
import com.board.util.PwEncryptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final AuthService authService;

    @Override
    public Integer insMember(InsMemberDTO insMemberDTO) {
        // 비밀번호 암호화 후 DTO에 대체해서 넣어줌
        insMemberDTO.setPw(PwEncryptor.encryptPw(insMemberDTO.getPw()));

        // status 설정
        insMemberDTO.setStatus(MemberStatusEnum.NORMAL);

        // DB에 저장
        memberMapper.insMember(insMemberDTO);

        return insMemberDTO.getNo();
    }

    @Override
    public SigninResponseDTO signin(IdPwDTO idPwDTO, ReqInfoDTO reqInfoDTO) {
        Integer memberNo = authService.checkIdPw(idPwDTO);

        // 기존에 로그인이 되어 있었다면, 기존 토큰들 expire.
        memberMapper.expireMemberAuth(memberNo);

        // 토큰 생성 후 DB 저장
        String accessToken = UUID.randomUUID().toString();

        MemberAuthDTO memberAuthDTO = MemberAuthDTO.builder()
                .memberNo(memberNo)
                .accessToken(accessToken)
                .status(MemberAuthStatusEnum.VALID)
                .ipAddr(reqInfoDTO.getIpAddr())
                .userAgent(reqInfoDTO.getUserAgent())
                .build();

        memberMapper.insMemberAuth(memberAuthDTO);

        // 토큰 내려주기
        return new SigninResponseDTO(accessToken);
    }

    @Override
    public void signout(Integer memberNo) {
        // 모든 access token expire 하기
        memberMapper.expireMemberAuth(memberNo);
    }

    @Override
    public MemberDetailDTO selectMemberDetail(int memberNo) {
        // 자기 자신의 정보 return
        return memberMapper.selectMemberDetail(memberNo);
    }

    @Override
    public void updateMemberDetail(int memberNo, UpdateMemberDetailDTO updateMemberDetailDTO) {
        // 멤버정보 수정을 위해 updateMemberDetailDTO에 member no, pw 세팅
        updateMemberDetailDTO.setNo(memberNo);
        updateMemberDetailDTO.setPw(PwEncryptor.encryptPw(updateMemberDetailDTO.getPw()));

        // 자기 자신의 정보 update
        memberMapper.updateMemberDetail(updateMemberDetailDTO);
    }

    @Override
    public void withdraw(Integer memberNo) {
        // access token으로 member_auth의 status를 expire
        memberMapper.expireMemberAuth(memberNo);

        // 회원 탈퇴
        memberMapper.withdraw(memberNo);
    }

}
