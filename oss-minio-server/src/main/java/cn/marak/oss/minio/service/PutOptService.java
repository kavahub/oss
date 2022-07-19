package cn.marak.oss.minio.service;

import java.io.InputStream;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.marak.oss.minio.bean.MinioUserMetadata;
import cn.marak.oss.minio.util.MinioArgsUtils;
import cn.marak.oss.minio.util.MinioExceptionUtil;
import cn.marak.oss.minio.util.MinioUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;

/**
 * put操作
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@Service
public class PutOptService {
    @Autowired
    private MinioClient client;

    /**
     * 上传一个文件流
     * 
     * @param bucket
     * @param objectId
     * @param file        文件流
     * @param contentType
     * @param consumer
     */
    public void putObject(final MinioUserMetadata metadata, final InputStream file, final Consumer<PutObjectArgs.Builder> consumer) {
        final String minioId = MinioUtils.minioId(metadata.getObjectId());
        metadata.setMinioId(minioId);

        try {
            final PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket(metadata.getBucket())
                    .object(metadata.getMinioId())
                    .userMetadata(metadata.toMap())
                    .stream(file, file.available(), -1)
                    .contentType(metadata.getContentType());
            if (consumer != null) {
                consumer.accept(builder);
            }

            final PutObjectArgs args = builder.build();
            if (log.isDebugEnabled()) {
                log.debug("PutObjectArgs={}", MinioArgsUtils.printPutObjectArgs(args));
            }
            client.putObject(args);
        } catch (Exception e) {
            throw MinioExceptionUtil.wapper(e);
        }
    }

    /**
     * 上传一个文件流
     * @param metadata
     * @param file
     */
    public void putObject(final MinioUserMetadata metadata, final InputStream file) {
        this.putObject(metadata, file, null);
    }   
}
