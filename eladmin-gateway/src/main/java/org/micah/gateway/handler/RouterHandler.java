package org.micah.gateway.handler;


import org.micah.gateway.entity.Router;
import org.micah.gateway.service.IRouterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;


/**
 * @program: eladmin-cloud
 * @description: 路由管理处理器
 * @author: MicahZhang
 * @create: 2020-07-29 16:36
 **/
@RestController
@RequestMapping("/router")
public class RouterHandler {

    private final IRouterService routerService;

    public RouterHandler(IRouterService routerService) {
        this.routerService = routerService;
    }

    /**
     * 加载路由策略
     * @return
     */
    @GetMapping("load")
    public Mono<ResponseEntity<Valid>> reLoadRouters(){
        this.routerService.initData();
        return Mono.just(new ResponseEntity<>(HttpStatus.OK));
    }


    /**
     * 查询所有的路由策略
     * @return
     */
    @GetMapping
    public Mono<ResponseEntity<List<Router>>> listRouters(){
        List<Router> list = this.routerService.list();
        System.out.println(list);
        return Mono.just(new ResponseEntity<>(list,HttpStatus.OK));
    }

    /**
     * 更新路由策略
     */
    @PutMapping
    public Mono<ResponseEntity<String>> updateRouters(@RequestBody Router gatewayEntity){
        boolean b = this.routerService.updateById(gatewayEntity);
        if (!b){
            return Mono.just(new ResponseEntity<>("更新失败",HttpStatus.INTERNAL_SERVER_ERROR));
        }
        this.routerService.initData();
        return Mono.just(new ResponseEntity<>("更新成功",HttpStatus.CREATED));
    }

    /**
     * 添加路由策略
     */
    @PostMapping
    public Mono<ResponseEntity<String>> createRouters(@RequestBody Router gatewayEntity){
        boolean b = this.routerService.save(gatewayEntity);
        if (!b){
            return Mono.just(new ResponseEntity<>("添加失败",HttpStatus.INTERNAL_SERVER_ERROR));
        }
        this.routerService.initData();
        return Mono.just(new ResponseEntity<>("添加成功",HttpStatus.CREATED));
    }

    /**
     * 删除路由策略
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<String>> deleteRouters(@PathVariable String id){
        boolean b = this.routerService.removeById(id);
        if (!b){
            return Mono.just(new ResponseEntity<>("删除失败",HttpStatus.INTERNAL_SERVER_ERROR));
        }
        this.routerService.initData();
        return Mono.just(new ResponseEntity<>("删除成功",HttpStatus.CREATED));
    }




}
