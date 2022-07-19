package cn.marak.oss.minio.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用请求
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/commonservice")
public class CommonServiceController {
    /**
     * 生成对象ID
     * 
     * @return
     */
    @GetMapping("/generate-object-id")
    @ResponseStatus(HttpStatus.OK)
    public String generateObjectId() {
        return UUID.randomUUID().toString();
    }    
}
