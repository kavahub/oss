package cn.springseed.oss.minio.bean;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import lombok.Builder;
import lombok.Data;

/**
 * 用户元数据实体
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
@Builder
public class MinioUserMetadata {
    public final static String FIELD_OBJECT_ID = "objectid";
    public final static String FIELD_BUCKET = "bucket";
    public final static String FIELD_MINIO_ID = "minioid";
    public final static String FIELD_FILE_NAME = "filename";
    public final static String FIELD_FILE_SIZE = "filesize";
    public final static String FIELD_CREATED_BY = "createdby";
    public final static String FIELD_CREATED_ON = "createdon";
    public final static String FIELD_CONTENT_TYPE = "contenttype";

    private String objectId;
    private String bucket;
    private String minioId;
    private String fileName;
    private long fileSize;
    private String createdBy;
    private ZonedDateTime createdOn;
    private String contentType;

    public static MinioUserMetadata of(Map<String, String> values) {
        return MinioUserMetadata.builder()
                .objectId(values.get(FIELD_OBJECT_ID))
                .bucket(values.get(FIELD_BUCKET))
                .minioId(values.get(FIELD_MINIO_ID))
                .fileName(values.get(FIELD_FILE_NAME))
                .fileSize(Long.parseLong(values.get(FIELD_FILE_SIZE)))
                .createdBy(values.get(FIELD_CREATED_BY))
                .createdOn(ZonedDateTime.parse(values.get(FIELD_CREATED_ON)))
                .contentType(values.get(FIELD_CONTENT_TYPE))
                .build();
    }

    public Map<String, String> toMap() {
        if (!(StringUtils.hasText(this.objectId) && StringUtils.hasText(this.bucket)
                && StringUtils.hasText(this.minioId) && StringUtils.hasText(this.fileName))) {
            throw new IllegalArgumentException("objectId, bucket, minioId, fileName are required");
        }

        if (this.createdOn == null) {
            this.createdOn = ZonedDateTime.now();
        }
        
        final Map<String, String> rslt = new HashMap<String, String>();
        rslt.put(FIELD_OBJECT_ID, this.objectId);
        rslt.put(FIELD_BUCKET, this.bucket);
        rslt.put(FIELD_MINIO_ID, this.minioId);
        rslt.put(FIELD_FILE_NAME, this.fileName);
        rslt.put(FIELD_FILE_SIZE, String.valueOf(this.fileSize));
        rslt.put(FIELD_CREATED_BY, this.createdBy);
        rslt.put(FIELD_CREATED_ON, this.createdOn.toString());
        rslt.put(FIELD_CONTENT_TYPE, this.contentType);
        return rslt;
    }
}
