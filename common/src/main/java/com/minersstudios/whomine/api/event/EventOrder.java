package com.minersstudios.whomine.api.event;

/**
 * This enum represents the priority of an event.
 * <p>
 * <b>Handling occurs in the following order :</b>
 * <ol>
 *     <li>{@link #LOWEST}</li>
 *     <li>{@link #LOW}</li>
 *     <li>{@link #NORMAL}</li>
 *     <li>{@link #HIGH}</li>
 *     <li>{@link #HIGHEST}</li>
 *     <li>{@link #CUSTOM}</li>
 * </ol>
 *
 * @see EventHandler
 */
public enum EventOrder {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4),
    CUSTOM(5);

    private final short order;

    /**
     * Event order constructor
     *
     * @param order The order of the event
     */
    EventOrder(final int order) {
        this.order = (short) order;
    }

    /**
     * Returns the order of the event
     *
     * @return The order of the event
     */
    public int getOrder() {
        return order;
    }
}
