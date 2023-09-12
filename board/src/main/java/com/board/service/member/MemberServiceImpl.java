package com.board.service.member;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberAuthDTO;
import com.board.dto.member.MemberHistoryDTO;
import com.board.dto.member.SelectMemberDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.enums.HistoryEnum;
import com.board.enums.MemberAuthStatusEnum;
import com.board.exception.ConflictException;
import com.board.mapper.member.MemberMapper;
import com.board.service.auth.AuthService;
import com.board.util.PwEncryptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final AuthService authService;
    private final PwEncryptor pwEncryptor;

    @Override
    public Integer insMember(InsMemberDTO insMemberDTO) {
        // 아이디, 이메일 중복 여부를 확인하고 중복이면 예외 발생
        checkDuplicateById(insMemberDTO.getId());
        checkDuplicateByEmail(insMemberDTO.getEmail());
        // 비밀번호 암호화 후 DTO에 대체해서 넣어줌
        insMemberDTO.setPw(pwEncryptor.encryptPw(insMemberDTO.getPw()));

        // DB에 저장
        memberMapper.insMember(insMemberDTO);

        return insMemberDTO.getNo();
    }

    /**
     * 아이디 중복여부 확인
     * 
     * @param id
     */
    private void checkDuplicateById(String id) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        selectMemberDTO.setId(id);

        checkDuplicateField(selectMemberDTO);
    }

    /**
     * 이메일 중복여부 확인
     * 
     * @param email
     */
    private void checkDuplicateByEmail(String email) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        selectMemberDTO.setEmail(email);

        checkDuplicateField(selectMemberDTO);
    }

    /**
     * SelectMemberDTO로
     * 
     * @param selectMemberDTO
     */
    private void checkDuplicateField(SelectMemberDTO selectMemberDTO) {
        String fieldToCheck;

        if (selectMemberDTO.getId() != null) {
            fieldToCheck = "id";
        } else if (selectMemberDTO.getEmail() != null) {
            fieldToCheck = "email";
        } else {
            throw new IllegalArgumentException("Wrong argument: SelectMemberDTO의 필드가 모두 null 입니다");
        }

        Optional.ofNullable(memberMapper.selectOne(selectMemberDTO))
                .ifPresent(member -> {
                    throw new ConflictException(fieldToCheck);
                });
    }

    private MemberAuthDTO buildMemberAuth(Integer memberNo, String accessToken,
            MemberAuthStatusEnum memberAuthStatusEnum) {
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        String userAgent = servletRequest.getHeader("User-Agent");
        String ipAddr = servletRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddr == null) {
            ipAddr = servletRequest.getRemoteAddr();
        }

        return MemberAuthDTO.builder()
                .memberNo(memberNo)
                .accessToken(accessToken)
                .status(memberAuthStatusEnum)
                .ipAddr(ipAddr)
                .userAgent(userAgent)
                .build();
    }

    private MemberHistoryDTO buildMemberHistory(Integer memberNo, HistoryEnum historyEnum) {

        return MemberHistoryDTO.builder()
                .type(historyEnum)
                .memberNo(memberNo)
                .build();
    }

    @Override
    public SigninResponseDTO signin(SigninRequestDTO signinRequestDTO) {
        Integer memberNo = authService.checkIdPw(signinRequestDTO);

        // 이미 로그인되어 있는지 확인
        if (memberMapper.isAlreadySignedIn(memberNo)) {
            throw new ConflictException("이미 로그인되어 있는 계정입니다.");
        }

        // 토큰 생성 후 DB 저장
        String accessToken = UUID.randomUUID().toString();

        MemberAuthDTO memberAuthDTO = buildMemberAuth(memberNo, accessToken, MemberAuthStatusEnum.VALID);

        memberMapper.insMemberAuth(memberAuthDTO);

        // 유저 히스토리 생성 후 DB 저장
        MemberHistoryDTO memberHistoryDTO = buildMemberHistory(memberNo, HistoryEnum.SIGNIN);

        memberMapper.insMemberHistory(memberHistoryDTO);

        // 토큰 내려주기
        return new SigninResponseDTO(accessToken);
    }

    @Override
    public void signout() {
        // 로그아웃하려고 하는 멤버의 access token 얻기
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        String accessToken = authService.retvAccessTokenFromRequest(servletRequest);

        // 토큰을 활용해 로그아웃하려고 하는 멤버의 member no 얻기
        Integer memberNo = authService.retvMemberNoFromAccessToken(accessToken);

        // access token으로 member_auth의 status를 expire
        memberMapper.expireMemberAuth(accessToken);

        // 유저 히스토리 생성 후 DB 저장
        MemberHistoryDTO memberHistoryDTO = buildMemberHistory(memberNo, HistoryEnum.SIGNOUT);

        memberMapper.insMemberHistory(memberHistoryDTO);
    }

    @Override
    public void withdraw() {
        // 탈퇴하려는 멤버의 access token 얻기
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        String accessToken = authService.retvAccessTokenFromRequest(servletRequest);

        // 토큰을 활용해 로그아웃하려고 하는 멤버의 member no 얻기
        Integer memberNo = authService.retvMemberNoFromAccessToken(accessToken);

        // access token으로 member_auth의 status를 expire
        memberMapper.expireMemberAuth(accessToken);

        // 회원 탈퇴
        memberMapper.withdraw(memberNo);
    }
}
