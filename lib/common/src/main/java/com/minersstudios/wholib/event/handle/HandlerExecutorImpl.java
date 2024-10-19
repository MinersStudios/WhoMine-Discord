package com.minersstudios.wholib.event.handle;

import com.minersstudios.wholib.listener.handler.HandlerParams;
import com.minersstudios.wholib.order.Order;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

@ApiStatus.Internal
@Immutable
public abstract class HandlerExecutorImpl<O extends Order<O>> implements HandlerExecutor<O> {

    private final HandlerParams<O> params;

    protected HandlerExecutorImpl(final @NotNull HandlerParams<O> params) {
        this.params = params;
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
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + "{params=" + this.params + '}';
    }
}
