package com.board.dto.file;

import com.board.enums.FileInfoParentTypeEnum;
import com.board.enums.FileStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
/**
 * file info를 추가할 때 사용하는 DTO
 */
public class InsFileInfoDTO {
    // PK.
    @JsonIgnore
    private Integer no;

    // 유저가 업로드할 때 사용한 파일 이름
    @Schema(description = "유저가 업로드할 때 사용한 파일 이름")
    private String originName;

    // 서버 파일 시스템에 저장할 때 사용할 이름
    @JsonIgnore
    private String saveName;

    // 서버 파일 시스템에 저장될 때 경로
    @JsonIgnore
    private String savePath;

    // 파일 확장자
    @JsonIgnore
    private String extension;

    // 파일 byte단위 크기
    @JsonIgnore
    private Long size;

    // 이 파일의 부모가 되는 데이터의 no.
    @JsonIgnore
    private Integer parentNo;

    // 이 파일의 부모의 타입. 예를 들어, 게시물, 댓글, 등등..
    @JsonIgnore
    private FileInfoParentTypeEnum parentType;

    // 파일 업로드한 사람의 no.
    @JsonIgnore
    private Integer authorNo;

    // 파일의 상태. 임시 파일인지 정식 파일인지
    @JsonIgnore
    private FileStatusEnum status;
}
