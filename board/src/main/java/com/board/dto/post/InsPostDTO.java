package com.board.dto.post;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.board.enums.PostStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "글 작성용 DTO")
@Data
public class InsPostDTO {
    @JsonIgnore
    private int no;

    @Size(min = 0, max = 150, message = "제목은 150글자 이하입니다.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Schema(description = "제목", example = "예시 제목")
    private String title;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    @Schema(description = "내용", example = "예시 내용")
    private String content;

    @NotNull(message = "fileInfoNoList는 null일 수 없습니다.")
    @Schema(description = "업로드할 파일 no 리스트", example = "[1, 2, 3]")
    private List<Integer> fileInfoNoList;

    @JsonIgnore
    private Integer authorNo;

    @JsonIgnore
    private PostStatusEnum status;
}
