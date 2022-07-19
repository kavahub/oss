package cn.marak.oss.minio.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.marak.oss.minio.bean.MinioUserMetadata;
import cn.marak.oss.minio.util.MinioArgsUtils;
import cn.marak.oss.minio.util.MinioExceptionUtil;
import cn.marak.oss.minio.util.MinioUtils;
import cn.marak.oss.minio.util.OSSMinioRuntimeException;
import cn.marak.oss.minio.util.OSSMinioRuntimeException.ErrorCode;
import cn.marak.oss.minio.util.OSSMinioRuntimeException.NotFound;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * stat操作
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@Service
public class StatOptService {
    @Autowired
    private MinioClient client;

    /**
     * 对象是否存在
     * 
     * @param bucket
     * @param objectId
     * @return
     */
    public boolean exist(final String bucket, final String objectId) {
        boolean found = false;
        try {
            StatObjectResponse response = statObject(bucket, objectId);
            found = MinioUtils.minioId(objectId).equals(response.object());
        }catch(OSSMinioRuntimeException e) { 
            final ErrorCode errorCode = e.getErrorCode();
            if (! (errorCode instanceof NotFound)) {
                throw e;
            }
        }

        return found;
    }

    /**
     * 获取单个对象元数据
     * 
     * @param bucket
     * @param objectId
     * @param consumer
     * @return
     */
    public MinioUserMetadata getUserMetadata(final String bucket, final String objectId,
            final Consumer<StatObjectArgs.Builder> consumer) {
        final StatObjectResponse statObjectResponse = this.statObject(bucket, objectId, consumer);
        return MinioUserMetadata.of(statObjectResponse.userMetadata());
    }

    /**
     * 获取单个对象元数据
     * 
     * @param bucket
     * @param objectId
     * @return
     */
    public MinioUserMetadata getUserMetadata(final String bucket, final String objectId) {
        final StatObjectResponse statObjectResponse = this.statObject(bucket, objectId);
        return MinioUserMetadata.of(statObjectResponse.userMetadata());
    }

    /**
     * 获取多个对象元数据
     * 
     * @param bucket
     * @param objectIds
     * @return
     */
    public List<MinioUserMetadata> getUserMetadata(final String bucket, final Iterable<String> objectIds) {
        return StreamSupport.stream(objectIds.spliterator(), false)
                .map(objectId -> getUserMetadata(bucket, objectId))
                .collect(Collectors.toList());
    }

    /**
     * 获取单个对象状态
     * 
     * @param bucket
     * @param objectId
     * @param consumer
     * @return
     */
    public StatObjectResponse statObject(final String bucket, final String objectId,
            final Consumer<StatObjectArgs.Builder> consumer) {
        final String minioId = MinioUtils.minioId(objectId);
        try {
            final StatObjectArgs.Builder builder = StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(minioId);
            if (consumer != null) {
                consumer.accept(builder);
            }

            final StatObjectArgs args = builder.build();
            if (log.isDebugEnabled()) {
                log.debug("StatObjectArgs={}", MinioArgsUtils.printStatObjectArgs(args));
            }
            return client.statObject(args);
        } catch (Exception e) {
            throw MinioExceptionUtil.wapper(e);
        }
    }

    /**
     * 获取单个对象状态
     * 
     * @param bucket
     * @param objectId
     * @return
     */
    public StatObjectResponse statObject(final String bucket, final String objectId) {
        return statObject(bucket, objectId, null);
    }

    /**
     * 获取多个对象状态
     * 
     * @param bucket
     * @param objectIds
     * @return
     */
    public Map<String, StatObjectResponse> statObject(final String bucket,
            final Iterable<String> objectIds) {
        return StreamSupport.stream(objectIds.spliterator(), false)
                .map(objectId -> new HashMap.SimpleEntry<>(objectId, statObject(bucket, objectId)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }    
}
