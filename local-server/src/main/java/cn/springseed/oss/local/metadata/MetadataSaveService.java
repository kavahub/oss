package cn.springseed.oss.local.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.springseed.oss.common.util.FileUtils;
import cn.springseed.oss.common.util.OSSRuntimeException;
import cn.springseed.oss.common.util.SecurityUtils;

/**
 * 元数据保持
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Service
@Transactional()
public class MetadataSaveService {
    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private MetadataQueryService metadataQueryService;

    public Metadata save(final String name, final String path, final long size) {
        validateName(name);

        final Metadata metadata = Metadata.builder().path(path)
                .name(name).size(size).type(FileUtils.getFileExtension(name))
                .createdBy(SecurityUtils.getCurrentUserInfo()).build();
        return metadataRepository.save(metadata);
    }

    public void updateName(final String id, final String name) {
        validateName(name);

        final Metadata metadata = this.metadataQueryService.findById(id).get();
        final String createdBy = metadata.getCreatedBy();
        if (StringUtils.hasText(createdBy) && !createdBy.equals(SecurityUtils.getCurrentUserInfo())) {
            throw OSSRuntimeException.metadataUpdatedByOwner();
        }
        ;
        metadata.setName(name);
        metadata.setType(FileUtils.getFileExtension(name));
        metadataRepository.save(metadata);
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw OSSRuntimeException.metadataNameInvalid(name);
        }
    }   
}
