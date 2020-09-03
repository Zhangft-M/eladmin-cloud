package ${package}.model.mapstruct;

import org.micah.core.base.BaseMapStruct;
import ${package}.model.${className};
import ${package}.model.dto.${className}Dto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author ${author}
* @date ${date}
**/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${className}MapStruct extends BaseMapStruct<${className}Dto, ${className}> {

}