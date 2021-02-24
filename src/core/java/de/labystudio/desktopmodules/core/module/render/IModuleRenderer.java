package de.labystudio.desktopmodules.core.module.render;

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
     * Change the visibility of the window
     *
     * @param visible Window is visible
     */
    void setVisible(boolean visible);

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

    /**
     * Get the bounds of the target monitor where the module is in
     *
     * @return Target monitor bounds
     */
    IScreenBounds getScreenBoundsOfTargetMonitor();

    /**
     * Is mouse over a rendered part of the module
     *
     * @return Mouse is over the module
     */
    boolean isMouseOver();


    /**
     * Set the size of the module renderer
     *
     * @param width  Width of the renderer
     * @param height Height of the renderer
     */
    void setSize(int width, int height);
}
