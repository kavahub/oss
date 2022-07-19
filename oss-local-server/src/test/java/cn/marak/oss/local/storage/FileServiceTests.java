package cn.marak.oss.local.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cn.marak.oss.OSSLocalApplication;
import cn.marak.oss.local.TestProfiles;
import cn.marak.oss.local.config.OSSProperties;
import cn.marak.oss.local.metadata.Metadata;
import cn.marak.oss.local.metadata.MetadataRepository;
import cn.marak.oss.local.util.OSSLocalRuntimeException;
import cn.marak.oss.local.util.OSSLocalRuntimeException.NotFound;

/**
 * 存储服务测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSLocalApplication.class)
@TestProfiles
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileServiceTests {
    @Autowired
    private FileSystemService storageService;
    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private OSSProperties properties;

    private Metadata md = Metadata.builder().name("name").path("path").build();

    @BeforeAll
    public void BeforeAll() {
        metadataRepository.save(md);
    }

    @Test
    public void thenNotNull() {
        assertThat(storageService).isNotNull();
    }

    @Test
    public void givenWrongId_whenLoadByObjectId_thenMetadataNotFoundException() {
        assertThrows(OSSLocalRuntimeException.class, () -> storageService.loadByObjectId("wrongId")).getErrorCode()
                .equals(NotFound.METADATA_NOT_EXIST);
    }

    @Test
    public void givenWrongId_whenLoadByMetadata_thenObjectNotFoundException() {
        assertThrows(OSSLocalRuntimeException.class, () -> storageService.loadByMetadata(md)).getErrorCode()
                .equals(NotFound.FILE_NOT_EXIST);
    }

    @Test
    public void givenFile_whenStore_thenOK() throws IOException {
        final String objectId = this.storeFile();
        final Metadata metadata = metadataRepository.findById(objectId).get();

        final Path filePath = this.getFilePath(metadata);
        assertThat(Files.exists(filePath)).isTrue();
        assertThat(Files.readString(filePath)).isEqualTo("Hello");
    }

    @Test
    public void givenFile_whenStoreAndLoad_thenOK() {
        final String objectId = this.storeFile();
        final FileData fileData = storageService.loadByObjectId(objectId);
        assertThat(fileData.exists()).isTrue();
    }

    @Test
    public void givenFile_whenStoreAndRemove_thenOK() {
        final String objectId = this.storeFile();

        final Metadata metadata = metadataRepository.findById(objectId).get();
        storageService.removeByObjectId(objectId);

        assertThrows(OSSLocalRuntimeException.class, () -> storageService.loadByObjectId(objectId)).getErrorCode()
                .equals(NotFound.METADATA_NOT_EXIST);

        assertThat(Files.exists(this.getFilePath(metadata))).isFalse();
    }

    private Path getFilePath(final Metadata metadata) {
        return properties.getUploadRootPath().resolve(metadata.getFullPath()).resolve(metadata.getId());
    }

    private String storeFile() {
        return storageService.store(new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE,
                "Hello".getBytes()));
    }
}
