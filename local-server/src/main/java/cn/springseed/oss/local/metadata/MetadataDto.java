package cn.springseed.oss.local.metadata;

import java.time.ZonedDateTime;

import lombok.Data;

/**
 * 元数据传输对象
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
public class MetadataDto {
    private String id;

    /** 文件路径 */
    private String path;
    
    /** 类型 */
    private String type;

    /** 名称 */
    private String name;

    /** 大小，字节 */
    private long size;

    /** 创建日期 */
    private ZonedDateTime createdOn;

    /** 创建人*/
    private String createdBy;

    /** 校验码 */
    private String crc32;   
}
