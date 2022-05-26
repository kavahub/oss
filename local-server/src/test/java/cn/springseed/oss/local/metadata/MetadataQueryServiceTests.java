package cn.springseed.oss.local.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cn.springseed.oss.common.util.OSSRuntimeException;
import cn.springseed.oss.common.util.OSSRuntimeException.NotFound;
import cn.springseed.oss.local.OSSLocalApplication;
import cn.springseed.oss.local.SpringseedActiveProfiles;

/**
 * 查询测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSLocalApplication.class)
@SpringseedActiveProfiles
public class MetadataQueryServiceTests {
    @Autowired
    private MetadataQueryService eetadataQueryService;

    @Autowired
    private MetadataRepository metadataRepository;

    private Metadata md = Metadata.builder().name("name").path("path").build();

    @BeforeEach
    public void setUp() {
        metadataRepository.saveAndFlush(md);
    }
    
    @Test
    public void whenValidId_thenShouldBeFound() {
        Metadata found = eetadataQueryService.findById(md.getId()).get();

        assertThat(found).isNotNull();
    }

    @Test
    public void whenInValidId_thenShouldBeException() {
        assertThrows(OSSRuntimeException.class, () -> this.eetadataQueryService.findById("wrong_id")).getErrorCode().equals(NotFound.METADATA_NOT_EXIST);

   }    
}
