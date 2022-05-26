package cn.springseed.oss.minio.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.springseed.oss.common.util.OSSRuntimeException;
import cn.springseed.oss.common.util.SecurityUtils;
import cn.springseed.oss.minio.bean.MinioUserMetadata;
import cn.springseed.oss.minio.service.PutOptService;

/**
 * put接口
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/putservice")
public class PutServiceController {
    @Autowired
    private PutOptService putOptService;

    /**
     * 上传文件
     * 
     * @param bucket
     * @param file
     * @param reqObjectId 对象ID，如果不指定，自动生成
     * @return 对象ID
     */
    @PostMapping("/object/{bucket}")
    @ResponseStatus(HttpStatus.CREATED)
    public String putObject(@PathVariable("bucket") String bucket, @RequestParam("file") MultipartFile file,
            @RequestParam(value = "objectId", required = false) String reqObjectId) {

        if (file.isEmpty()) {
            throw OSSRuntimeException.fileNotExist();
        }

        final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!StringUtils.hasText(fileName)) {
            throw OSSRuntimeException.fileNameInvalid(fileName);
        }

        final String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString).orElse(null);

        final String objectId = reqObjectId == null ? UUID.randomUUID().toString() : reqObjectId;
        final MinioUserMetadata umd = MinioUserMetadata.builder()
                .objectId(objectId)
                .bucket(bucket)
                .contentType(contentType)
                .fileName(fileName)
                .fileSize(file.getSize())
                .createdBy(SecurityUtils.getCurrentUserInfo())
                .build();

        try (InputStream fileData = file.getInputStream()) {
            putOptService.putObject(umd, fileData);
            return objectId;
        } catch (IOException e) {
            throw OSSRuntimeException.internalServerError(e);
        }
    }
}
