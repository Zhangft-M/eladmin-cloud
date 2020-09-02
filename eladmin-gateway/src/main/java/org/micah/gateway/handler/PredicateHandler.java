package org.micah.gateway.handler;

import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Predicate;
import org.micah.gateway.entity.query.PredicateQueryCriteria;
import org.micah.gateway.service.IPredicateService;
import org.micah.gateway.service.IRouterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:13
 **/
@RestController
@RequestMapping("/router/predicates")
@RequiredArgsConstructor
public class PredicateHandler {

    private final IPredicateService predicateService;

    private final IRouterService routerService;

    /**
     * 查询所有的路由策略
     * @return
     */
    @GetMapping("all")
    public Mono<ResponseEntity<PageResult>> listRouters(){
        List<Predicate> list = this.predicateService.list();
        return Mono.just(new ResponseEntity<>(PageResult.success((long) list.size(),list), HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<PageResult> query(PredicateQueryCriteria predicateCriteria, @RequestParam int page, @RequestParam int size, @RequestParam String sort){
        return new ResponseEntity<>(this.predicateService.queryAll(predicateCriteria,page,size,sort),HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> create(@Validated @RequestBody Predicate resource){
        if(resource.getPredicateId() != null){
            throw new IllegalArgumentException("新的数据id不为空");
        }
        this.predicateService.save(resource);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> updatePredicate(@Validated @RequestBody Predicate resources){
        this.predicateService.updateById(resources);
        this.routerService.initData();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids) {
        this.predicateService.deleteByIds(ids);
        this.routerService.initData();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
