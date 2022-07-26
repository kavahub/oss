package cn.marak.oss.local;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cn.marak.oss.OSSLocalApplication;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { OSSLocalApplication.class })
@TestProfiles
public class ContextIntegrationTests {
    @Test
    public void whenLoadApplication_thenSuccess() {

    }
}
