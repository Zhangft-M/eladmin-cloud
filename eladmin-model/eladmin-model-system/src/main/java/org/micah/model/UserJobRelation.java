package org.micah.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-12 14:47
 **/
@Setter
@Getter
public class UserJobRelation implements Serializable {

    private static final long serialVersionUID = 6302335955493076994L;

    private Long userId;

    private Long jobId;

    public UserJobRelation(Long userId, Long jobId) {
        this.userId = userId;
        this.jobId = jobId;
    }

    public UserJobRelation() {
    }
}
