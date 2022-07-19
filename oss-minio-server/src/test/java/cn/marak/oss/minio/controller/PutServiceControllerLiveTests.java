package cn.marak.oss.minio.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import cn.marak.oss.OSSMinioApplication;
import cn.marak.oss.minio.TestProfiles;
import cn.marak.oss.minio.Util;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSMinioApplication.class)
@TestProfiles
@AutoConfigureMockMvc
public class PutServiceControllerLiveTests {
    private final static String BASE_URL = "/v1/putservice";
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "write")
    public void givenFile_whenPut_thenOK() throws Exception {
        final MvcResult result = mvc
                .perform(multipart(BASE_URL + "/object/{bucket}", Util.TEST_BUCKET)
                        .file(Util.createMockMultipartFile("test-file.txt")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isNotBlank();
    }

    @Test
    @WithMockUser(username = "write")
    public void givenJpgFile_whenPut_thenOK() throws Exception {
        final MvcResult result = mvc
                .perform(multipart(BASE_URL + "/object/{bucket}", Util.TEST_BUCKET)
                        .file(Util.createMockMultipartFile("picture.jpg")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isNotBlank();
    } 
}
