package cn.springseed.oss.minio;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import lombok.experimental.UtilityClass;

/**
 * 工具
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class Util {
    public final static String TEST_BUCKET = "docexamplebucket12";
    
    public MockMultipartFile createMockMultipartFile(final String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return new MockMultipartFile("file", resource.getFilename(), "multipart/form-data",
                resource.getInputStream());
    }
    public String uploadFile(MockMvc mvc) throws Exception, IOException {
        return uploadFile(mvc, "test-file.txt");
    }

    public String uploadFile(MockMvc mvc, String fileName) throws Exception, IOException {
        String objectId = UUID.randomUUID().toString();
        mvc.perform(multipart("/v1/putservice/object/{bucket}", TEST_BUCKET)
                .file(createMockMultipartFile(fileName))
                .param("objectId", objectId))
                .andDo(print())
                .andExpect(status().isCreated());
        return objectId;
    }    
}
