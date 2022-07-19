package cn.marak.oss.minio.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 属性对象
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("marak.oss.minio")
public class OSSMinioProperties {
    private Server server = new Server();
    private Connection connection = new Connection();
    private Method method = new Method();

    @Data
    public static class Method {
        /**
         * 单位：秒
         */
        private Duration postExpiry = Duration.ofDays(7);
        /**
         * 单位：秒
         */
        private Duration getExpiry = Duration.ofDays(7);
        /**
         * 单位：秒
         */
        private Duration deleteExpiry = Duration.ofDays(7);
    }

    @Data
    public static class Connection {

        /**
         * Define the connect timeout for the Minio Client.
         */
        private Duration connectTimeout = Duration.ofSeconds(10);

        /**
         * Define the write timeout for the Minio Client.
         */
        private Duration writeTimeout = Duration.ofSeconds(60);

        /**
         * Define the read timeout for the Minio Client.
         */
        private Duration readTimeout = Duration.ofSeconds(10);
    }

    @Data
    public static class Server {

        /**
         * URL for Minio instance. Can include the HTTP scheme. Must include the port.
         * If the port is not provided, then the port of the HTTP is taken.
         */
        private String endpoint;

        private int port;

        /**
         * Access key (login) on Minio instance
         */
        private String accessKey;

        /**
         * Secret key (password) on Minio instance
         */
        private String secretKey;

        /**
         * If the scheme is not provided in {@code url} property, define if the
         * connection is done via HTTP or HTTPS.
         */
        private boolean secure = false;
    }
    
}
