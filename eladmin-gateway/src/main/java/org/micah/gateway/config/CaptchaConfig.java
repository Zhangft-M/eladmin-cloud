package org.micah.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 验证码配置
 * 
 * @author ruoyi
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "captcha.config")
public class CaptchaConfig {
    /**
     * 验证码配置
     */
    private CaptchaEnum codeType = CaptchaEnum.arithmetic;
    /**
     * 验证码有效期 分钟
     */
    private Long expiration = 2L;
    /**
     * 验证码内容长度
     */
    private int length = 2;
    /**
     * 验证码宽度
     */
    private int width = 111;
    /**
     * 验证码高度
     */
    private int height = 36;
    /**
     * 验证码字体
     */
    private String fontName;
    /**
     * 字体大小
     */
    private int fontSize = 25;

    public CaptchaEnum getCodeType() {
        return codeType;
    }
}