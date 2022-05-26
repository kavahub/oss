package cn.springseed.oss.local.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import cn.springseed.oss.OSSLocalApplication;
import cn.springseed.oss.local.MockUserUtil;
import cn.springseed.oss.local.SpringseedActiveProfiles;

/**
 * 测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSLocalApplication.class)
@SpringseedActiveProfiles
@AutoConfigureMockMvc
public class OSSLocalControllerTests {
    private final static String FILES_URL = "/v1/files";
    private final static String METADATA_URL = "/v1/metadatas";
    private final static String TEST_FILE_TXT = "test-file.txt";

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenUploadFile_whenDownload_thenContentOK() throws Exception {
        // upload
        final String objectId = this.upload(TEST_FILE_TXT);

        // 文件内容测试
        final String fileData = this.loadContent(objectId);
        assertThat(fileData).isEqualTo("你好");

        // 创建人测试
        mvc.perform(get(METADATA_URL + "/{id}", objectId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdBy", equalTo("test")));
    }

    @Test
    @WithMockUser(roles = { "oss_read", "oss_write", "oss_delete" })
    public void givenUploadFile_whenRemove_thenContentOK() throws Exception {
        // upload
        final String objectId = this.upload(TEST_FILE_TXT);
        assertThat(this.isNotFound(objectId)).isEqualTo(false);

        this.remove(objectId);
        assertThat(this.isNotFound(objectId)).isEqualTo(true);

    }

    @Test
    @WithMockUser(roles = { "oss_read", "oss_write" })
    public void givenFile_whenAllInZip_thenOK() throws Exception {
        // upload
        final String objectId1 = this.upload(TEST_FILE_TXT);
        final String objectId2 = this.upload("test-file2");

        // 下载zip
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("objectIds", Arrays.asList(objectId1, objectId2, "wrong_id"));
        final MvcResult result = mvc.perform(get(FILES_URL + "/download-zip").params(params))
                //.andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // 保存到target
        final Path zipPath = Paths.get("target", "all-in-zip.zip");
        final byte[] zipData = result.getResponse().getContentAsByteArray();
        Files.copy(new ByteArrayInputStream(zipData), zipPath, StandardCopyOption.REPLACE_EXISTING);

        // try (final ZipFile zip = new ZipFile(zipPath.toFile())) {
        //     assertThat(zip.size()).isEqualTo(2);
        // }
    }

    @Test
    public void givenUploadFile_whenUpdateName_thenOK() throws Exception {
        MockUserUtil.logInAs("test1", new String[] { "oss_write" });
        // upload
        final String objectId = this.upload(TEST_FILE_TXT);
        // 修改文件名称
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("name", "aaa.txt");
        mvc.perform(put(METADATA_URL + "/{id}/name", objectId).params(params1))
                .andDo(print())
                .andExpect(status().isNoContent());

        MockUserUtil.logInAs("test2", new String[] { "oss_write" });
        // 修改文件名称
        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("name", "bbb.txt");
        mvc.perform(put(METADATA_URL + "/{id}/name", objectId).params(params2))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", equalTo("OSSRUNTIMEEXCEPTION.METADATA_UPDATED_BY_OWNER")));
    }

    @Test
    @WithMockUser(roles = { "oss_read", "oss_delete" })
    public void givenWrongRole_whenUpload_thenForbidden() throws Exception {
        mvc.perform(multipart(FILES_URL + "/upload").file(this.createMockMultipartFile(TEST_FILE_TXT)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = { "oss_write", "oss_delete" })
    public void givenWrongRole_whenLoad_thenForbidden() throws Exception {
        mvc.perform(get(FILES_URL + "/download/id"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = { "oss_write", "oss_read" })
    public void givenWrongRole_whenRemove_thenForbidden() throws Exception {
        mvc.perform(delete(FILES_URL + "/{objectId}", ""))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    private MockMultipartFile createMockMultipartFile(final String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return new MockMultipartFile("file", resource.getFilename(),
                "multipart/form-data",
                resource.getInputStream());
    }

    private String upload(final String fileName) throws Exception {

        final MvcResult result1 = mvc
                .perform(multipart(FILES_URL + "/upload").file(createMockMultipartFile(fileName)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        return result1.getResponse().getContentAsString();
    }

    private String loadContent(final String objectId) throws Exception {
        final MvcResult result = mvc.perform(get(FILES_URL + "/download/{objectId}", objectId))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        return new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private void remove(final String objectId) throws Exception {
        mvc.perform(delete(FILES_URL + "/{objectId}", objectId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private boolean isNotFound(final String objectId) throws Exception {
        final MvcResult result = mvc.perform(get(FILES_URL + "/download/{objectId}", objectId))
                .andDo(print()).andReturn();

        return result.getResponse().getStatus() == 404;
    }

}
