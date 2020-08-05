package org.micah.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @program: eladmin-cloud
 * @description: 错误日志传输对象
 * @author: Micah
 * @create: 2020-08-05 15:44
 **/
@Data
public class LogErrorDTO implements Serializable {

    private static final long serialVersionUID = 4557209617190236452L;
    private Long id;

    private String username;

    private String description;

    private String method;

    private String params;

    private String browser;

    private String requestIp;

    private String address;

    private LocalDateTime createTime;
}
