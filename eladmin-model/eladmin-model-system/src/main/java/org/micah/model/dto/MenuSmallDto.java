package org.micah.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-16 18:28
 **/
@Setter
@Getter
public class MenuSmallDto implements Serializable {

    private static final long serialVersionUID = -9037486687224404916L;

    private Long id;

    private String permission;
}
