package cn.marak.oss.minio.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cn.marak.oss.minio.bean.MinioUserMetadata;
import cn.marak.oss.minio.service.GetOptService;
import cn.marak.oss.minio.service.StatOptService;
import cn.marak.oss.minio.util.FileNameCounter;
import io.minio.http.Method;

/**
 * get接口
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/getservice")
public class GetServiceController {
    @Autowired
    private GetOptService getOptService;
    @Autowired
    private StatOptService statOptService;

    /**
     * 下载对象
     * 
     * @param bucket
     * @param objectId
     * @param response
     * @throws IOException
     */
    @GetMapping("/object/{bucket}/{objectId}")
    @ResponseStatus(HttpStatus.OK)
    public void getObject(@PathVariable("bucket") String bucket, @PathVariable("objectId") String objectId,
            final HttpServletResponse response) throws IOException {

        // 元数据
        final MinioUserMetadata userMetadata = statOptService.getUserMetadata(bucket, objectId);
        // 文件数据
        final InputStream inputStream = getOptService.getObject(bucket, objectId);

        // 设置响应
        response.setContentType(userMetadata.getContentType());
        response.setHeader("Content-Disposition",
                String.format("inline; filename=\"" + userMetadata.getFileName() + "\""));

        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    /**
     * 批量下载多个对象，压缩后下载
     * 
     * @param bucket
     * @param objectIds
     * @param response
     * @throws IOException
     */
    @GetMapping("/object-zip/{bucket}")
    @ResponseStatus(HttpStatus.OK)
    public void getObjects(@PathVariable("bucket") String bucket,
            @RequestParam(value = "objectIds") Set<String> objectIds,
            final HttpServletResponse response) throws IOException {
        byte[] buffer = new byte[2048];
        final FileNameCounter fileNameCounter = new FileNameCounter();
        // 元数据
        final List<MinioUserMetadata> userMetadatas = statOptService.getUserMetadata(bucket, objectIds);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (final MinioUserMetadata userMetadata : userMetadatas) {
                // 读取文件
                final InputStream fileData = getOptService.getObject(bucket, userMetadata.getObjectId());
                // 避免重复的文件名
                final ZipEntry zipEntry = new ZipEntry(fileNameCounter.convert(userMetadata.getFileName()));
                zos.putNextEntry(zipEntry);

                int bytesRead;
                while ((bytesRead = fileData.read(buffer)) != -1) {
                    zos.write(buffer, 0, bytesRead);
                }

                zos.closeEntry();
            }

            response.setContentType("application/zip");
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Expires", "0");
            final ServletOutputStream sos = response.getOutputStream();
            sos.write(baos.toByteArray());
            sos.flush();
        }
    }

    /**
     * 获取对象预签名URL
     * 
     * @param bucket
     * @param objectId
     * @param method   请求方法，包括：POST, GET, PUT, DELETE
     * @return
     */
    @GetMapping("/presigned-url/{bucket}/{objectId}/{method}")
    @ResponseStatus(HttpStatus.OK)
    public String getPresignedObjectUrl(@PathVariable("bucket") String bucket,
            @PathVariable("objectId") String objectId, @PathVariable("method") Method method) {
        return getOptService.getPresignedObjectUrl(bucket, objectId, method);
    }
}
