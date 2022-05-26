package cn.springseed.oss.common.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import cn.springseed.common.typeof.TypeOf;
import cn.springseed.oss.common.util.OSSRuntimeException;
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
    @ExceptionHandler({ OSSRuntimeException.class })
    public ResponseEntity<ErrorResponse> handleOSSRuntimeException(OSSRuntimeException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(ex);
        return TypeOf.whenTypeOf(ex.getErrorCode())
                .is(OSSRuntimeException.NotFound.class)
                .thenReturn(errorcode -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse))
                .orElse((errorcode) -> {
                    if (log.isDebugEnabled()) {
                        log.debug(errorResponse.toString());
                    }

                    if (ex.getThrowable() != null) {
                        // 存在内部异常，记录错误日志
                        log.error("服务异常", ex);
                    }
                    return ResponseEntity.badRequest().body(errorResponse);
                });

    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleInternal(final Exception ex, final WebRequest request) {
        log.error("服务异常", ex);
        return ResponseEntity.internalServerError().body(ErrorResponse.of(ex));
    }
}
