package cn.marak.oss.minio.controller;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.util.IdUtil;
import cn.marak.oss.minio.bean.MinioUserMetadata;
import cn.marak.oss.minio.service.PutOptService;
import cn.marak.oss.minio.util.OSSMinioRuntimeException;
import cn.marak.oss.minio.util.SecurityUtils;

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
     * @throws IOException
     */
    @PostMapping("/object/{bucket}")
    @ResponseStatus(HttpStatus.CREATED)
    public String putObject(@PathVariable("bucket") String bucket, @RequestParam("file") MultipartFile file,
            @RequestParam(value = "objectId", required = false) String reqObjectId) throws IOException {

        if (file.isEmpty()) {
            throw OSSMinioRuntimeException.fileNotExist();
        }

        final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!StringUtils.hasText(fileName)) {
            throw OSSMinioRuntimeException.fileNameInvalid(fileName);
        }

        final Tika tika = new Tika();
        final String mediaType = tika.detect(file.getInputStream());

        final String objectId = reqObjectId == null ? IdUtil.getSnowflake().nextIdStr() : reqObjectId;
        final MinioUserMetadata umd = MinioUserMetadata.builder()
                .objectId(objectId)
                .bucket(bucket)
                .contentType(mediaType)
                .fileName(fileName)
                .fileSize(file.getSize())
                .createdBy(SecurityUtils.getCurrentUserInfo())
                .build();

        putOptService.putObject(umd, file.getInputStream());
        return objectId;
    }
}
