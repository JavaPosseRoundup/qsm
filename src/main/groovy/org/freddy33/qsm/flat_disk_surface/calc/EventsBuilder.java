package org.freddy33.qsm.flat_disk_surface.calc;

/**
 * User: freds
 * Date: 5/31/12
 * Time: 11:12 PM
 */
interface EventsBuilder {
    public EventFlat[] createEvents(EventBlockFlat from, double size);
}
