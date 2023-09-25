package com.board.dto.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.board.enums.MemberStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 회원가입을 위한 DTO
 */
@Data
@Schema(description = "회원가입용 DTO")
public class InsMemberDTO {
    @JsonIgnore
    private int no;

    @Pattern(regexp = "^[\uAC00-\uD7A3]*$", message = "이름은 한글만 허용합니다.")
    @Size(max = 20, message = "이름은 20자 이하여야 합니다.")
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Schema(description = "회원 이름", example = "홍길동")
    private String name;

    @Size(max = 20, message = "아이디는 20자 이하여야 합니다.")
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Schema(description = "회원 아이디", example = "hongildong")
    private String id;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*]).{7,}$", message = "비밀번호는 영문, 숫자, 특수문자 조합으로 7자 이상입니다.")
    @Size(max = 20, message = "비밀번호는 20자 이하여야 합니다.")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Schema(description = "비밀번호", example = "examplepw1234!")
    private String pw;

    @Email(message = "이메일 형식이 틀렸습니다.")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Schema(description = "이메일", example = "hongildong@gmail.com")
    private String email;

    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 틀렸습니다.")
    @NotBlank(message = "전화번호는 공백일 수 없습니다.")
    @Schema(description = "전화번호", example = "01012341234")
    private String phone;

    @JsonIgnore
    private MemberStatusEnum status;
}