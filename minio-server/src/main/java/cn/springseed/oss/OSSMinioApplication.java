package cn.springseed.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序入口
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@SpringBootApplication
public class OSSMinioApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(OSSMinioApplication.class, args);
    }   
}
