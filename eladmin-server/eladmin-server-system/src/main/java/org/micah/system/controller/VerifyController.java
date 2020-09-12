package org.micah.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.core.util.enums.CodeBiEnum;
import org.micah.core.util.enums.CodeEnum;
import org.micah.system.service.IVerifyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
@Api(tags = "系统：验证码管理")
public class VerifyController {

    private final IVerifyService verificationCodeService;


    @PostMapping(value = "/resetEmail")
    @ApiOperation("重置邮箱，发送验证码")
    public ResponseEntity<Void> resetEmail(@RequestParam String tos){
        this.verificationCodeService.sendEmail(tos,CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/email/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public ResponseEntity<Void> resetPass(@RequestParam String tos){
        this.verificationCodeService.sendEmail(tos,CodeEnum.PHONE_RESET_PWD_CODE.getKey());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public ResponseEntity<Object> validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi){
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)){
            case ONE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email ,code);
                break;
            case TWO:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email ,code);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}