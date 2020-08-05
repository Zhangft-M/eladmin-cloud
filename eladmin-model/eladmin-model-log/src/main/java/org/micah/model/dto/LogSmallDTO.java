package org.micah.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author micah
 */
@Data
public class LogSmallDTO implements Serializable {

    private static final long serialVersionUID = 8464175762998662090L;

    private String description;

    private String requestIp;

    private Long time;

    private String address;

    private String browser;

    private LocalDateTime createTime;
}