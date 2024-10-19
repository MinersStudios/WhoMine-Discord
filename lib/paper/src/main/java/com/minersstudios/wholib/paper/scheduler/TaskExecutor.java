package com.minersstudios.wholib.paper.scheduler;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents the scheduler owner that can run tasks
 */
public interface TaskExecutor {

    /**
     * Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to ensure the thread-safety of asynchronous tasks.
     * <br>
     * Returns a task that will run asynchronously.
     *
     * @param task The task to be run
     * @return A BukkitTask that contains the id number
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    @NotNull BukkitTask runTaskAsync(final @NotNull Runnable task);

    /**
     * Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to ensure the thread-safety of asynchronous tasks.
     * <br>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param task   The task to be run
     * @param delay  The ticks to wait before running task for the first time
     * @param period The ticks to wait between runs
     * @return A BukkitTask that contains the id number
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    @NotNull BukkitTask runTaskTimerAsync(
            final @NotNull Runnable task,
            final long delay,
            final long period
    );

    /**
     * Returns a task that will run on the next server tick
     *
     * @param task The task to be run
     * @return A BukkitTask that contains the id number
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    @NotNull BukkitTask runTask(final @NotNull Runnable task);

    /**
     * Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to ensure the thread-safety of asynchronous tasks.
     * <br>
     * Returns a task that will run asynchronously after the specified number
     * of server ticks.
     *
     * @param task  The task to be run
     * @param delay The ticks to wait before running the task
     * @return A BukkitTask that contains the id number
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    @NotNull BukkitTask runTaskLaterAsync(
            final @NotNull Runnable task,
            final long delay
    );

    /**
     * Returns a task that will run after the specified number of server ticks
     *
     * @param task  The task to be run
     * @param delay The ticks to wait before running the task
     * @return A BukkitTask that contains the id number
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    @NotNull BukkitTask runTaskLater(
            final @NotNull Runnable task,
            final long delay
    );

    /**
     * Returns a task that will repeatedly run until cancelled, starting after
     * the specified number of server ticks
     *
     * @param task   The task to be run
     * @param delay  The ticks to wait before running the task
     * @param period The ticks to wait between runs
     * @return A BukkitTask that contains the id number
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    @NotNull BukkitTask runTaskTimer(
            final @NotNull Runnable task,
            final long delay,
            final long period
    );

    /**
     * Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to ensure the thread-safety of asynchronous tasks.
     * <br>
     * Returns a task that will run asynchronously.
     *
     * @param task The task to be run
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    void runTaskAsync(final @NotNull Consumer<BukkitTask> task);

    /**
     * Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to ensure the thread-safety of asynchronous tasks.
     * <br>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param task   The task to be run
     * @param delay  The ticks to wait before running task for the first time
     * @param period The ticks to wait between runs
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    void runTaskTimerAsync(
            final @NotNull Consumer<BukkitTask> task,
            final long delay,
            final long period
    );

    /**
     * Returns a task that will run on the next server tick
     *
     * @param task The task to be run
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    void runTask(final @NotNull Consumer<BukkitTask> task);

    /**
     * Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to ensure the thread-safety of asynchronous tasks.
     * <br>
     * Returns a task that will run asynchronously after the specified number
     * of server ticks.
     *
     * @param task  The task to be run
     * @param delay The ticks to wait before running the task
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    void runTaskLaterAsync(
            final @NotNull Consumer<BukkitTask> task,
            final long delay
    );

    /**
     * Returns a task that will run after the specified number of server ticks
     *
     * @param task  The task to be run
     * @param delay The ticks to wait before running the task
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    void runTaskLater(
            final @NotNull Consumer<BukkitTask> task,
            final long delay
    );

    /**
     * Returns a task that will repeatedly run until cancelled, starting after
     * the specified number of server ticks
     *
     * @param task   The task to be run
     * @param delay  The ticks to wait before running the task
     * @param period The ticks to wait between runs
     * @throws IllegalArgumentException If the current plugin is not enabled
     */
    void runTaskTimer(
            final @NotNull Consumer<BukkitTask> task,
            final long delay,
            final long period
    );
}
