package cn.springseed.oss.minio.util;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;

import org.springframework.util.StringUtils;

import io.minio.BaseArgs;
import io.minio.BucketArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.ObjectArgs;
import io.minio.ObjectConditionalReadArgs;
import io.minio.ObjectVersionArgs;
import io.minio.ObjectWriteArgs;
import io.minio.PutObjectArgs;
import io.minio.PutObjectBaseArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 参数工具
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class MinioArgsUtils {
    private String convert(Method method) {
        if (method == null) {
            return "";
        }

        return method.name();
    }

    private String convert(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }

        return map.toString();
    }

    private String convert(Multimap<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }

        return map.toString();
    }

    private String convert(Long l) {
        if (l != null) {
            return l.toString();
        }

        return "";
    }

    private String convert(String s) {
        if (StringUtils.hasText(s)) {
            return s;
        }

        return "";
    }

    private String convert(ZonedDateTime zdt) {
        if (zdt != null) {
            return zdt.toString();
        }

        return "";
    }

    private String print(List<String> msgList) {
        return new StringBuilder("{").append(msgList.stream().collect(Collectors.joining(","))).append("}").toString();
    }

    private List<String> printBaseArgs(final BaseArgs args) {
        final Multimap<String, String> headers = args.extraHeaders();
        final Multimap<String, String> params = args.extraQueryParams();

        return Arrays.asList("extraHeaders=" + convert(headers), "extraQueryParams=" + convert(params));
    }

    private List<String> printBucketArgs(final BucketArgs args) {
        final String bucket = args.bucket();
        final String region = args.region();

        return Arrays.asList("bucket=" + convert(bucket), "region=" + convert(region));
    }

    private List<String> printObjectArgs(final ObjectArgs args) {
        final String object = args.object();
        return Arrays.asList("object=" + convert(object));
    }

    private List<String> printObjectWriteArgs(final ObjectWriteArgs args) {
        final Multimap<String, String> headers = args.headers();
        final Multimap<String, String> userMetadata = args.userMetadata();
        final Map<String, String> tags = args.tags().get();

        return Arrays.asList("headers=" + convert(headers), "userMetadata=" + convert(userMetadata),
                "tags=" + convert(tags));
    }

    private List<String> printPutObjectBaseArgs(final PutObjectBaseArgs args) {
        final long size = args.objectSize();
        final long partSize = args.partSize();
        final long partCount = args.partCount();
        String contentType = "";
        try {
            contentType = args.contentType();
        } catch (IOException e) {
            log.error("contentType", e);
        }
        final boolean preloadData = args.preloadData();

        return Arrays.asList("size=" + size, "partSize=" + partSize, "partCount=" + partCount,
                "contentType=" + convert(contentType), "preloadData=" + preloadData);
    }

    public String printPutObjectArgs(final PutObjectArgs args) {
        final List<String> msgList = new ArrayList<>();
        msgList.addAll(printPutObjectBaseArgs(args));
        msgList.addAll(printObjectWriteArgs(args));
        msgList.addAll(printObjectArgs(args));
        msgList.addAll(printBucketArgs(args));
        msgList.addAll(printBaseArgs(args));

        return print(msgList);
    }


    private List<String> printObjectVersionArgs(final ObjectVersionArgs args) {
        final String versionId = args.versionId();


        return Arrays.asList("versionId=" + convert(versionId));
    }

    private List<String> printObjectConditionalReadArgs(final ObjectConditionalReadArgs args) {
        final Long offset = args.offset();
        final Long length = args.length();
        final String matchETag = args.matchETag();
        final String notMatchETag = args.notMatchETag();
        final ZonedDateTime modifiedSince = args.modifiedSince();
        final ZonedDateTime unmodifiedSince = args.unmodifiedSince();

        return Arrays.asList("offset=" + convert(offset), "length=" + convert(length), "matchETag=" + convert(matchETag),
                "notMatchETag=" + convert(notMatchETag), "modifiedSince=" + convert(modifiedSince), "unmodifiedSince=" + convert(unmodifiedSince));
    }

    public String printGetObjectArgs(final GetObjectArgs args) {
        final List<String> msgList = new ArrayList<>();
        msgList.addAll(printObjectConditionalReadArgs(args));
        msgList.addAll(printObjectVersionArgs(args));
        msgList.addAll(printObjectArgs(args));
        msgList.addAll(printBucketArgs(args));
        msgList.addAll(printBaseArgs(args));

        return print(msgList);
    }

    public String printListObjectsArgs(final ListObjectsArgs args) {
        final String delimiter = args.delimiter();
        final boolean useUrlEncodingType = args.useUrlEncodingType();
        final String keyMarker = args.keyMarker();
        final int maxKeys = args.maxKeys();
        final String prefix = args.prefix();
        final String continuationToken = args.continuationToken();
        final boolean fetchOwner = args.fetchOwner();
        final String versionIdMarker = args.versionIdMarker();
        final boolean includeUserMetadata = args.includeUserMetadata();
        final boolean recursive = args.recursive();
        final boolean useApiVersion1 = args.useApiVersion1();
        final boolean includeVersions = args.includeVersions();

        List<String> infoList = Arrays.asList("delimiter=" + convert(delimiter), "useUrlEncodingType=" + useUrlEncodingType, "keyMarker=" + convert(keyMarker),
                "maxKeys=" + maxKeys, "prefix=" + prefix, "continuationToken=" + convert(continuationToken),
                "maxKfetchOwnereys=" + fetchOwner, "versionIdMarker=" + convert(versionIdMarker), "includeUserMetadata=" + includeUserMetadata
                , "recursive=" + recursive, "useApiVersion1=" + useApiVersion1, "includeVersions=" + includeVersions);

        final List<String> msgList = new ArrayList<>(infoList);
        msgList.addAll(printBucketArgs(args));
        msgList.addAll(printBaseArgs(args));

        return print(msgList);
    }

    public String printRemoveObjectArgs(final RemoveObjectArgs args) {
        final boolean bypassGovernanceMode = args.bypassGovernanceMode();
        List<String> infoList = Arrays.asList("bypassGovernanceMode=" + bypassGovernanceMode);
        
        final List<String> msgList = new ArrayList<>(infoList);
        msgList.addAll(printObjectVersionArgs(args));
        msgList.addAll(printObjectArgs(args));
        msgList.addAll(printBucketArgs(args));
        msgList.addAll(printBaseArgs(args));

        return print(msgList);
    }

    public String printStatObjectArgs(final StatObjectArgs args) {
        final List<String> msgList = new ArrayList<>();
        msgList.addAll(printObjectConditionalReadArgs(args));
        msgList.addAll(printObjectVersionArgs(args));
        msgList.addAll(printObjectArgs(args));
        msgList.addAll(printBucketArgs(args));
        msgList.addAll(printBaseArgs(args));

        return print(msgList);
    }

    public static Object printGetPresignedObjectUrlArgs(GetPresignedObjectUrlArgs args) {
        final Method method = args.method();
        final int expiry = args.expiry();
        List<String> infoList = Arrays.asList("method=" + convert(method), "expiry=" + expiry);
        
        final List<String> msgList = new ArrayList<>(infoList);
        msgList.addAll(printObjectVersionArgs(args));
        msgList.addAll(printObjectArgs(args));
        msgList.addAll(printBucketArgs(args));
        msgList.addAll(printBaseArgs(args));
        
        return print(msgList);
    }    
}
