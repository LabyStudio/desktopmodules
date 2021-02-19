package de.labystudio.desktopmodules.core.module;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.wrapper.IModuleRenderer;
import de.labystudio.desktopmodules.core.module.wrapper.IRenderCallback;
import de.labystudio.desktopmodules.core.renderer.IScreenBounds;

import java.awt.image.BufferedImage;

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

    /**
     * Module enabled/visible
     */
    private boolean enabled;

    /**
     * The icon of the module
     */
    private BufferedImage icon;

    /**
     * Load all texture of this module
     *
     * @param textureLoader Texture loader to load the texture
     */
    public abstract void loadTextures(TextureLoader textureLoader);

    /**
     * Called on first class load.
     * Override this method instead of creating a constructor
     */
    public void onInitialize(Addon addon, boolean enabled) {
        this.addon = addon;
        this.icon = addon.getDesktopModules().getTextureLoader().loadTexture(getIconPath());

        // Create a module renderer
        setEnabled(enabled);
    }

    /**
     * Create renderer for this module
     *
     * @return The created implementation for the model render interface
     */
    protected abstract IModuleRenderer createRenderer();

    /**
     * Get the path to the icon of the module
     *
     * @return Resource path of the module icon
     */
    protected abstract String getIconPath();

    /**
     * Get display name of the module
     *
     * @return Module name for the settings gui
     */
    public abstract String getDisplayName();

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

    public Addon getAddon() {
        return this.addon;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Create the module renderer or destroy it
     *
     * @param enabled New module visible state
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled && this.moduleRenderer == null) {
            // Create module renderer
            this.moduleRenderer = createRenderer();
        } else if (this.moduleRenderer != null) {
            // Close the window
            this.moduleRenderer.close();
            this.moduleRenderer = null;
        }

        // Call visibility change
        this.addon.onModuleVisibilityChanged(this, enabled);
    }

    public BufferedImage getIcon() {
        return this.icon;
    }
}
