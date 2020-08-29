package org.micah.gateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.gateway.entity.Predicate;
import org.micah.gateway.mapper.PredicateMapper;
import org.micah.gateway.service.IPredicateService;
import org.springframework.stereotype.Service;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:21
 **/
@Service
public class PredicateServiceImpl extends ServiceImpl<PredicateMapper, Predicate> implements IPredicateService {
}
