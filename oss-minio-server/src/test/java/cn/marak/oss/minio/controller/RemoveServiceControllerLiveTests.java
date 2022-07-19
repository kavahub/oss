package cn.marak.oss.minio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class RemoveServiceControllerLiveTests {
        private final static String BASE_URL = "/v1/removeservice";
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test")
    public void givenFile_whenRemove_thenOK() throws Exception {
        final String objectId = Util.uploadFile(mvc);

        mvc.perform(delete(BASE_URL + "/object/{bucket}/{objectId}", Util.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(get("/v1/getservice/object/{bucket}/{objectId}", Util.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test")
    public void givenWrongID_whenRemove_thenOK() throws Exception {
        mvc.perform(delete(BASE_URL + "/object/{bucket}/{objectId}", Util.TEST_BUCKET, "wrongId"))
                .andDo(print())
                .andExpect(status().isNoContent());

    }
  
}
