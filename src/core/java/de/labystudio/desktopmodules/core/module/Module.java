package de.labystudio.desktopmodules.core.module;

import com.google.gson.JsonObject;
import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.wrapper.IModuleRenderer;
import de.labystudio.desktopmodules.core.module.wrapper.IRenderCallback;
import de.labystudio.desktopmodules.core.renderer.IScreenBounds;
import de.labystudio.desktopmodules.core.renderer.swing.SwingModuleRenderer;

import java.awt.image.BufferedImage;

/**
 * @author LabyStudio
 */
public abstract class Module<T extends Addon> implements IRenderCallback {

    protected final int width;
    protected final int height;

    /**
     * Module config
     */
    private JsonObject config;

    /**
     * The render instance of this module
     */
    protected IModuleRenderer moduleRenderer;

    /**
     * The addon which the module belongs to
     */
    protected T addon;

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
     * Module is on the right side of the target monitor
     */
    protected boolean rightBound;

    /**
     * The icon of the module
     */
    private BufferedImage icon;

    /**
     * Create module width fixed size
     *
     * @param width  Module width
     * @param height Module height
     */
    public Module(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Called when loading the addon into the application
     *
     * @param addon  The addon that loaded this module
     * @param config The config element of this module
     */
    public void onInitialize(T addon, JsonObject config) {
        this.addon = addon;
        this.config = config;

        this.icon = addon.getDesktopModules().getTextureLoader().loadTexture(getIconPath());
    }

    /**
     * Called before each config save
     *
     * @param config The module config
     */
    public void onSaveConfig(JsonObject config) {
        // Save module visibility state
        config.addProperty("enabled", this.enabled);

        // Save module position
        config.addProperty("x", this.moduleRenderer.getX());
        config.addProperty("y", this.moduleRenderer.getY());
    }

    /**
     * Called when loading the addon
     *
     * @param config The module config
     */
    public void onLoadConfig(JsonObject config) {
        this.config = config;

        // Load module visibility state
        setEnabled(!config.has("enabled") || config.get("enabled").getAsBoolean());

        // Load module position
        int x = config.has("x") ? config.get("x").getAsInt() : 0;
        int y = config.has("y") ? config.get("y").getAsInt() : 0;
        this.moduleRenderer.setLocation(x, y);

        // Update right bound state after changing the location of the module
        updateRightBoundState();
    }

    /**
     * Create renderer for this module
     *
     * @return The created implementation for the model render interface
     */
    protected IModuleRenderer createRenderer() {
        return new SwingModuleRenderer(this, this.width, this.height);
    }

    /**
     * Load all texture of this module
     *
     * @param textureLoader Texture loader to load the texture
     */
    public abstract void loadTextures(TextureLoader textureLoader);

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
     * Is mouse over a rendered part of the module
     *
     * @return Mouse is over the module
     */
    public boolean isMouseOver() {
        return this.moduleRenderer != null && this.moduleRenderer.isMouseOver();
    }

    /**
     * Get module renderer interface of module
     *
     * @return The module renderer interface
     */
    public IModuleRenderer getModuleRenderer() {
        return moduleRenderer;
    }

    /**
     * Create the module renderer or destroy it
     *
     * @param enabled New module visible state
     */
    public void setEnabled(boolean enabled) {
        boolean prevHasActiveModules = this.addon.hasActiveModules();

        // Update enabled state of this module
        this.enabled = enabled;

        if (enabled && this.moduleRenderer == null) {
            // Create module renderer
            this.moduleRenderer = createRenderer();
        } else if (!enabled && this.moduleRenderer != null) {
            // Close the window
            this.moduleRenderer.close();
            this.moduleRenderer = null;
        }

        // Call visibility change
        this.addon.onModuleVisibilityChanged(this, enabled);

        // Enable or disable addon (if all modules are disabled for example)
        if (prevHasActiveModules != this.addon.hasActiveModules()) {
            try {
                if (this.addon.hasActiveModules()) {
                    // Enable addon when at least one modules is enable
                    this.addon.onEnable();
                } else {
                    // Disable addon when all modules are disabled
                    this.addon.onDisable();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the right bound state of the module
     * Right bound is true if the module is on the right side of the target monitor
     */
    private void updateRightBoundState() {
        IScreenBounds targetBounds = this.moduleRenderer.getScreenBoundsOfTargetMonitor();
        this.rightBound = this.moduleRenderer.getX() + this.width / 2 - targetBounds.getMinX() > (targetBounds.getMaxX() - targetBounds.getMinX()) / 2;
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

            updateRightBoundState();
        }
    }

    @Override
    public void onMouseReleased(int x, int y, int mouseButton) {
        this.dragging = false;
    }

    @Override
    public void onMouseScroll(int x, int y, int scrollAmount) {
        // No implementation
    }

    public BufferedImage getIcon() {
        return this.icon;
    }

    public Addon getAddon() {
        return this.addon;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public JsonObject getConfig() {
        return config;
    }
}
