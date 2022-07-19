package cn.marak.oss.minio.bean;

import java.time.ZonedDateTime;
import java.util.Map;

import io.minio.messages.Item;
import lombok.Data;

/**
 *  {@link Item} 包装实体类
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
public class MinioItem {
    private String etag;
    private String objectName;
    private ZonedDateTime lastModified;
    private String owner;
    private long size;
    private String storageClass;
    private boolean isLatest;
    private String versionId;
    private boolean isDir;
    private boolean isDeleteMarker;
    private String encodingType;
    private Map<String, String> userMetadata;

    public static MinioItem of(Item item) {
        MinioItem rslt = new MinioItem();
        rslt.etag = item.etag();
        rslt.objectName = item.objectName();
        //rslt.lastModified = item.lastModified();
        rslt.size = item.size();
        rslt.storageClass = item.storageClass();
        rslt.owner = item.owner() == null ? null : item.owner().id();
        rslt.userMetadata = item.userMetadata();
        rslt.isLatest = item.isLatest();
        rslt.versionId = item.versionId();
        rslt.isDir = item.isDir();
        rslt.isDeleteMarker = item.isDeleteMarker();
        return rslt;
    }    
}
