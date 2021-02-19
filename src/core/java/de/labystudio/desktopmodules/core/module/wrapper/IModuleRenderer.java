package de.labystudio.desktopmodules.core.module.wrapper;

import de.labystudio.desktopmodules.core.renderer.IScreenBounds;

/**
 * Module renderer interface
 *
 * @author LabyStudio
 */
public interface IModuleRenderer {

    /**
     * Request a new render frame
     */
    void requestFrame();

    /**
     * Close the window
     */
    void close();

    /**
     * Get absolute module x position
     *
     * @return Absolute x position
     */
    int getX();

    /**
     * Get absolute module y position
     *
     * @return Absolute y position
     */
    int getY();

    /**
     * Get module width
     *
     * @return Module width
     */
    int getWidth();

    /**
     * Get module height
     *
     * @return Module height
     */
    int getHeight();

    /**
     * Change the location of the module
     *
     * @param x New absolute x position
     * @param y New absolute y position
     */
    void setLocation(int x, int y);

    /**
     * Get the total bounds of all monitors
     *
     * @return Entire display bounds
     */
    IScreenBounds getScreenBounds();
}