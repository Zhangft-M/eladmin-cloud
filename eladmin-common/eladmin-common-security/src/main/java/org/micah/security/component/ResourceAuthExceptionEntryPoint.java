package org.micah.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.spi.http.HttpContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: eladmin-cloud
 * @description: 处理匿名访问时候没有权限的异常
 * @author: Micah
 * @create: 2020-08-04 15:39
 **/
@Component
@RequiredArgsConstructor
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/json;charset=utf-8");
        Map<String,Object> result = new HashMap<>(6);
        result.put("code", HttpStatus.UNAUTHORIZED.value());
        if (e!=null){
            result.put("msg","error");
            result.put("data",e.getMessage());
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        writer.append(this.objectMapper.writeValueAsString(result));
    }
}
