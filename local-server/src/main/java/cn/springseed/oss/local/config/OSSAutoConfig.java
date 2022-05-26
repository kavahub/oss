package cn.springseed.oss.local.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({OSSProperties.class})
public class OSSAutoConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }  
}
