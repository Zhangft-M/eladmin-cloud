package org.micah.gateway.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.CaptchaConstants;
import org.micah.gateway.config.CaptchaConfig;
import org.micah.gateway.config.KaptchaTextCreator;
import org.micah.gateway.exception.CaptchaException;
import org.micah.gateway.service.IValidateCodeService;
import org.micah.redis.util.RedisUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: eladmin-cloud
 * @description: 验证码业务处理实现类
 * @author: Micah
 * @create: 2020-07-29 17:22
 **/
@Service
@Slf4j
public class ValidateCodeService implements IValidateCodeService {


    private final RedisUtils redisUtils;

    private final CaptchaConfig captchaConfig;

    private final KaptchaTextCreator kaptchaTextCreator;

    public ValidateCodeService(RedisUtils redisUtils, CaptchaConfig captchaConfig, KaptchaTextCreator kaptchaTextCreator) {
        this.redisUtils = redisUtils;
        this.captchaConfig = captchaConfig;
        this.kaptchaTextCreator = kaptchaTextCreator;
    }


    @Override
    public Map<String,Object> createCapcha() throws IOException, CaptchaException {
        // 获取验证码
        Captcha captcha = this.kaptchaTextCreator.getCaptcha();
        // 生成一个唯一的id
        String uuid = CaptchaConstants.CAPTCHA_CODE_KEY+ IdUtil.simpleUUID();
        // 保存到redis中
        this.redisUtils.set(uuid,captcha.text(), CaptchaConstants.CAPTCHA_EXPIRATION,TimeUnit.MINUTES);
        Map<String,Object> map = new HashMap<>();
        map.put("uuid",uuid);
        map.put("img", captcha.toBase64());
        return map;
    }

    @Override
    public void checkCapcha(String code, String codeId) throws CaptchaException {
        if (StrUtil.isBlank(code))
        {
            throw new CaptchaException("验证码不能为空");
        }
        if (StrUtil.isBlank(codeId))
        {
            throw new CaptchaException("验证码已失效");
        }
        String captcha = (String) redisUtils.get(codeId);
        redisUtils.del(codeId);

        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException("验证码错误");
        }
    }
}
