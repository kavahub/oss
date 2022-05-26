package cn.springseed.oss.local.metadata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 元数据JPA接口
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Repository
public interface MetadataRepository extends JpaRepository<Metadata, String> { 
    
}
