package cn.springseed.oss.minio.util;

import cn.springseed.common.typeof.TypeOf;
import cn.springseed.oss.common.util.OSSRuntimeException;
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
    public OSSRuntimeException wapper(Exception ex) {
        return TypeOf.whenTypeOf(ex)
                .is(ErrorResponseException.class).thenReturn(errorResponseException -> {
                    String code = errorResponseException.errorResponse().code();

                    if ("AccessDenied".equalsIgnoreCase(code)) {
                        return OSSRuntimeException.asscessStorageDenied();
                    } else if ("NoSuchBucket".equalsIgnoreCase(code)) {
                        return OSSRuntimeException.bucketNotExist();
                    } else if ("InvalidBucketName".equalsIgnoreCase(code)) {
                        return OSSRuntimeException.bucketNameInvalid();
                    } else if ("NoSuchKey".equalsIgnoreCase(code)) {
                        return OSSRuntimeException.objectIdNotExist();
                    } else if ("NoSuchObject".equalsIgnoreCase(code)) {
                        return OSSRuntimeException.objectNotExist();
                    }

                    return OSSRuntimeException.internalServerError(ex);

                }).orElse(OSSRuntimeException.internalServerError(ex));
    }
}
