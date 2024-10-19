package com.minersstudios.whomine.listener.event.block;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.params.NoteBlockData;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import org.bukkit.Instrument;
import org.bukkit.Note;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
