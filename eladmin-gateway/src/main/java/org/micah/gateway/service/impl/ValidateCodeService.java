package org.micah.gateway.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.code.kaptcha.Producer;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micah.core.constant.Constants;
import org.micah.gateway.exception.CaptchaException;
import org.micah.gateway.service.IValidateCodeService;
import org.micah.redis.util.RedisUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: eladmin-cloud
 * @description: 验证码业务处理实现类
 * @author: Micah
 * @create: 2020-07-29 17:22
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ValidateCodeService implements IValidateCodeService {

    private final Producer producer;
    private final RedisUtils redisUtils;


    @SuppressWarnings("rawtypes")
    @Override
    public ResponseEntity createCapcha() throws IOException, CaptchaException {

        // 生成验证码
        String text = producer.createText();
        // 生成含前面表达式的验证码
        String capStr = text.substring(0, text.lastIndexOf("@"));
        // 生成含有所有内容的验证码，包含表达式的结果
        String verifyCode = text.substring(text.lastIndexOf("@") + 1);
        BufferedImage image = producer.createImage(capStr);

        // 生成验证码的唯一标识key
        String idStr = IdWorker.getIdStr();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + idStr;
        // 将完整的验证码存在redis中
        redisUtils.set(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流对象,用来转换BufferedImage对象
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return ResponseEntity.ok(e.getMessage());
        }

        HashMap<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("uuid", idStr);
        map.put("img", Base64.encode(os.toByteArray()));
        return ResponseEntity.ok(map);

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
        String verifyKey = Constants.CAPTCHA_CODE_KEY + codeId;
        String captcha = (String) redisUtils.get(verifyKey);
        redisUtils.del(verifyKey);

        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException("验证码错误");
        }
    }
}
