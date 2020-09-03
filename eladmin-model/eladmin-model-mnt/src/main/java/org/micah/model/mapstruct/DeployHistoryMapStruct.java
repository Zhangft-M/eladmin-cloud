package org.micah.model.mapstruct;

import org.micah.core.base.BaseMapStruct;
import org.micah.model.DeployHistory;
import org.micah.model.dto.DeployHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Micah
* @date 2020-09-03
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployHistoryMapStruct extends BaseMapStruct<DeployHistoryDto, DeployHistory> {

}