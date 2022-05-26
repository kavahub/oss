package cn.springseed.oss.minio.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import cn.springseed.oss.OSSMinioApplication;
import cn.springseed.oss.minio.SpringseedActiveProfiles;
import cn.springseed.oss.minio.Util;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSMinioApplication.class)
@SpringseedActiveProfiles
@AutoConfigureMockMvc
@Slf4j
public class GetServiceControllerLiveTests {
    private final static String BASE_URL = "/v1/getservice";
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenObjectId_whenGetObject_thenContentOK() throws Exception {
        final String objectId = Util.uploadFile(mvc);

        final MvcResult result = mvc
                .perform(get(BASE_URL + "/object/{bucket}/{objectId}", Util.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // 文件内容测试
        final String fileData = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
        assertThat(fileData).isEqualTo("你好");
    }

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenWrongObjectId_whenGetObject_thenBadRequest() throws Exception {
        mvc.perform(get(BASE_URL + "/object/{bucket}/{objectId}", Util.TEST_BUCKET, "wrongobjectid"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", equalTo("OSSRUNTIMEEXCEPTION.OBJECT_ID_NOT_EXIST")));
    }

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenObjectIds_whenGetObjects_thenOk() throws Exception {
        final String objectId1 = Util.uploadFile(mvc);
        final String objectId2 = Util.uploadFile(mvc);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("objectIds", Arrays.asList(objectId1, objectId2));

        final MvcResult result = mvc.perform(get(BASE_URL + "/object-zip/{bucket}", Util.TEST_BUCKET).params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // 保存到target
        final Path zipPath = Paths.get("target", "data-zip.zip");
        final byte[] zipData = result.getResponse().getContentAsByteArray();
        Files.copy(new ByteArrayInputStream(zipData), zipPath, StandardCopyOption.REPLACE_EXISTING);

        // try (final ZipFile zip = new ZipFile(zipPath.toFile())) {
        //     assertThat(zip.size()).isEqualTo(2);
        // }
    }

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenObjectIds_whenGetPresignedObjectUrl_thenOk() throws Exception {
        final String objectId = Util.uploadFile(mvc);

        final String result = mvc.perform(get(BASE_URL + "/presigned-url/{bucket}/{objectId}/GET", Util.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info(">>>GetPresignedObjectUrl: {}", result);
        final URL url = new URL(result);
        assertThat(url.getHost()).isNotBlank();
        assertThat(url.getQuery()).isNotBlank();
    }

    @Test
    @WithMockUser(username = "test")
    public void givenNoRoles_whenGetObject_thenForbidden() throws Exception {
        mvc.perform(get(BASE_URL + "/objects/data/{bucket}/{objectId}", Util.TEST_BUCKET, "wrongobjectid"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    
}
