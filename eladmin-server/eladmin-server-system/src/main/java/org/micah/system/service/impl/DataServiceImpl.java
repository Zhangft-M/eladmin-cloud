package org.micah.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.micah.system.service.IDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-11 18:33
 **/
@Service
public class DataServiceImpl implements IDataService {
    /**
     * 通过用户查询用户拥有的部门数据权限
     *
     * @param byName
     * @return
     */
    @Override
    public List<Long> getDeptIds(Object byName) {
        return null;
    }
}
