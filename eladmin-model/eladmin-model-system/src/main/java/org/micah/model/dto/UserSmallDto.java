package org.micah.model.dto;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-16 18:42
 **/
public class UserSmallDto {

    private Long id;

    private String username;

    private String password;

    private List<MenuSmallDto> menuSmallDtos;

    private List<RoleSmallDto> roleSmallDtos;
}
