package de.labystudio.desktopmodules.core.renderer.wrapper;

/**
 * Callback from the module renderer for each render tick
 *
 * @author LabyStudio
 */
public interface IRenderCallback {

    /**
     * Module render callback.
     *
     * @param context Render context of the module renderer
     * @param width   Module width
     * @param height  Module height
     */
    void onRender(IRenderContext context, int width, int height);

    /**
     * Called on mouse down interaction
     *
     * @param x           Clicked mouse position x
     * @param y           Clicked mouse position y
     * @param mouseButton The pressed mouse button
     */
    void onMousePressed(int x, int y, int mouseButton);

    /**
     * Called on mouse drag interaction
     *
     * @param x           Dragged mouse position x
     * @param y           Dragged mouse position y
     * @param mouseButton The dragged mouse button
     */
    void onMouseDragged(int x, int y, int mouseButton);

    /**
     * Called on mouse release interaction
     *
     * @param x           Released mouse position x
     * @param y           Released mouse position y
     * @param mouseButton The released mouse button
     */
    void onMouseReleased(int x, int y, int mouseButton);

}
