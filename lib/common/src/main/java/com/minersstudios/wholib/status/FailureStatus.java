package com.minersstudios.wholib.status;

import com.minersstudios.wholib.annotation.StatusKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a failure status.
 * <br>
 * Factory methods for creating a failure status:
 * <ul>
 *     <li>{@link #failure(String, StatusPriority)}</li>
 *     <li>{@link #failureLow(String)}</li>
 *     <li>{@link #failureHigh(String)}</li>
 * </ul>
 *
 * @see Status
 */
@SuppressWarnings("unused")
@Immutable
public class FailureStatus extends ImplStatus {

    protected FailureStatus(
            final @StatusKey @NotNull String key,
            final @NotNull StatusPriority priority
    ) {
        super(key, priority);
    }

    @ApiStatus.OverrideOnly
    @Override
    public void accept(
            final @Nullable Consumer<SuccessStatus> onSuccess,
            final @NotNull Consumer<FailureStatus> onFailure
    ) {
        onFailure.accept(this);
    }

    @ApiStatus.OverrideOnly
    @Override
    public <U> @NotNull U apply(
            final @Nullable Function<SuccessStatus, U> onSuccess,
            final @NotNull Function<FailureStatus, U> onFailure
    ) {
        return onFailure.apply(this);
    }
}
