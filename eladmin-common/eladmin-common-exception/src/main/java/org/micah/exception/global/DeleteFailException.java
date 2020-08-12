package org.micah.exception.global;

/**
 * @program: eladmin-cloud
 * @description: 删除失败
 * @author: Micah
 * @create: 2020-08-09 13:54
 **/
public class DeleteFailException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DeleteFailException(String message) {
        super(message);
    }
}
