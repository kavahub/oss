package cn.marak.oss.minio.config;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.marak.oss.minio.config.OSSMinioProperties.Connection;
import cn.marak.oss.minio.config.OSSMinioProperties.Server;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

/**
 * 自动配置
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(OSSMinioProperties.class)
public class MinioAutoConfig {
    @Bean
    public MinioClient minioClient(final OSSMinioProperties properties) {
        final Server server = properties.getServer();
        final Connection connection = properties.getConnection();

        MinioClient minioClient;
        if (!configuredProxy()) {
            minioClient = MinioClient.builder()
                    .endpoint(server.getEndpoint(), server.getPort(), server.isSecure())
                    .credentials(server.getAccessKey(), server.getSecretKey())
                    .build();
        } else {
            // http代理
            minioClient = MinioClient.builder()
                    .endpoint(server.getEndpoint(), server.getPort(), server.isSecure())
                    .credentials(server.getAccessKey(), server.getSecretKey())
                    .httpClient(client())
                    .build();
        }
        minioClient.setTimeout(
                connection.getConnectTimeout().toMillis(),
                connection.getWriteTimeout().toMillis(),
                connection.getReadTimeout().toMillis());

        return minioClient;
    }

    /**
     * 是否设置http代理
     * 
     * @return
     */
    private boolean configuredProxy() {
        String httpHost = System.getProperty("http.proxyHost");
        String httpPort = System.getProperty("http.proxyPort");
        return httpHost != null && httpPort != null;
    }

    private OkHttpClient client() {
        String httpHost = System.getProperty("http.proxyHost");
        String httpPort = System.getProperty("http.proxyPort");

        log.info("Http proxy enabled, host: {}, paort: {}", httpHost, httpPort);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (httpHost != null)
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpHost, Integer.parseInt(httpPort))));
        return builder.build();
    }
}
