package org.micah.gateway.handler;

import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Filter;
import org.micah.gateway.entity.Predicate;
import org.micah.gateway.entity.query.FilterQueryCriteria;
import org.micah.gateway.service.IFilterService;
import org.micah.gateway.service.IPredicateService;
import org.micah.gateway.service.IRouterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:18
 **/
@RestController
@RequestMapping("/router/filters")
@RequiredArgsConstructor
public class FilterHandler {

    private final IFilterService filterService;

    private final IRouterService routerService;

    /**
     * 查询所有的过滤策略
     * @return
     */
    @GetMapping("all")
    public Mono<ResponseEntity<PageResult>> listRouters(){
        List<Filter> list = this.filterService.list();
        return Mono.just(new ResponseEntity<>(PageResult.success((long) list.size(),list), HttpStatus.OK));
    }


    @GetMapping
    public ResponseEntity<PageResult> query(FilterQueryCriteria filterCriteria, @RequestParam int page,@RequestParam int size,@RequestParam String sort){
        return new ResponseEntity<>(this.filterService.queryAll(filterCriteria,page,size,sort),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Validated @RequestBody Filter resource){
        if(resource.getFilterId() != null){
            throw new IllegalArgumentException("新的数据id不为空");
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateFilter(@Validated @RequestBody Filter resources){
        this.filterService.updateFilter(resources);
        this.routerService.initData();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Set<Long> ids) {
        this.filterService.deleteAll(ids);
        this.routerService.initData();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
