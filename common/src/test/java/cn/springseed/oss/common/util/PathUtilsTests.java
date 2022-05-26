package cn.springseed.oss.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class PathUtilsTests {
    @Test
    public void givenFileName_whenGetFilePath_thenOk() {
        assertThat(PathUtils.generalHashPath("我的文件.txt")).contains("A4","62");
        assertThat(PathUtils.generalHashPath("我的文件")).contains("22", "98");
    }

    /**
     * 性能及文件分布情况
     */
    @Test
    public void givenOneMillionFileName_whenGetFilePath_thenCount() {
        final int OneMillion = 1_000_000;

        final Map<String, Integer> count = new HashMap<>();
        for(int i = 0; i < OneMillion; i++) {
            final String fileName = UUID.randomUUID().toString();
            final String key = String.join(",", PathUtils.generalHashPath(fileName));
            Integer value = count.get(key);
            if (value == null) {
                value = 0;
            }

            count.put(key, value + 1);
        }

        System.out.println("目录数：" + count.size());
        System.out.println("文件数前1000：" + count.values().stream().sorted(Comparator.reverseOrder()).limit(1000).collect(Collectors.toList()));
    }
}
