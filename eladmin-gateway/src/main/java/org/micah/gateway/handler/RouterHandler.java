package org.micah.gateway.handler;


import cn.hutool.db.Page;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Router;
import org.micah.gateway.entity.query.RouterQueryCriteria;
import org.micah.gateway.service.IRouterService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


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
    public Mono<ResponseEntity<PageResult>> listRouters(RouterQueryCriteria criteria ,@RequestParam int page, @RequestParam int size, @RequestParam String sort){
        PageResult list = this.routerService.queryPage(criteria, page,size,sort);
        // System.out.println(list);
        return Mono.just(new ResponseEntity<>(list,HttpStatus.OK));
    }

    /**
     * 根据id查询路由策略
     * @return
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<Router>> getById(@PathVariable Long id){
        Router router = this.routerService.selectById(id);
        // System.out.println(list);
        return Mono.just(new ResponseEntity<>(router,HttpStatus.OK));
    }

    /**
     * 更新路由策略
     */
    @PutMapping
    public Mono<ResponseEntity<String>> updateRouters(@RequestBody Router router){
        boolean b = this.routerService.updateById(router);
        if (!b){
            return Mono.just(new ResponseEntity<>("更新失败",HttpStatus.INTERNAL_SERVER_ERROR));
        }
        this.routerService.initData();
        return Mono.just(new ResponseEntity<>("更新成功",HttpStatus.CREATED));
    }

    /**
     * 更新路由断言
     */
    @PutMapping("predicate")
    public Mono<ResponseEntity<Void>> updateRouterPredicate(@RequestBody Router router){
       this.routerService.updateRouterPredicate(router);
       this.routerService.initData();
       return Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * 更新路由过滤器
     */
    @PutMapping("filter")
    public Mono<ResponseEntity<Void>> updateRouterFilter(@RequestBody Router router){
        this.routerService.updateRouterFilter(router);
        this.routerService.initData();
        return Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT));
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
    @DeleteMapping
    public Mono<ResponseEntity<String>> deleteRouters(@RequestBody Set<Long> ids){
        this.routerService.deleteByIds(ids);
        this.routerService.initData();
        return Mono.just(new ResponseEntity<>("删除成功",HttpStatus.CREATED));
    }




}
