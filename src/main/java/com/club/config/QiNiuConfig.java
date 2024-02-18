package com.club.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jzx
 * @date 2024-02-12 15:19
 * 概要：
 */

@Data
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiNiuConfig {

    /**
     * 设置好账号的ACCESS_KEY
     */
    private String ACCESS_KEY;

    /**
     * 设置好账号的SECRET_KEY
     */
    private String SECRET_KEY;

    /**
     * 设置七牛要上传的空间
     */
    private String bucketName;

    /**
     * 设置关联七牛的域名
     */
    private String QINIU_IMAGE_DOMAIN;

    /**
     * 密钥配置
     * @return
     */
    public Auth getAuth() {
        // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth;
    }

    /**
     * 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
     * 华东：zone0
     * 华北：zone1
     * 华南：zone2
     * 北美：zoneNa0
     *
     * @return
     */
    public Configuration getConfiguration() {
        Configuration cfg = new Configuration(Zone.zone0());
        return cfg;
    }

    /**
     * 构造一个七牛manager
     *
     * @return
     */
    public UploadManager getUploadManager() {
        return new UploadManager(getConfiguration());
    }

    /**
     * 构造一个七牛manager
     *
     * @return
     */
    public BucketManager getBucketManager() {
        return new BucketManager(getAuth(), getConfiguration());
    }

    /**
     * 简单上传，使用默认策略
     * 只需要设置上传的空间名就可以了
     *
     * @return
     */
    public String getUpToken() {
        return getAuth().uploadToken(getBucketName());
    }
}
