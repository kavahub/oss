package cn.marak.oss.minio.util;

import lombok.Getter;

/**
 * OSS请求异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Getter
public class OSSMinioRuntimeException extends RuntimeException {
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
        FILE_NAME_INVALID("无效的文件名"),
        FILE_UPLOADED_EMPTY("没有上传的文件"),
        FILE_NOT_READABLE("文件不可读"),
        FILE_READ_FAIL("文件读取失败"),
        FILE_SAVE_FAIL("文件存储失败"),
        BUCKET_NOT_EXIST("桶不存在"),
        BUCKET_NAME_INVALID("桶名无效"),
        ACCESS_STORAGE_DENIED("禁止访问存储服务");

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

    private OSSMinioRuntimeException(final ErrorCode code) {
        super();
        this.throwable = null;
        this.errorCode = code;
    }

    private OSSMinioRuntimeException(final ErrorCode code, final Throwable throwable) {
        super(throwable);
        this.throwable = throwable;
        this.errorCode = code;
    }
    
    private OSSMinioRuntimeException(final ErrorCode code, final String message) {
        super(message);
        this.throwable = null;
        this.errorCode = code;
    }

    public String getError() {
        final String rslt = OSSMinioRuntimeException.class.getSimpleName() + "." + errorCode.toString();
        return rslt.toUpperCase();
    }

    public String getDescription() {
        return errorCode.getDescription();
    }

    public static OSSMinioRuntimeException fileNameInvalid(String message) {
        return new OSSMinioRuntimeException(BadRequest.FILE_NAME_INVALID, message);
    }

    public static OSSMinioRuntimeException fileNotReadable() {
        return new OSSMinioRuntimeException(BadRequest.FILE_NOT_READABLE);
    }

    public static OSSMinioRuntimeException fileUploadedEmpty() {
        return new OSSMinioRuntimeException(BadRequest.FILE_UPLOADED_EMPTY);
    }

    public static OSSMinioRuntimeException fileReadFail(final Throwable t) {
        return new OSSMinioRuntimeException(BadRequest.FILE_READ_FAIL, t);
    }

    public static OSSMinioRuntimeException fileSaveFail(final Throwable t) {
        return new OSSMinioRuntimeException(BadRequest.FILE_SAVE_FAIL, t);
    }

    public static OSSMinioRuntimeException fileNotExist() {
        return new OSSMinioRuntimeException(NotFound.FILE_NOT_EXIST);
    }

    public static OSSMinioRuntimeException metadataNotExist(String message) {
        return new OSSMinioRuntimeException(NotFound.METADATA_NOT_EXIST, message);
    }

    public static OSSMinioRuntimeException bucketNotExist() {
        return new OSSMinioRuntimeException(BadRequest.BUCKET_NOT_EXIST);
    }

    public static OSSMinioRuntimeException bucketNameInvalid() {
        return new OSSMinioRuntimeException(BadRequest.BUCKET_NAME_INVALID);
    }

    public static OSSMinioRuntimeException objectNotExist() {
        return new OSSMinioRuntimeException(NotFound.OBJECT_NOT_EXIST);
    } 
    
    public static OSSMinioRuntimeException objectIdNotExist() {
        return new OSSMinioRuntimeException(NotFound.OBJECT_ID_NOT_EXIST);
    }

    public static OSSMinioRuntimeException asscessStorageDenied() {
        return new OSSMinioRuntimeException(BadRequest.ACCESS_STORAGE_DENIED);
    } 
    
}
