package org.micah.gateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.gateway.entity.Filter;
import org.micah.gateway.mapper.FilterMapper;
import org.micah.gateway.service.IFilterService;
import org.springframework.stereotype.Service;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-28 18:20
 **/
@Service
public class FilterServiceImpl extends ServiceImpl<FilterMapper, Filter> implements IFilterService {
}
