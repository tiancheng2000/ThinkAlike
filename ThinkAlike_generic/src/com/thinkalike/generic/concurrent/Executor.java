package com.thinkalike.generic.concurrent;

import java.util.concurrent.Callable;

public interface Executor<T> {

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the <tt>Executor</tt> implementation.
     *
     * @param task the runnable task
     * @param result result of the runnable task should be bound with this variable
     * @throws NullPointerException if command is null
     */
    void execute(Runnable task, T result);

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the <tt>Executor</tt> implementation.
     *
     * @param task the runnable task
     * @throws NullPointerException if command is null
     */
    void execute(Callable<T> task);
    
}
