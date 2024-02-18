package com.club.utils;

import com.alibaba.fastjson2.JSONObject;
import com.club.common.exception.GlobalException;
import com.club.config.QiNiuConfig;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author
 * @date 2024-02-12 15:19
 * 概要：操作七牛云的工具类
 */
@Component
@Slf4j
public class QiNiuUtil {

    @Autowired
    private QiNiuConfig config;

    private static QiNiuConfig qiNiuConfig;

    //初始化静态参数
    //通过@PostConstruct实现初始化bean之前进行的操作
    @PostConstruct
    public void init() {
        qiNiuConfig = config;
    }

    /**
     * 储存图片文件
     *
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String saveFile(MultipartFile file, String fileName) throws IOException {
        try {
            // 调用put方法上传
            Response res = qiNiuConfig.getUploadManager().put(file.getBytes(), fileName, qiNiuConfig.getUpToken());
            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回这张存储照片的地址
                return qiNiuConfig.getQINIU_IMAGE_DOMAIN() + JSONObject.parseObject(res.bodyString()).get("key");

            } else {
                log.error("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            log.error("七牛异常:" + e.getMessage());
            return null;
        }
    }

    /**
     * 存储文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String saveFile(MultipartFile file) throws IOException {
        //判断文件是否为空
        if (file.isEmpty()) {
            throw new GlobalException("文件为空");
        }
        //获取文件后缀
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        //获取文件类型
        String type = FileUtil.getFileType(suffix);
        //获取文件大小
        FileUtil.getSize(file.getSize());
        //获取UUID+date文件名
        String fileName = QiNiuUtil.getUUIDFileNameNow(file.getOriginalFilename());
        //获取文件上传七牛云后的url
        return QiNiuUtil.saveFile(file, fileName);
    }

    /**
     * 储存图片文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String deleteFile(String fileName) throws IOException {
        BucketManager bucketManager = qiNiuConfig.getBucketManager();
        try {
            //七牛云删除
            bucketManager.delete(qiNiuConfig.getBucketName(), fileName);
            //数据库删除
            return "success";
        } catch (QiniuException ex) {
            //数据库删除

            return "failed";
        }
    }

    /**
     * 获取UUID+Date的文件名
     *
     * @param fileName
     * @return
     */
    @SuppressWarnings("")
    public static String getUUIDFileNameNow(String fileName) {
        int dotPos = fileName.lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return "jzx/" + format.format(date) + "/" + UUID.randomUUID().toString().replaceAll("-", "") + "/" + fileName;
    }
}
