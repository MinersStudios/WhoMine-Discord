package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.block.params.NoteBlockData;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import org.bukkit.Instrument;
import org.bukkit.Note;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import org.bukkit.event.block.NotePlayEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(NotePlayEvent.class)
public final class NotePlayListener extends PaperEventListener {
    private static final Instrument DEFAULT_INSTRUMENT = NoteBlockData.defaultData().instrument();
    private static final Note DEFAULT_NOTE = NoteBlockData.defaultData().note();

    @CancellableHandler(order = EventOrder.CUSTOM, ignoreCancelled = true)
    public void onNotePlay(final @NotNull PaperEventContainer<NotePlayEvent> container) {
        final NotePlayEvent event = container.getEvent();

        if (
                !(event.getInstrument() == DEFAULT_INSTRUMENT
                && event.getNote().equals(DEFAULT_NOTE))
        ) {
            event.setCancelled(true);
        }
    }
}
