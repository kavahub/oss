package cn.marak.oss.local.util;

import lombok.Getter;

/**
 * OSS请求异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Getter
public class OSSLocalRuntimeException extends RuntimeException {
    /**
     * 错误代码接口
     */
    public interface ErrorCode {
        public String getDescription();
    }

    public static enum NotFound implements ErrorCode {
        FILE_NOT_EXIST("文件不存在"),
        METADATA_NOT_EXIST("元数据不存在"),
        OBJECT_NOT_EXIST("对象不存在"),
        OBJECT_ID_NOT_EXIST("对象ID不存在"),        
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

    private OSSLocalRuntimeException(final ErrorCode code) {
        super();
        this.throwable = null;
        this.errorCode = code;
    }

    private OSSLocalRuntimeException(final ErrorCode code, final Throwable throwable) {
        super(throwable);
        this.throwable = throwable;
        this.errorCode = code;
    }
    
    private OSSLocalRuntimeException(final ErrorCode code, final String message) {
        super(message);
        this.throwable = null;
        this.errorCode = code;
    }

    public String getError() {
        final String rslt = OSSLocalRuntimeException.class.getSimpleName() + "." + errorCode.toString();
        return rslt.toUpperCase();
    }

    public String getDescription() {
        return errorCode.getDescription();
    }

    public static OSSLocalRuntimeException metadataNameInvalid(String message) {
        return new OSSLocalRuntimeException(BadRequest.METADATA_NAME_INVALID, message);
    }

    public static OSSLocalRuntimeException metadataUpdatedByOwner() {
        return new OSSLocalRuntimeException(BadRequest.METADATA_UPDATED_BY_OWNER);
    }

    public static OSSLocalRuntimeException fileNameInvalid(String message) {
        return new OSSLocalRuntimeException(BadRequest.FILE_NAME_INVALID, message);
    }

    public static OSSLocalRuntimeException fileNotReadable() {
        return new OSSLocalRuntimeException(BadRequest.FILE_NOT_READABLE);
    }

    public static OSSLocalRuntimeException fileUploadedEmpty() {
        return new OSSLocalRuntimeException(BadRequest.FILE_UPLOADED_EMPTY);
    }

    public static OSSLocalRuntimeException fileReadFail(final Throwable t) {
        return new OSSLocalRuntimeException(BadRequest.FILE_READ_FAIL, t);
    }

    public static OSSLocalRuntimeException fileSaveFail(final Throwable t) {
        return new OSSLocalRuntimeException(BadRequest.FILE_SAVE_FAIL, t);
    }

    public static OSSLocalRuntimeException fileNotExist() {
        return new OSSLocalRuntimeException(NotFound.FILE_NOT_EXIST);
    }

    public static OSSLocalRuntimeException metadataNotExist(String message) {
        return new OSSLocalRuntimeException(NotFound.METADATA_NOT_EXIST, message);
    }

    public static OSSLocalRuntimeException objectNotExist() {
        return new OSSLocalRuntimeException(NotFound.OBJECT_NOT_EXIST);
    } 
    
    public static OSSLocalRuntimeException objectIdNotExist() {
        return new OSSLocalRuntimeException(NotFound.OBJECT_ID_NOT_EXIST);
    }     
}
