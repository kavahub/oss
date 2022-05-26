package cn.springseed.oss.minio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import cn.springseed.oss.OSSMinioApplication;
import cn.springseed.oss.minio.SpringseedActiveProfiles;
import cn.springseed.oss.minio.Util;

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
public class ListServiceControllerLiveTests {
    private final static String BASE_URL = "/v1/listservice";
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test", roles = { "oss_read" })
    public void whenGetItem_thenOK() throws Exception {
        mvc.perform(get(BASE_URL + "/objects/{bucket}", Util.TEST_BUCKET))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "test")
    public void givenNoRoles_thenForbidden() throws Exception {
        mvc.perform(get(BASE_URL + "/objects/{bucket}", Util.TEST_BUCKET))
                .andDo(print())
                .andExpect(status().isForbidden());
    }    
}
