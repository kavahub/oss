package cn.marak.oss.minio.util;

import cn.hutool.core.lang.Assert;
import lombok.experimental.UtilityClass;

/**
 * 路径工具
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class PathUtils {
    private final static int MASK = 255;

    /**
     * 优化目录结构，根据文件hash值，生成两级目录
     * 
     * @param fileName
     * @return
     */
    public String[] generalHashPath(final String fileName) {
        Assert.notBlank(fileName);

        final int hash = fileName.hashCode();
        final int firstDir = hash & MASK;
        final int secondDir = (hash >> 8) & MASK;

        return new String[] { String.format("%02x", firstDir).toUpperCase(),
                String.format("%02x", secondDir).toUpperCase() };
    }
}
