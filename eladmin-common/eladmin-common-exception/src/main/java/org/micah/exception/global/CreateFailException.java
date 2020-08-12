package org.micah.exception.global;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: eladmin-cloud
 * @description: 创建失败异常
 * @author: Micah
 * @create: 2020-08-09 13:49
 **/
public class CreateFailException extends RuntimeException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CreateFailException(String message) {
        super(message);
    }
}
