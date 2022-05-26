package cn.springseed.oss.local.metadata;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.springseed.oss.common.util.OSSRuntimeException;

/**
 * 元数据查询服务
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Service
public class MetadataQueryService {
    @Autowired
    private MetadataRepository metadataRepository;

    /**
     * 依据ID查找元数据，如果不存在，抛出 {@link MetadataNotFoundException} 异常
     * 
     * @param id
     * @return
     */
    public Optional<Metadata> findById(final String id) {
        final Metadata entity = metadataRepository.findById(id).orElseThrow(() -> OSSRuntimeException.metadataNotExist(String.valueOf(id)));
        return Optional.of(entity);
    } 
    
}
