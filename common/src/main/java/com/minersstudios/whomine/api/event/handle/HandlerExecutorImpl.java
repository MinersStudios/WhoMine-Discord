package com.minersstudios.whomine.api.event.handle;

import com.minersstudios.whomine.api.event.EventContainer;
import com.minersstudios.whomine.api.event.EventListener;
import com.minersstudios.whomine.api.listener.handler.HandlerParams;
import com.minersstudios.whomine.api.order.Order;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Immutable
final class HandlerExecutorImpl<O extends Order<O>> implements HandlerExecutor<O> {
    private static final Logger LOGGER = Logger.getLogger(HandlerExecutor.class.getSimpleName());

    private final EventExecutor handler;
    private final HandlerParams<O> params;

    HandlerExecutorImpl(
            final @NotNull EventExecutor handler,
            final @NotNull HandlerParams<O> params
    ) {
        this.handler = handler;
        this.params = params;
    }

    @Override
    public @NotNull EventExecutor getHandler() {
        return this.handler;
    }

    @Override
    public @NotNull HandlerParams<O> getParams() {
        return this.params;
    }

    @Override
    public @NotNull O getOrder() {
        return this.getParams().getOrder();
    }

    @Override
    public void execute(
            final @NotNull EventListener<?, ?> listener,
            final @NotNull EventContainer<?, ?> container
    ) {
        try {
            this.handler.execute(listener, container);
        } catch (final Throwable e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Failed to execute event: " + container,
                    e
            );
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.handler.hashCode();
        result = prime * result + this.params.hashCode();

        return result;
    }

    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof HandlerExecutorImpl<?> other
                        && this.handler.equals(other.handler)
                        && this.params.equals(other.params)
                );
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{'
                + "handler=" + this.handler
                + ", params=" + this.params
                + '}';
    }
}
