package cn.springseed.oss.common.util;

import lombok.Getter;

/**
 * OSS请求异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Getter
public class OSSRuntimeException extends RuntimeException {
    public static enum Auth implements ErrorCode {
        UNAUTHORIZED("未登录"),;

        private String description;

        private Auth(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public static enum NotFound implements ErrorCode {
        FILE_NOT_EXIST("文件不存在"),
        METADATA_NOT_EXIST("元数据不存在"),
        ;

        private String description;

        private NotFound(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public static enum BadRequest implements ErrorCode {
        METADATA_NAME_INVALID("无效的元数据名称"),
        METADATA_UPDATED_BY_OWNER("创建者才能修改"),
        FILE_NAME_INVALID("无效的文件名"),
        FILE_UPLOADED_EMPTY("没有上传的文件"),
        FILE_NOT_READABLE("文件不可读"),
        FILE_READ_FAIL("文件读取失败"),
        FILE_SAVE_FAIL("文件存储失败"),
        ;

        private String description;

        private BadRequest(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
    
    private final ErrorCode errorCode;
    private final Throwable throwable;

    private OSSRuntimeException(final ErrorCode code) {
        super();
        this.throwable = null;
        this.errorCode = code;
    }

    private OSSRuntimeException(final ErrorCode code, final Throwable throwable) {
        super(throwable);
        this.throwable = throwable;
        this.errorCode = code;
    }
    
    private OSSRuntimeException(final ErrorCode code, final String message) {
        super(message);
        this.throwable = null;
        this.errorCode = code;
    }

    public String getError() {
        final String rslt = OSSRuntimeException.class.getSimpleName() + "." + errorCode.toString();
        return rslt.toUpperCase();
    }

    public String getDescription() {
        return errorCode.getDescription();
    }

    public static OSSRuntimeException metadataNameInvalid(String message) {
        return new OSSRuntimeException(BadRequest.METADATA_NAME_INVALID, message);
    }

    public static OSSRuntimeException metadataUpdatedByOwner() {
        return new OSSRuntimeException(BadRequest.METADATA_UPDATED_BY_OWNER);
    }

    public static OSSRuntimeException fileNameInvalid(String message) {
        return new OSSRuntimeException(BadRequest.FILE_NAME_INVALID, message);
    }

    public static OSSRuntimeException fileNotReadable() {
        return new OSSRuntimeException(BadRequest.FILE_NOT_READABLE);
    }

    public static OSSRuntimeException fileUploadedEmpty() {
        return new OSSRuntimeException(BadRequest.FILE_UPLOADED_EMPTY);
    }

    public static OSSRuntimeException fileReadFail(final Throwable t) {
        return new OSSRuntimeException(BadRequest.FILE_READ_FAIL);
    }

    public static OSSRuntimeException fileSaveFail(final Throwable t) {
        return new OSSRuntimeException(BadRequest.FILE_SAVE_FAIL);
    }

    public static OSSRuntimeException fileNotExist() {
        return new OSSRuntimeException(NotFound.FILE_NOT_EXIST);
    }

    public static OSSRuntimeException metadataNotExist(String message) {
        return new OSSRuntimeException(NotFound.METADATA_NOT_EXIST, message);
    }

    public static OSSRuntimeException unauthorized() {
        return new OSSRuntimeException(Auth.UNAUTHORIZED);
    }
}
