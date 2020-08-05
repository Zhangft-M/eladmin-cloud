package org.micah.auth.controller;

import lombok.RequiredArgsConstructor;
import org.micah.auth.service.ILogoutService;
import org.micah.core.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @program: eladmin-cloud
 * @description: tokenController主要用来处理注销逻辑
 * @author: Micah
 * @create: 2020-08-03 17:58
 **/
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class LogoutController {


    private final ILogoutService tokenService;

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StringUtils.isEmpty(authHeader)) {
            // 没有token信息，表示已经是注销状态，直接返回ok
            return ResponseEntity.ok().build();
        }
        // 进行注销操作
        Boolean isSuccess = this.tokenService.logout(authHeader);
        if (isSuccess) {
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("注销失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
