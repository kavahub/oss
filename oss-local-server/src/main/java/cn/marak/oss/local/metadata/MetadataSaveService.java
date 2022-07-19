package cn.marak.oss.local.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.marak.oss.local.util.OSSLocalRuntimeException;
import cn.marak.oss.local.util.SecurityUtils;


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

    public Metadata save(final String name, final String path, final String contentType, final long size) {
        validateName(name);

        final Metadata metadata = Metadata.builder().path(path)
                .name(name).size(size).contentType(contentType)
                .createdBy(SecurityUtils.getCurrentUserInfo()).build();
        return metadataRepository.save(metadata);
    }

    public void updateName(final String id, final String name) {
        validateName(name);

        final Metadata metadata = this.metadataQueryService.findById(id);
        final String createdBy = metadata.getCreatedBy();
        if (StringUtils.hasText(createdBy) && !createdBy.equals(SecurityUtils.getCurrentUserInfo())) {
            throw OSSLocalRuntimeException.metadataUpdatedByOwner();
        }
        metadata.setName(name);
        metadataRepository.save(metadata);
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw OSSLocalRuntimeException.metadataNameInvalid(name);
        }
    }   
}
