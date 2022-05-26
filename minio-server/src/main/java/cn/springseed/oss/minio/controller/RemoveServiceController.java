package cn.springseed.oss.minio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cn.springseed.oss.minio.service.RemoveOptService;
import cn.springseed.oss.minio.service.StatOptService;

/**
 * Remove接口
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/removeservice")
public class RemoveServiceController {
    @Autowired
    private RemoveOptService removeOptService;
    @Autowired
    private StatOptService statOptService;
    
    /**
     * 删除对象
     * 
     * @param bucket
     * @param objectId
     */
    @DeleteMapping("/object/{bucket}/{objectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeObject(@PathVariable("bucket") String bucket, @PathVariable("objectId") String objectId) {
        if (statOptService.exist(bucket, objectId)) {
            removeOptService.removeObject(bucket, objectId);
        }
    }    
}
