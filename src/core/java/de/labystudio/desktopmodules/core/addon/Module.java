package de.labystudio.desktopmodules.core.addon;

import de.labystudio.desktopmodules.core.renderer.wrapper.IModuleRenderer;
import de.labystudio.desktopmodules.core.renderer.wrapper.IRenderCallback;
import de.labystudio.desktopmodules.core.renderer.wrapper.IScreenBounds;

/**
 * @author LabyStudio
 */
public abstract class Module implements IRenderCallback {

    /**
     * The render instance of this module
     */
    protected IModuleRenderer moduleRenderer;

    /**
     * The addon which the module belongs to
     */
    protected Addon addon;

    /**
     * Last clicked mouse x position relative to the module position
     */
    private int lastMouseClickOffsetX;

    /**
     * Last clicked mouse y position relative to the module position
     */
    private int lastMouseClickOffsetY;

    /**
     * Module mouse dragging state
     */
    private boolean dragging = false;

    public abstract void loadTextures(Addon addon);

    /**
     * Called on first class load.
     * Override this method instead of creating a constructor
     */
    public void onInitialize(Addon addon) {
        this.addon = addon;
        this.moduleRenderer = createRenderer();
    }

    /**
     * Create renderer for this module
     *
     * @return The created implementation for the model render interface
     */
    protected abstract IModuleRenderer createRenderer();

    /**
     * Called on each application tick
     */
    public abstract void onTick();

    /**
     * Get module renderer interface of module
     *
     * @return The module renderer interface
     */
    public IModuleRenderer getModuleRenderer() {
        return moduleRenderer;
    }

    @Override
    public void onMousePressed(int x, int y, int mouseButton) {
        if (!this.dragging) {
            this.lastMouseClickOffsetX = x;
            this.lastMouseClickOffsetY = y;
            this.dragging = true;
        }
    }

    @Override
    public void onMouseDragged(int offsetX, int offsetY, int mouseButton) {
        if (this.dragging) {
            int x = this.moduleRenderer.getX() + (offsetX - this.lastMouseClickOffsetX);
            int y = this.moduleRenderer.getY() + (offsetY - this.lastMouseClickOffsetY);

            // Keep the module in bounds
            IScreenBounds screenBounds = this.moduleRenderer.getScreenBounds();
            if (x < screenBounds.getMinX())
                x = screenBounds.getMinX();
            if (x > screenBounds.getMaxX() - this.moduleRenderer.getWidth())
                x = screenBounds.getMaxX() - this.moduleRenderer.getWidth();
            if (y < screenBounds.getMinY())
                y = screenBounds.getMinY();
            if (y > screenBounds.getMaxY() - this.moduleRenderer.getHeight())
                y = screenBounds.getMaxY() - this.moduleRenderer.getHeight();

            // Update module location
            this.moduleRenderer.setLocation(x, y);
        }
    }

    @Override
    public void onMouseReleased(int x, int y, int mouseButton) {
        this.dragging = false;
    }
}
