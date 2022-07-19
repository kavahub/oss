package cn.marak.oss.local.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 属性配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("marak.oss")
public class OSSProperties {
    private Path uploadRootPath = Paths.get(System.getProperty("user.home"), ".marak", "oss");
}
