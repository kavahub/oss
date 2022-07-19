package cn.marak.oss.minio.bean;

import java.time.ZonedDateTime;
import java.util.Map;

import io.minio.StatObjectResponse;
import io.minio.messages.RetentionMode;
import lombok.Data;

/**
 * {@link StatObjectResponse} 包装实体类
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
public class MinioStatObjectResponse {
    private String etag;
    private long size;
    private ZonedDateTime lastModified;
    private RetentionMode retentionMode;
    private ZonedDateTime retentionRetainUntilDate;
    private boolean legalHold;
    private boolean deleteMarker;
    private String versionId;
    private String contentType;
    private Map<String, String> userMetadata;

    public static MinioStatObjectResponse of(StatObjectResponse res) {
        MinioStatObjectResponse rslt = new MinioStatObjectResponse();
        rslt.etag = res.etag();
        rslt.size = res.size();
        rslt.lastModified = res.lastModified();
        rslt.retentionMode = res.retentionMode();
        rslt.retentionRetainUntilDate = res.retentionRetainUntilDate();
        rslt.legalHold = res.legalHold() == null ? null : res.legalHold().status();
        rslt.deleteMarker = res.deleteMarker();
        rslt.versionId = res.versionId();
        rslt.contentType = res.contentType();
        rslt.userMetadata = res.userMetadata();
        return rslt;
    }    
}
