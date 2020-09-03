package org.micah.model.mapstruct;

import org.micah.core.base.BaseMapStruct;
import org.micah.model.ServerDeploy;
import org.micah.model.dto.ServerDeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author micah
* @date 2020-09-03
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerDeployMapStruct extends BaseMapStruct<ServerDeployDto, ServerDeploy> {

}