package cn.marak.oss.local.metadata;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import cn.marak.oss.OSSLocalApplication;
import cn.marak.oss.local.TestProfiles;

/**
 * 接口测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSLocalApplication.class)
@AutoConfigureMockMvc
@TestProfiles
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataControllerTests {
    private final static String BASE_URL = "/v1/metadatas";
    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private MockMvc mvc;

    private Metadata md1 = Metadata.builder().name("name1").path("path1").build();
    private Metadata md2 = Metadata.builder().name("name2").path("path2").build();

    @BeforeAll

    public void BeforeAll() {
        metadataRepository.saveAllAndFlush(Arrays.asList(md1, md2));
    }

    @Test
    @WithMockUser(username = "test")
    public void givenId_whenGet_thenOk() throws Exception {
        mvc.perform(get(BASE_URL + "/{id}", md1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("name1")));
    }

    @Test
    @WithMockUser(username = "test")
    public void givenWrongId_whenGet_thenNotFound() throws Exception {
        mvc.perform(get(BASE_URL + "/{id}", "wrong_id"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test")
    public void givenIds_whenGetIds_thenOK() throws Exception {
        List<String> ids = Arrays.asList(md1.getId(), md2.getId(), "wrong_id");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("ids", ids);

        mvc.perform(get(BASE_URL + "/ids").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(equalTo(2))));
    }
}
