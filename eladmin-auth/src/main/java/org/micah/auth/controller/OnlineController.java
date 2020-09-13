package org.micah.auth.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.micah.auth.service.IOnlineUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/online")
public class OnlineController {

    private final IOnlineUserService onlineUserService;

    @ApiOperation("查询在线用户")
    @GetMapping
    public ResponseEntity<Object> query(String filter, Pageable pageable){
        return new ResponseEntity<>(onlineUserService.getAll(filter, pageable),HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, String filter) throws IOException {
        // onlineUserService.download(onlineUserService.getAll(filter), response);
    }

    @ApiOperation("踢出用户")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids) throws Exception {
        for (Long id : ids) {
            // 解密Key
            onlineUserService.kickOut(id,null);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}