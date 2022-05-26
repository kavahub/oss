package cn.springseed.oss.minio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cn.springseed.oss.OSSMinioApplication;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { OSSMinioApplication.class })
@SpringseedActiveProfiles
public class ContextIntegrationLiveTests {
    @Test
    public void whenLoadApplication_thenSuccess() {

    }    
}
