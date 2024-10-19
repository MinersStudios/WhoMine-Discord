package com.minersstudios.wholib.discord;

import com.minersstudios.wholib.listener.Listener;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordListener<E extends GenericEvent>
        extends ListenerAdapter
        implements Listener<Class<E>> {

    @Override
    public @NotNull Class<E> getKey() {
        return null;
    }
}
