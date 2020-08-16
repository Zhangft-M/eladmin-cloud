package org.micah.core.constant;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public final class CaptchaConstants {

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

}
