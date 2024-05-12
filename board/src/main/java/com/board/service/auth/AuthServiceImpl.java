package com.board.service.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.member.IdPwDTO;
import com.board.exception.AuthenticationException;
import com.board.exception.NotSignedInException;
import com.board.mapper.auth.AuthMapper;
import com.board.util.PwEncryptor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthMapper authMapper;

    @Override
    public Integer checkAccessTokenValidity(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new AuthenticationException("Access-Token 값이 비어있습니다.");
        }
        Integer memberNo = authMapper.selectMemberNoByAccessToken(accessToken);

        if (memberNo == null) {
            throw new NotSignedInException("로그인하지 않았거나 이미 로그아웃한 토큰입니다.");
        }

        return memberNo;
    }

    @Override
    public Integer checkIdPw(IdPwDTO idPwDTO) {
        idPwDTO.setPw(PwEncryptor.encryptPw(idPwDTO.getPw()));

        Integer memberNo = authMapper.selectMemberNoByIdPW(idPwDTO);
        if (memberNo == null) {
            throw new AuthenticationException("아이디와 비밀번호를 확인해주세요.");
        }
        return memberNo;
    }
}
