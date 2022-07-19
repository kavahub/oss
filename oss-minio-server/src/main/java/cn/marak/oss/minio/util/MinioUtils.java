package cn.marak.oss.minio.util;


import cn.hutool.core.lang.Assert;
import lombok.experimental.UtilityClass;

/**
 * 工具
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class MinioUtils {
     /**
     * 生成Minio的对象ID，包含两级目录结构
     * @param objectId
     * @return
     */
    public String minioId(final String objectId) {
        Assert.notBlank(objectId);

        final String[] paths = PathUtils.generalHashPath(objectId); 
        return new StringBuilder("/").append(String.join("/", paths)).append("/").append(objectId).toString();
    }   
}
