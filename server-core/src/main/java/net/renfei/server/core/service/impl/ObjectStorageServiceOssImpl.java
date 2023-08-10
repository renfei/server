package net.renfei.server.core.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.ObjectStorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * 阿里云OSS实现对象存储服务
 *
 * @author renfei
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "application"
        , name = "defaultObjectStorageService"
        , havingValue = "ObjectStorageServiceOssImpl")
public class ObjectStorageServiceOssImpl extends BaseService implements ObjectStorageService {
    private final OSS ossClient;
    private final ServerProperties serverProperties;

    public ObjectStorageServiceOssImpl(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
        this.ossClient = new OSSClientBuilder().build(
                serverProperties.getAliyun().getOss().getEndpoint(),
                serverProperties.getAliyun().getAccessKeyId(),
                serverProperties.getAliyun().getAccessKeySecret());
    }

    @Override
    public byte[] getObjectBytes(String objectKey) {
        return this.getObjectBytes(serverProperties.getAliyun().getOss().getBucketName(), objectKey);
    }

    @Override
    public InputStream getObjectInputStream(String objectKey) {
        return this.getObjectInputStream(serverProperties.getAliyun().getOss().getBucketName(), objectKey);
    }

    @Override
    public byte[] getObjectBytes(String bucketName, String objectKey) {
        try {
            return IOUtils.toByteArray(this.getObjectInputStream(bucketName, objectKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getObjectInputStream(String bucketName, String objectKey) {
        try (OSSObject ossObject = ossClient.getObject(bucketName, objectKey)) {
            return ossObject.getObjectContent();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public File getObjectFile(String objectKey, String path) {
        return this.getObjectFile(serverProperties.getAliyun().getOss().getBucketName(), objectKey, path);
    }

    @Override
    public File getObjectFile(String bucketName, String objectKey, String path) {
        File file = new File(path);
        ossClient.getObject(new GetObjectRequest(bucketName, objectKey), file);
        return file;
    }

    @Override
    public void putObject(String objectKey, File file) {
        this.putObject(objectKey, file, null);
    }

    @Override
    public void putObject(String objectKey, byte[] bytes) {
        this.putObject(objectKey, bytes, null);
    }

    @Override
    public void putObject(String objectKey, ByteBuffer byteBuffer) {
        this.putObject(objectKey, byteBuffer, null);
    }

    @Override
    public void putObject(String objectKey, InputStream inputStream, long contentLength) {
        this.putObject(objectKey, inputStream, contentLength, null);
    }

    @Override
    public void putObject(String objectKey, File file, Map<String, String> metadata) {
        this.putObject(serverProperties.getAliyun().getOss().getBucketName(), objectKey, file, metadata);
    }

    @Override
    public void putObject(String objectKey, byte[] bytes, Map<String, String> metadata) {
        this.putObject(serverProperties.getAliyun().getOss().getBucketName(), objectKey, bytes, metadata);
    }

    @Override
    public void putObject(String objectKey, ByteBuffer byteBuffer, Map<String, String> metadata) {
        this.putObject(serverProperties.getAliyun().getOss().getBucketName(), objectKey, byteBuffer, metadata);
    }

    @Override
    public void putObject(String objectKey, InputStream inputStream, long contentLength, Map<String, String> metadata) {
        this.putObject(serverProperties.getAliyun().getOss().getBucketName(), objectKey, inputStream, contentLength, metadata);
    }

    @Override
    public void putObject(String bucketName, String objectKey, File file, Map<String, String> metadata) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setUserMetadata(metadata);
        this.ossClient.putObject(bucketName, objectKey, file, objectMetadata);
    }

    @Override
    public void putObject(String bucketName, String objectKey, byte[] bytes, Map<String, String> metadata) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (metadata != null) {
            objectMetadata.setUserMetadata(metadata);
        }
        this.ossClient.putObject(bucketName, objectKey, new ByteArrayInputStream(bytes), objectMetadata);
    }

    @Override
    public void putObject(String bucketName, String objectKey, ByteBuffer byteBuffer, Map<String, String> metadata) {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes, 0, bytes.length);
        this.putObject(bucketName, objectKey, bytes, metadata);
    }

    @Override
    public void putObject(String bucketName, String objectKey, InputStream inputStream, long contentLength, Map<String, String> metadata) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (metadata != null) {
            objectMetadata.setUserMetadata(metadata);
        }
        this.ossClient.putObject(bucketName, objectKey, inputStream, objectMetadata);
    }

    @Override
    public void deleteObject(String objectKey) {
        this.deleteObject(serverProperties.getAliyun().getOss().getBucketName(), objectKey);
    }

    @Override
    public void deleteObject(String bucketName, String objectKey) {
        this.ossClient.deleteObject(bucketName, objectKey);
    }

    @Override
    public void copyObject(String objectKey, String toObjectKey) {
        this.copyObject(serverProperties.getAliyun().getOss().getBucketName(), objectKey, serverProperties.getAliyun().getOss().getBucketName(), toObjectKey);
    }

    @Override
    public void copyObject(String objectKey, String toBucketName, String toObjectKey) {
        this.copyObject(serverProperties.getAliyun().getOss().getBucketName(), objectKey, toBucketName, toObjectKey);
    }

    @Override
    public void copyObject(String bucketName, String objectKey, String toBucketName, String toObjectKey) {
        ossClient.copyObject(bucketName, objectKey, toBucketName, toObjectKey);
    }
}
