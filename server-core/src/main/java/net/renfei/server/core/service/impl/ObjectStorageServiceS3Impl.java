package net.renfei.server.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.config.ServerProperties;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.ObjectStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * AWS S3 实现对象存储服务
 *
 * @author renfei
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "application"
        , name = "defaultObjectStorageService"
        , havingValue = "ObjectStorageServiceS3Impl")
public class ObjectStorageServiceS3Impl extends BaseService implements ObjectStorageService {
    private final S3Client s3Client;
    private final ServerProperties serverProperties;

    public ObjectStorageServiceS3Impl(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                serverProperties.getAws().getAccessKeyId(),
                serverProperties.getAws().getSecretAccessKey());
        Region region = Region.of(serverProperties.getAws().getS3Region());
        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }

    @Override
    public byte[] getObjectBytes(String objectKey) {
        return this.getObjectBytes(serverProperties.getAws().getS3BucketName(), objectKey);
    }

    @Override
    public InputStream getObjectInputStream(String objectKey) {
        return this.getObjectInputStream(serverProperties.getAws().getS3BucketName(), objectKey);
    }

    @Override
    public byte[] getObjectBytes(String bucketName, String objectKey) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(objectKey)
                .bucket(bucketName)
                .build();
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObject(objectRequest, ResponseTransformer.toBytes());
        return objectBytes.asByteArray();
    }

    @Override
    public InputStream getObjectInputStream(String bucketName, String objectKey) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(objectKey)
                .bucket(bucketName)
                .build();
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObject(objectRequest, ResponseTransformer.toBytes());
        return objectBytes.asInputStream();
    }

    @Override
    public File getObjectFile(String objectKey, String path) {
        return this.getObjectFile(serverProperties.getAws().getS3BucketName(), objectKey, path);
    }


    @Override
    public File getObjectFile(String bucketName, String objectKey, String path) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(objectKey)
                .bucket(bucketName)
                .build();
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObject(objectRequest, ResponseTransformer.toBytes());
        File file = new File(path);
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(objectBytes.asByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        this.putObject(serverProperties.getAws().getS3BucketName(), objectKey, file, metadata);
    }

    @Override
    public void putObject(String objectKey, byte[] bytes, Map<String, String> metadata) {
        this.putObject(serverProperties.getAws().getS3BucketName(), objectKey, bytes, metadata);
    }

    @Override
    public void putObject(String objectKey, ByteBuffer byteBuffer, Map<String, String> metadata) {
        this.putObject(serverProperties.getAws().getS3BucketName(), objectKey, byteBuffer, metadata);
    }

    @Override
    public void putObject(String objectKey, InputStream inputStream, long contentLength, Map<String, String> metadata) {
        this.putObject(serverProperties.getAws().getS3BucketName(), objectKey, inputStream, contentLength, metadata);
    }

    @Override
    public void putObject(String bucketName, String objectKey, File file, Map<String, String> metadata) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .metadata(metadata)
                .build();
        RequestBody requestBody = RequestBody.fromFile(file);
        this.putObject(putObjectRequest, requestBody);
    }

    @Override
    public void putObject(String bucketName, String objectKey, byte[] bytes, Map<String, String> metadata) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .metadata(metadata)
                .build();
        RequestBody requestBody = RequestBody.fromBytes(bytes);
        this.putObject(putObjectRequest, requestBody);
    }

    @Override
    public void putObject(String bucketName, String objectKey, ByteBuffer byteBuffer, Map<String, String> metadata) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .metadata(metadata)
                .build();
        RequestBody requestBody = RequestBody.fromByteBuffer(byteBuffer);
        this.putObject(putObjectRequest, requestBody);
    }

    @Override
    public void putObject(String bucketName, String objectKey, InputStream inputStream,
                          long contentLength, Map<String, String> metadata) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .metadata(metadata)
                .build();
        RequestBody requestBody = RequestBody.fromInputStream(inputStream, contentLength);
        this.putObject(putObjectRequest, requestBody);
    }

    @Override
    public void deleteObject(String objectKey) {
        this.deleteObject(serverProperties.getAws().getS3BucketName(), objectKey);
    }

    @Override
    public void deleteObject(String bucketName, String objectKey) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    @Override
    public void copyObject(String objectKey, String toObjectKey) {
        this.copyObject(serverProperties.getAws().getS3BucketName(), objectKey, serverProperties.getAws().getS3BucketName(), toObjectKey);
    }

    @Override
    public void copyObject(String objectKey, String toBucketName, String toObjectKey) {
        this.copyObject(serverProperties.getAws().getS3BucketName(), objectKey, toBucketName, toObjectKey);
    }

    @Override
    public void copyObject(String bucketName, String objectKey, String toBucketName, String toObjectKey) {
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(objectKey)
                .destinationBucket(toBucketName)
                .destinationKey(toObjectKey)
                .build();
        s3Client.copyObject(copyObjectRequest);
    }

    private void putObject(PutObjectRequest putObjectRequest, RequestBody requestBody) {
        s3Client.putObject(putObjectRequest, requestBody);
    }
}
