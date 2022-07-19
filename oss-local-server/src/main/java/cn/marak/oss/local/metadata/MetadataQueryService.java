package cn.marak.oss.local.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.marak.oss.local.util.OSSLocalRuntimeException;


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
     * 依据ID查找元数据
     * 
     * @param id
     * @throws
     *  MetadataNotFoundException: id不存在时
     * @return
     */
    public Metadata findById(final String id) {
        final Metadata entity = metadataRepository.findById(id).orElseThrow(() -> OSSLocalRuntimeException.metadataNotExist(String.valueOf(id)));
        return entity;
    } 
    
}
