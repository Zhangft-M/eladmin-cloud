package org.micah.exception.global.util;

import cn.hutool.core.util.ObjectUtil;
import org.micah.exception.global.BadRequestException;

public class ValidationUtil{

    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " 不存在: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }

}