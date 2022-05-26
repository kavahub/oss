package cn.springseed.oss.minio.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cn.springseed.oss.minio.bean.MinioStatObjectResponse;
import cn.springseed.oss.minio.service.StatOptService;
import io.minio.StatObjectResponse;

/**
 * stat接口
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/statservice")
public class StatServiceController {
    @Autowired
    private StatOptService statOptService;

    /**
     * 获取单个对象状态
     * 
     * @param bucket
     * @param objectId
     * @return
     */
    @GetMapping("/statobject/{bucket}/{objectId}")
    @ResponseStatus(HttpStatus.OK)
    public MinioStatObjectResponse getStatObjectResponse(@PathVariable("bucket") String bucket,
            @PathVariable("objectId") String objectId) {
                final StatObjectResponse res = statOptService.statObject(bucket, objectId);
        return MinioStatObjectResponse.of(res);
    }

    /**
     * 获取多个对象状态
     * @param bucket
     * @param objectIds
     * @return
     */
    @GetMapping("/statobject/{bucket}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, MinioStatObjectResponse> getStatObjectResponse(@PathVariable("bucket") String bucket,
            @RequestParam(value = "objectIds") Set<String> objectIds) {
        final Map<String, StatObjectResponse> resMap = statOptService.statObject(bucket, objectIds);

        final Map<String, MinioStatObjectResponse> rsltMap = new HashMap<>();
        resMap.forEach(((k, v) -> rsltMap.put(k, MinioStatObjectResponse.of(v))));
        return rsltMap;
    }

    /**
     * 获取单个对象元数据
     * 
     * @param bucket
     * @param objectId
     * @return
     */
    @GetMapping("/usermetadata/{bucket}/{objectId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getUserMetadata(@PathVariable("bucket") String bucket,
            @PathVariable("objectId") String objectId) {
        return statOptService.statObject(bucket, objectId).userMetadata();
    }

    /**
     * 获取多个对象元数据
     * 
     * @param bucket
     * @param objectIds
     * @return
     */
    @GetMapping("/usermetadata/{bucket}")
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getUserMetadata(@PathVariable("bucket") String bucket,
            @RequestParam(value = "objectIds") Set<String> objectIds) {
        final List<Map<String, String>> rslt = new ArrayList<>();

        statOptService.statObject(bucket, objectIds)
                .values()
                .stream()
                .forEach(data -> rslt.add(data.userMetadata()));
        return rslt;
    }    
}
