package org.micah.gateway.handler;

import lombok.RequiredArgsConstructor;
import org.micah.core.web.page.PageResult;
import org.micah.gateway.entity.Predicate;
import org.micah.gateway.service.IPredicateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:13
 **/
@RestController
@RequestMapping("/router/predicate")
@RequiredArgsConstructor
public class PredicateHandler {

    private final IPredicateService predicateService;

    /**
     * 查询所有的路由策略
     * @return
     */
    @GetMapping
    public Mono<ResponseEntity<PageResult>> listRouters(){
        List<Predicate> list = this.predicateService.list();
        return Mono.just(new ResponseEntity<>(PageResult.success((long) list.size(),list), HttpStatus.OK));
    }
}
