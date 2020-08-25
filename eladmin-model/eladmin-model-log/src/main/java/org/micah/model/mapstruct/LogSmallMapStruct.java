
package org.micah.model.mapstruct;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.micah.core.base.BaseMapStruct;
import org.micah.model.Log;
import org.micah.model.dto.LogSmallDTO;

/**
 * @author Zheng Jie
 * @date 2019-5-22
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogSmallMapStruct extends BaseMapStruct<LogSmallDTO, Log> {

}