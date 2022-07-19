package cn.marak.oss.local.util;

import lombok.Builder;
import lombok.Data;

/**
 * 接口请求错误响应
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
@Builder
public class ErrorResponse {
    private String error;
    private String description;
    private String message;

    public static ErrorResponse of(OSSLocalRuntimeException ex) {
        return ErrorResponse.builder().error(ex.getError()).message(ex.getMessage()).description(ex.getDescription()).build();
    }

    public static ErrorResponse of(Exception ex) {
        return ErrorResponse.builder().error("INTERNAL_SERVER_ERROR").message(ex.getMessage()).build();
    }
}
