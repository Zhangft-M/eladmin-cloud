package org.micah.exception.global;

/**
 * @program: eladmin-cloud
 * @description: 更新失败异常
 * @author: Micah
 * @create: 2020-08-09 13:53
 **/
public class UpdateFailException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UpdateFailException(String message) {
        super(message);
    }
}
