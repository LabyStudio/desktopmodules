package de.labystudio.desktopmodules.core.renderer.wrapper;

/**
 * Screen bounds wrapper
 *
 * @author LabyStudio
 */
public interface IScreenBounds {

    /**
     * Minimum screen position x
     *
     * @return Absolute x position
     */
    int getMinX();

    /**
     * Minimum screen position y
     *
     * @return Absolute y position
     */
    int getMinY();

    /**
     * Maximum screen position x
     *
     * @return Absolute x position
     */
    int getMaxX();

    /**
     * Maximum screen position y
     *
     * @return Absolute y position
     */
    int getMaxY();
}
