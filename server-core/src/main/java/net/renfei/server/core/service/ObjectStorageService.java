package net.renfei.server.core.service;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * 对象存储服务
 *
 * @author renfei
 */
public interface ObjectStorageService {
    /**
     * 获取对象字节数组
     *
     * @param objectKey 对象Key
     * @return
     */
    byte[] getObjectBytes(String objectKey);

    /**
     * 获取对象流
     *
     * @param objectKey 对象Key
     * @return
     */
    InputStream getObjectInputStream(String objectKey);

    /**
     * 获取对象字节数组
     *
     * @param bucketName 存储桶名
     * @param objectKey  对象Key
     * @return
     */
    byte[] getObjectBytes(String bucketName, String objectKey);

    /**
     * 获取对象流
     *
     * @param bucketName 存储桶名
     * @param objectKey  对象Key
     * @return
     */
    InputStream getObjectInputStream(String bucketName, String objectKey);

    /**
     * 获取对象文件
     *
     * @param objectKey 对象Key
     * @param path      本地文件存储路径
     * @return
     */
    File getObjectFile(String objectKey, String path);

    /**
     * 获取对象文件
     *
     * @param bucketName 存储桶名
     * @param objectKey  对象Key
     * @param path       本地文件存储路径
     * @return
     */
    File getObjectFile(String bucketName, String objectKey, String path);

    /**
     * 上传对象
     *
     * @param objectKey 对象Key
     * @param file      本地文件对象
     */
    void putObject(String objectKey, File file);

    /**
     * 上传对象
     *
     * @param objectKey 对象Key
     * @param bytes     本地对象字节数组
     */
    void putObject(String objectKey, byte[] bytes);

    /**
     * 上传对象
     *
     * @param objectKey  对象Key
     * @param byteBuffer 本地对象字节Buffer
     */
    void putObject(String objectKey, ByteBuffer byteBuffer);

    /**
     * 上传对象
     *
     * @param objectKey     对象Key
     * @param inputStream   输入流
     * @param contentLength 对象总大小
     */
    void putObject(String objectKey, InputStream inputStream, long contentLength);

    /**
     * 上传对象
     *
     * @param objectKey 对象Key
     * @param file      对象文件
     * @param metadata  对象元信息
     */
    void putObject(String objectKey, File file, Map<String, String> metadata);

    /**
     * 上传对象
     *
     * @param objectKey 对象Key
     * @param bytes     对象字节数组
     * @param metadata  对象元信息
     */
    void putObject(String objectKey, byte[] bytes, Map<String, String> metadata);

    /**
     * 上传对象
     *
     * @param objectKey  对象Key
     * @param byteBuffer 对象字节Buffer
     * @param metadata   对象元信息
     */
    void putObject(String objectKey, ByteBuffer byteBuffer, Map<String, String> metadata);

    /**
     * 上传对象
     *
     * @param objectKey     对象Key
     * @param inputStream   输入流
     * @param contentLength 对象总大小
     * @param metadata      对象元信息
     */
    void putObject(String objectKey, InputStream inputStream, long contentLength, Map<String, String> metadata);

    /**
     * 上传对象
     *
     * @param bucketName 存储桶名
     * @param objectKey  对象Key
     * @param file       对象文件
     * @param metadata   对象元信息
     */
    void putObject(String bucketName, String objectKey, File file, Map<String, String> metadata);

    /**
     * 上传对象
     *
     * @param bucketName 存储桶名
     * @param objectKey  对象Key
     * @param bytes      对象字节数组
     * @param metadata   对象元信息
     */
    void putObject(String bucketName, String objectKey, byte[] bytes, Map<String, String> metadata);

    /**
     * 上传对象
     *
     * @param bucketName 存储桶名
     * @param objectKey  对象Key
     * @param byteBuffer 对象字节Buffer
     * @param metadata   对象元信息
     */
    void putObject(String bucketName, String objectKey, ByteBuffer byteBuffer, Map<String, String> metadata);

    /**
     * 上传对象
     *
     * @param bucketName    存储桶名
     * @param objectKey     对象Key
     * @param inputStream   输入流
     * @param contentLength 对象总大小
     * @param metadata      对象元信息
     */
    void putObject(String bucketName, String objectKey, InputStream inputStream, long contentLength, Map<String, String> metadata);

    /**
     * 删除对象
     *
     * @param objectKey 对象Key
     */
    void deleteObject(String objectKey);

    /**
     * 删除对象
     *
     * @param bucketName 存储桶名
     * @param objectKey  对象Key
     */
    void deleteObject(String bucketName, String objectKey);

    /**
     * 复制对象
     *
     * @param objectKey   对象Key
     * @param toObjectKey 目标对象Key
     */
    void copyObject(String objectKey, String toObjectKey);

    /**
     * 复制对象
     *
     * @param objectKey    对象Key
     * @param toBucketName 目标存储桶名
     * @param toObjectKey  目标对象Key
     */
    void copyObject(String objectKey, String toBucketName, String toObjectKey);

    /**
     * 复制对象
     *
     * @param bucketName   存储桶名
     * @param objectKey    对象Key
     * @param toBucketName 目标存储桶名
     * @param toObjectKey  目标对象Key
     */
    void copyObject(String bucketName, String objectKey, String toBucketName, String toObjectKey);
}
