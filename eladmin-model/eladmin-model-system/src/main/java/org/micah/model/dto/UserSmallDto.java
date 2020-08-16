package org.micah.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-16 18:42
 **/
@Data
public class UserSmallDto implements Serializable {

    private static final long serialVersionUID = 4105123798157780641L;

    private Long id;

    private String username;

    private String password;

    private Boolean enabled;

    private List<String> roleNames;

    private List<String> permissions;
}
