package cn.marak.oss.minio.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 文件名称计数，相同名称的使用序号
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class FileNameCounter {
    public final static String UNKNOW_FILE_NAME = "未知文件名";
    private Map<String, Integer> nameCache = new HashMap<>();

    public String convert(final String fileName) {
        String tmpFileName = fileName;
        if (!StringUtils.hasText(tmpFileName)) {
            tmpFileName = UNKNOW_FILE_NAME;
        }

        Integer value = nameCache.get(tmpFileName);
        if (value == null) {
            value = -1;
        }

        value += 1;
        nameCache.put(tmpFileName, value);

        if (value.intValue() > 0) {
            // 修改文件名称，增加计数到文件名
            final int index = tmpFileName.indexOf(".");
            if (index < 0) {
                return tmpFileName + " - " + value;
            }

            return String.format("%s - %s%s",
                    tmpFileName.substring(0, index),
                    value,
                    tmpFileName.substring(index));
        }
        return tmpFileName;
    }    
}
