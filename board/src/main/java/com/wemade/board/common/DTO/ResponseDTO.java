package com.wemade.board.common.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class ResponseDTO<T> extends BaseResponseDTO {

    @Schema(description = "data")
    private T data;

    public ResponseDTO(int result, String resultString, T data) {
        this.setResult(result);
        this.setResultString(resultString);
        this.setData(data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}