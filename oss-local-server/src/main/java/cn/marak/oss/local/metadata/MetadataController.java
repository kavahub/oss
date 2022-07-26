package cn.marak.oss.local.metadata;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 元数据API接口
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/metadatas")
public class MetadataController {
    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private MetadataSaveService metadataSaveService;
    @Autowired
    private MetadataQueryService metadataQueryService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/{id}")
    public MetadataDto getById(@PathVariable String id) {
        final Metadata md = this.metadataQueryService.findById(id);
        return modelMapper.map(md, MetadataDto.class);
    }

    @GetMapping(value = "/ids")
    public List<MetadataDto> getByIds(@RequestParam Set<String> ids) {
        return metadataRepository.findAllById(ids)
                .stream()
                .map(e -> modelMapper.map(e, MetadataDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/{id}/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @RequestParam(value="name") String name) {
        metadataSaveService.updateName(id, name);
    }    
}
