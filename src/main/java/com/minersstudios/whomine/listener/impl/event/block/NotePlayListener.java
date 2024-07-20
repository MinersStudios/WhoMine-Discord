package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.params.NoteBlockData;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.NotePlayEvent;
import org.jetbrains.annotations.NotNull;

public final class NotePlayListener extends EventListener {
    private static final Instrument DEFAULT_INSTRUMENT = NoteBlockData.defaultData().instrument();
    private static final Note DEFAULT_NOTE = NoteBlockData.defaultData().note();

    public NotePlayListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNotePlay(final @NotNull NotePlayEvent event) {
        if (
                !(event.getInstrument() == DEFAULT_INSTRUMENT
                && event.getNote().equals(DEFAULT_NOTE))
        ) {
            event.setCancelled(true);
        }
    }
}
