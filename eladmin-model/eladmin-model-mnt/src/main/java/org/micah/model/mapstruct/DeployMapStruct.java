package org.micah.model.mapstruct;

import org.micah.core.base.BaseMapStruct;
import org.micah.model.Deploy;
import org.micah.model.dto.DeployDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Micah
* @date 2020-09-03
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,uses = {AppMapStruct.class, ServerDeployMapStruct.class})
public interface DeployMapStruct extends BaseMapStruct<DeployDto, Deploy> {

}