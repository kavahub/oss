package cn.springseed.oss.minio.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cn.springseed.oss.minio.bean.MinioItem;
import cn.springseed.oss.minio.service.ListOptsService;
import io.minio.messages.Item;

/**
 *  {@link Item} 包装实体类
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/listservice")
public class ListServiceController {
    @Autowired
    private ListOptsService listOptsService;

    /**
     * 查询对象
     * 
     * @param bucket
     * @param prefix 对象前缀
     * @param includeUserMetadata 是否包含用户元数据
     * @return
     */
    @GetMapping(value = "/objects/{bucket}")
    @ResponseStatus(HttpStatus.OK)
    public List<MinioItem> listItems(@PathVariable("bucket") String bucket,
            @RequestParam(value = "prefix", required = false) String prefix,
            @RequestParam(value = "includeUserMetadata", required = false) Boolean includeUserMetadata) {
        final List<Item> items = listOptsService.listObjects(bucket, builder -> {
            if (StringUtils.hasText(prefix)) {
                builder.prefix(prefix);
            }

            if (includeUserMetadata != null) {
                builder.includeUserMetadata(includeUserMetadata);
            }
        });

        return items.stream().map(MinioItem::of).collect(Collectors.toList());
    }
    
}
