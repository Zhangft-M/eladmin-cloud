package org.micah.model.mapstruct;

import org.micah.core.base.BaseMapStruct;
import org.micah.model.App;
import org.micah.model.dto.AppDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Micah
* @date 2020-09-03
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppMapStruct extends BaseMapStruct<AppDto,App> {

}