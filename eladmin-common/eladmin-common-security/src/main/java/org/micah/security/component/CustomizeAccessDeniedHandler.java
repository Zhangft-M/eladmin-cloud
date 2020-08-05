package org.micah.security.component;

import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 自定义访问权限不足异常处理
 * @author: Micah
 * @create: 2020-08-04 19:55
 **/
@Slf4j
@RequiredArgsConstructor
public class CustomizeAccessDeniedHandler extends OAuth2AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    /**
     * 授权拒绝处理，使用R包装
     * @param request request
     * @param response response
     * @param authException authException
     */
    @Override
    @SneakyThrows
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) {
        log.info("授权失败，禁止访问 {}", request.getRequestURI());
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/json;charset=utf-8");
        Map<String,String> result = new HashMap<>(2);
        result.put("msg","权限不足，拒绝访问");
        response.setStatus(HttpStatus.HTTP_FORBIDDEN);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(objectMapper.writeValueAsString(result));
    }
}
