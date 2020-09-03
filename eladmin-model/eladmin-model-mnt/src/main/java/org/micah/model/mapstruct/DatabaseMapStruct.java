package org.micah.model.mapstruct;

import org.micah.core.base.BaseMapStruct;
import org.micah.model.Database;
import org.micah.model.dto.DatabaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Micah
* @date 2020-09-03
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseMapStruct extends BaseMapStruct<DatabaseDto, Database> {

}