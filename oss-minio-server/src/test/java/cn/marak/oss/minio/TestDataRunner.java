package cn.marak.oss.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试数据生成
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Component
@Slf4j
public class TestDataRunner implements CommandLineRunner {
    @Autowired
    private MinioClient client;
    
    @Override
    public void run(String... args) throws Exception {
        final BucketExistsArgs beArgs = BucketExistsArgs.builder().bucket(Util.TEST_BUCKET).build();
        if (!client.bucketExists(beArgs)) {
            log.info("Create minio bucket: {}", Util.TEST_BUCKET);
            final MakeBucketArgs mbArgs = MakeBucketArgs.builder().bucket(Util.TEST_BUCKET).build();
            client.makeBucket(mbArgs);
        }
        
    }    
}
