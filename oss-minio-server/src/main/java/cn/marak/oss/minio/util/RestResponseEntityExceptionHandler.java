package cn.marak.oss.minio.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import cn.marak.oss.minio.util.OSSMinioRuntimeException.ErrorCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求异常处理器
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler({ OSSMinioRuntimeException.class })
    public ResponseEntity<ErrorResponse> handleOSSMinioRuntimeException(OSSMinioRuntimeException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(ex);
        final ErrorCode errorCode = ex.getErrorCode();
        
        if (OSSMinioRuntimeException.NotFound.class.isAssignableFrom(errorCode.getClass())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ex));
        } else {
            if (log.isDebugEnabled()) {
                log.debug(errorResponse.toString());
            }

            if (ex.getThrowable() != null) {
                // 存在内部异常，记录错误日志
                log.error("服务异常", ex);
            }
            return ResponseEntity.badRequest().body(errorResponse); 
        }
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleException(final Exception ex, final WebRequest request) {
        log.error("服务异常", ex);
        return ResponseEntity.internalServerError().body(ErrorResponse.of(ex));
    }
}
