package org.micah.gateway.handler;

import lombok.RequiredArgsConstructor;
import org.micah.gateway.entity.GatewayEntity;
import org.micah.gateway.service.IGateWayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * @program: eladmin-cloud
 * @description: 路由管理处理器
 * @author: MicahZhang
 * @create: 2020-07-29 16:36
 **/
@RestController
@RequestMapping("router")
@RequiredArgsConstructor
public class RouterHandler {

    private final IGateWayService gateWayService;


    /**
     * 查询所有的路由策略
     * @return
     */
    @GetMapping
    public Mono<ResponseEntity<List<GatewayEntity>>> listRouters(){
        List<GatewayEntity> list = this.gateWayService.list();
        return Mono.just(new ResponseEntity<>(list,HttpStatus.OK));
    }

    /**
     * 更新路由策略
     */
    @PutMapping
    public Mono<ResponseEntity<String>> updateRouters(@RequestBody GatewayEntity gatewayEntity){
        boolean b = this.gateWayService.updateById(gatewayEntity);
        if (!b){
            return Mono.just(new ResponseEntity<>("更新失败",HttpStatus.INTERNAL_SERVER_ERROR));
        }
        this.gateWayService.initData();
        return Mono.just(new ResponseEntity<>("更新成功",HttpStatus.CREATED));
    }

    /**
     * 更新路由策略
     */
    @PostMapping
    public Mono<ResponseEntity<String>> createRouters(@RequestBody GatewayEntity gatewayEntity){
        boolean b = this.gateWayService.save(gatewayEntity);
        if (!b){
            return Mono.just(new ResponseEntity<>("添加失败",HttpStatus.INTERNAL_SERVER_ERROR));
        }
        this.gateWayService.initData();
        return Mono.just(new ResponseEntity<>("添加成功",HttpStatus.CREATED));
    }

    /**
     * 删除路由策略
     */
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<String>> deleteRouters(@PathVariable String id){
        boolean b = this.gateWayService.removeById(id);
        if (!b){
            return Mono.just(new ResponseEntity<>("删除失败",HttpStatus.INTERNAL_SERVER_ERROR));
        }
        this.gateWayService.initData();
        return Mono.just(new ResponseEntity<>("删除成功",HttpStatus.CREATED));
    }




}
