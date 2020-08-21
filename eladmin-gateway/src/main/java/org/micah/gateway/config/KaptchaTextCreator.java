package org.micah.gateway.config;

import java.awt.*;
import java.util.Objects;

import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import org.micah.core.util.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 验证码文本生成器
 *
 */
@Component
public class KaptchaTextCreator {
    
    private final CaptchaConfig captchaConfig;

    public KaptchaTextCreator(CaptchaConfig captchaConfig) {
        this.captchaConfig = captchaConfig;
    }

    /**
     * 获取验证码生产类
     *
     * @return /
     */
    public Captcha getCaptcha() {
        if (Objects.isNull(captchaConfig)) {
            if (Objects.isNull(captchaConfig.getCodeType())) {
                captchaConfig.setCodeType(CaptchaEnum.arithmetic);
            }
        }
        return switchCaptcha(captchaConfig);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param captchaConfig 验证码配置信息
     * @return /
     */
    private Captcha switchCaptcha(CaptchaConfig captchaConfig) {
        Captcha captcha;
        synchronized (this) {
            switch (captchaConfig.getCodeType()) {
                case arithmetic:
                    // 算术类型 https://gitee.com/whvse/EasyCaptcha
                    captcha = new ArithmeticCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    // 几位数运算，默认是两位
                    captcha.setLen(captchaConfig.getLength());
                    break;
                case chinese:
                    captcha = new ChineseCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                case chinese_gif:
                    captcha = new ChineseGifCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                case gif:
                    captcha = new GifCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                case spec:
                    captcha = new SpecCaptcha(captchaConfig.getWidth(), captchaConfig.getHeight());
                    captcha.setLen(captchaConfig.getLength());
                    break;
                default:
                    throw new IllegalArgumentException("验证码配置信息错误！正确配置查看 CaptchaEnum ");
            }
        }
        if(StringUtils.isNotBlank(captchaConfig.getFontName())){
            captcha.setFont(new Font(captchaConfig.getFontName(), Font.PLAIN, captchaConfig.getFontSize()));
        }
        return captcha;
    }
}