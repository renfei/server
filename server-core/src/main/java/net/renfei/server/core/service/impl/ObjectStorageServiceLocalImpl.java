package net.renfei.server.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.ObjectStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;

/**
 * 本地磁盘实现对象存储服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application"
        , name = "defaultObjectStorageService"
        , havingValue = "ObjectStorageServiceLocalImpl"
        , matchIfMissing = true)
public class ObjectStorageServiceLocalImpl extends BaseService implements ObjectStorageService {

    @Override
    public byte[] getObjectBytes(String objectKey) {
        return this.getObjectBytes(null, objectKey);
    }

    @Override
    public InputStream getObjectInputStream(String objectKey) {
        return this.getObjectInputStream(null, objectKey);
    }

    @Override
    public byte[] getObjectBytes(String bucketName, String objectKey) {
        try {
            return Files.readAllBytes(Paths.get(objectKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getObjectInputStream(String bucketName, String objectKey) {
        try {
            return new FileInputStream(this.getObjectFile(null, objectKey, null));
        } catch (RuntimeException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getObjectFile(String objectKey, String path) {
        return this.getObjectFile(null, objectKey, path);
    }

    @Override
    public File getObjectFile(String bucketName, String objectKey, String path) {
        File file = new File(objectKey);
        if (file.isFile() && file.exists()) {
            return file;
        }
        throw new RuntimeException("文件不存在");
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
        this.putObject(null, objectKey, file, metadata);
    }

    @Override
    public void putObject(String objectKey, byte[] bytes, Map<String, String> metadata) {
        this.putObject(null, objectKey, bytes, metadata);
    }

    @Override
    public void putObject(String objectKey, ByteBuffer byteBuffer, Map<String, String> metadata) {
        this.putObject(null, objectKey, byteBuffer, metadata);
    }

    @Override
    public void putObject(String objectKey, InputStream inputStream, long contentLength, Map<String, String> metadata) {
        this.putObject(null, objectKey, inputStream, contentLength, metadata);
    }

    @Override
    public void putObject(String bucketName, String objectKey, File file, Map<String, String> metadata) {

    }

    @Override
    public void putObject(String bucketName, String objectKey, byte[] bytes, Map<String, String> metadata) {
        try {
            Files.write(Paths.get(objectKey), bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putObject(String bucketName, String objectKey, ByteBuffer byteBuffer, Map<String, String> metadata) {
        try (FileChannel fileChannel = new FileOutputStream(objectKey, false).getChannel()) {
            fileChannel.write(byteBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void putObject(String bucketName, String objectKey, InputStream inputStream,
                          long contentLength, Map<String, String> metadata) {
        try {
            Files.copy(inputStream, Paths.get(objectKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteObject(String objectKey) {
        this.deleteObject(null, objectKey);
    }

    @Override
    public void deleteObject(String bucketName, String objectKey) {
        try {
            Files.delete(Paths.get(objectKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyObject(String objectKey, String toObjectKey) {
        this.copyObject(null, objectKey, null, toObjectKey);
    }

    @Override
    public void copyObject(String objectKey, String toBucketName, String toObjectKey) {
        this.copyObject(null, objectKey, toBucketName, toObjectKey);
    }

    @Override
    public void copyObject(String bucketName, String objectKey, String toBucketName, String toObjectKey) {
        try {
            Files.copy(Paths.get(objectKey), Paths.get(toBucketName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
