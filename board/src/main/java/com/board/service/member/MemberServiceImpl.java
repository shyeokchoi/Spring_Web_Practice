package com.board.service.member;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.SelectMemberDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.exception.ConflictException;
import com.board.mapper.member.MemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
/**
 * 로그인, 로그아웃, 멤버 가입, 조회, 수정, 삭제 등을 담당하는 Service
 */
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public Integer insMember(InsMemberDTO insMemberDTO) {
        // 아이디, 이메일 중복 여부를 확인하고 중복이면 예외 발생
        checkDuplicateId(insMemberDTO);
        checkDuplicateEmail(insMemberDTO);

        String orgPw = insMemberDTO.getPw();
        String saltedPw = saltPw(orgPw);
        String encryptedPw = computeSha256(saltedPw);
        insMemberDTO.setPw(encryptedPw);

        memberMapper.insMember(insMemberDTO);

        return insMemberDTO.getNo();
    }

    /**
     * id 중복 여부 확인
     * 
     * @param insMemberDTO
     */
    private void checkDuplicateId(InsMemberDTO insMemberDTO) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        selectMemberDTO.setId(insMemberDTO.getId());
        Optional.ofNullable(memberMapper.selectOne(selectMemberDTO))
                .ifPresent(member -> {
                    throw new ConflictException("id");
                });
    }

    /**
     * email 중복 여부 확인
     * 
     * @param insMemberDTO
     */
    private void checkDuplicateEmail(InsMemberDTO insMemberDTO) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        selectMemberDTO.setEmail(insMemberDTO.getEmail());
        Optional.ofNullable(memberMapper.selectOne(selectMemberDTO))
                .ifPresent(member -> {
                    throw new ConflictException("email");
                });
    }

    /**
     * sha-256 알고리즘으로 비밀번호 암호화
     * 
     * @param pw
     * @return 암호화된 비밀번호
     */
    private String computeSha256(String pw) {
        try {
            // MessageDigest 객체 생성 (SHA-256 알고리즘 사용)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 문자열을 바이트 배열로 변환
            byte[] encodedHash = digest.digest(pw.getBytes());

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 해시된 비밀번호를 문자열로 반환
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Internal error: password 암호화 실패");
        }

    }

    /**
     * pw salting
     * 
     * @param pw
     * @return salting 완료된 pw
     */
    private String saltPw(String pw) {
        return "wemade" + pw + "12345";
    }

    @Override
    public SigninResponseDTO signin(SigninRequestDTO signinRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signin'");
    }
}
