package cn.marak.oss.minio.util;

import io.minio.errors.ErrorResponseException;
import lombok.experimental.UtilityClass;

/**
 * 异常工具
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class MinioExceptionUtil {
    /**
     * 包装Minio库异常
     * 
     * @param ex
     * @return
     */
    public RuntimeException wapper(Exception ex) {
        RuntimeException rslt = new RuntimeException(ex);
        if (ErrorResponseException.class.isAssignableFrom(ex.getClass())) {
            String code = ((ErrorResponseException) ex).errorResponse().code();
            switch (code) {
                case "AccessDenied":
                    rslt = OSSMinioRuntimeException.asscessStorageDenied();
                    break;
                case "NoSuchBucket":
                    rslt = OSSMinioRuntimeException.bucketNotExist();
                    break;
                case "InvalidBucketName":
                    rslt = OSSMinioRuntimeException.bucketNameInvalid();
                    break;
                case "NoSuchKey":
                    rslt = OSSMinioRuntimeException.objectIdNotExist();
                    break;
                case "NoSuchObject":
                    rslt = OSSMinioRuntimeException.objectNotExist();
                    break;
                default:
                    rslt = new RuntimeException(ex);
            }
        }

        return rslt;
    }
}
