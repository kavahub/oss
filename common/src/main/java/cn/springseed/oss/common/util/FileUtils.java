package cn.springseed.oss.common.util;

import java.util.Optional;

import lombok.experimental.UtilityClass;

/**
 * 文件工具
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class FileUtils {
    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                       .filter(f -> f.contains("."))
                      .map(f -> f.substring(filename.lastIndexOf(".") + 1)).orElse(null);
    }
}
