package com.board.service.member;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberAuthDTO;
import com.board.dto.member.MemberHistoryDTO;
import com.board.dto.member.SelectMemberDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.enums.HistoryEnum;
import com.board.enums.MemberAuthStatusEnum;
import com.board.exception.AuthenticationException;
import com.board.exception.ConflictException;
import com.board.mapper.member.MemberMapper;
import com.board.util.PwEncryptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
/**
 * 로그인, 로그아웃, 멤버 가입, 조회, 수정, 삭제 등을 담당하는 Service
 */
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
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

    private void checkDuplicateById(String id) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        selectMemberDTO.setId(id);

        checkDuplicateField(selectMemberDTO);
    }

    private void checkDuplicateByEmail(String email) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        selectMemberDTO.setEmail(email);

        checkDuplicateField(selectMemberDTO);
    }

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

    @Override
    public SigninResponseDTO signin(SigninRequestDTO signinRequestDTO) {
        // 비밀번호 암호화
        signinRequestDTO.setPw(pwEncryptor.encryptPw(signinRequestDTO.getPw()));

        // 아이디-패스워드 매칭여부 확인
        Optional<Integer> memberNoOptional = Optional.ofNullable(memberMapper.signin(signinRequestDTO));
        if (!memberNoOptional.isPresent()) {
            throw new AuthenticationException("아이디와 비밀번호를 확인해주세요.");
        }

        Integer memberNo = memberNoOptional.get();

        // 이미 로그인되어 있는지 확인
        if (memberMapper.isAlreadySignedIn(memberNo)) {
            throw new ConflictException("이미 로그인되어 있는 계정입니다.");
        }

        // 토큰 생성 후 DB 저장
        String accessToken = UUID.randomUUID().toString();

        MemberAuthDTO memberAuthDTO = MemberAuthDTO.builder()
                .memberNo(memberNo)
                .accessToken(accessToken)
                .status(MemberAuthStatusEnum.VALID)
                .createdAt(Instant.now().getEpochSecond())
                .build();

        memberMapper.insMemberAuth(memberAuthDTO);

        // 유저 히스토리 생성 후 DB 저장
        MemberHistoryDTO memberHistoryDTO = MemberHistoryDTO.builder()
                .type(HistoryEnum.LOGIN)
                .timestamp(Instant.now().getEpochSecond())
                .memberNo(memberNo)
                .build();

        memberMapper.insMemberHistory(memberHistoryDTO);

        // 토큰 내려주기
        return new SigninResponseDTO(accessToken);
    }
}
