package com.minersstudios.wholib.velocity.utility;

import com.minersstudios.wholib.event.EventOrder;
import com.velocitypowered.api.event.PostOrder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ApiConverter {

    @Contract(" -> fail")
    private ApiConverter() throws AssertionError {
        throw new AssertionError("Utility class");
    }

    public static @NotNull PostOrder apiToVelocity(final @NotNull EventOrder order) {
        return switch (order) {
            case LOWEST  -> PostOrder.FIRST;
            case LOW     -> PostOrder.EARLY;
            case NORMAL  -> PostOrder.NORMAL;
            case HIGH    -> PostOrder.LATE;
            case HIGHEST -> PostOrder.LAST;
            case CUSTOM  -> PostOrder.CUSTOM;
        };
    }

    public static @NotNull EventOrder velocityToApi(final @NotNull PostOrder order) {
        return switch (order) {
            case FIRST  -> EventOrder.LOWEST;
            case EARLY  -> EventOrder.LOW;
            case NORMAL -> EventOrder.NORMAL;
            case LATE   -> EventOrder.HIGH;
            case LAST   -> EventOrder.HIGHEST;
            case CUSTOM -> EventOrder.CUSTOM;
        };
    }
}
