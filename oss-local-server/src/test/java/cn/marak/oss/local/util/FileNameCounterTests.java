package cn.marak.oss.local.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class FileNameCounterTests {
 
    @Test
    public void givenEmptyName_thenOK() {
        final FileNameCounter counter = new FileNameCounter();
        assertThat(counter.convert(null)).isEqualTo(FileNameCounter.UNKNOW_FILE_NAME);
        assertThat(counter.convert(null)).isEqualTo(FileNameCounter.UNKNOW_FILE_NAME + " - 1");
        assertThat(counter.convert(null)).isEqualTo(FileNameCounter.UNKNOW_FILE_NAME + " - 2");
    }

    @Test
    public void givenOrginalName_thenOK() {
        final FileNameCounter counter = new FileNameCounter();
        assertThat(counter.convert("我的文件")).isEqualTo("我的文件");
        assertThat(counter.convert("我的文件.txt")).isEqualTo("我的文件.txt");
        assertThat(counter.convert("我的文件.txt")).isEqualTo("我的文件 - 1.txt");
        assertThat(counter.convert("我的文件.txt")).isEqualTo("我的文件 - 2.txt");
        assertThat(counter.convert("我的文件.txt.txt")).isEqualTo("我的文件.txt.txt");
    }

    @Test
    public void givenSameName_thenOK() {
        final FileNameCounter counter = new FileNameCounter();
        assertThat(counter.convert("我的文件.txt")).isEqualTo("我的文件.txt");
        assertThat(counter.convert("我的文件.jpg")).isEqualTo("我的文件.jpg");
    }    
}
