package cn.marak.oss.local.storage;

import org.springframework.core.io.Resource;

import lombok.Builder;
import lombok.Data;

/**
 * 文件信息
 *  
 * @author PinWei Wan
 */
@Data
@Builder
public class FileData {
    private Resource content;
    private String name;
    private String contentType;

    public boolean exists() {
        return content == null ? false : content.exists();
    }
}
