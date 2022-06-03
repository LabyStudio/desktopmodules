package de.labystudio.desktopmodules.core.module;

import com.google.gson.JsonObject;
import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.render.IModuleRenderer;
import de.labystudio.desktopmodules.core.module.render.IRenderCallback;
import de.labystudio.desktopmodules.core.renderer.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.IScreenBounds;
import de.labystudio.desktopmodules.core.renderer.font.Font;
import de.labystudio.desktopmodules.core.renderer.font.FontStyle;
import de.labystudio.desktopmodules.core.renderer.swing.SwingModuleRenderer;
import de.labystudio.desktopmodules.core.renderer.swing.SwingScreenBounds;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author LabyStudio
 */
public abstract class Module<T extends Addon> implements IRenderCallback {

    protected final Font DEFAULT_FONT = new Font("Dialog", FontStyle.PLAIN, 12);

    protected int width;
    protected int height;

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

        this.icon = addon.getDesktopModules().getTextureLoader().load(getIconPath());
        this.moduleRenderer = createRenderer();
    }

    /**
     * Called when enabling the module
     */
    public void onEnable() {
        // No implementation
    }

    /**
     * Called when disabling the module
     */
    public void onDisable() {
        // No implementation
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
        this.moduleRenderer.setVisible(this.enabled = (!config.has("enabled") || config.get("enabled").getAsBoolean()));

        // Get target screen bounds
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        IScreenBounds bounds = new SwingScreenBounds(mouseLocation.x, mouseLocation.y);

        // Get center location of the target monitor for the default module position
        int centerX = bounds.getMinX() + (bounds.getMaxX() - bounds.getMinX()) / 2;
        int centerY = bounds.getMinY() + (bounds.getMaxY() - bounds.getMinY()) / 2;

        // Load module position
        int x = config.has("x") ? config.get("x").getAsInt() : centerX - this.width / 2;
        int y = config.has("y") ? config.get("y").getAsInt() : centerY - this.height / 2;
        this.moduleRenderer.setLocation(x, y);

        // Update right bound state after changing the location of the module
        updateRightBoundState();
    }

    /**
     * Change the visibility of a module. It will also handle the loading and saving of the module config.
     *
     * @param enabled Module visibility state
     */
    public void setEnabled(boolean enabled) {
        boolean hadActiveModules = this.addon.hasActiveModules();

        try {
            // Load entire config first
            if (!hadActiveModules) {
                this.addon.loadConfig();
                onLoadConfig(this.addon.getModuleConfig(this));
            }

            // Update enabled state of this module
            this.enabled = enabled;
            this.moduleRenderer.setVisible(enabled);

            // Save the change
            onSaveConfig(this.config);
            this.addon.saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Call visibility change
        this.addon.onModuleVisibilityChanged(this, enabled, hadActiveModules);
    }

    /**
     * Update the size of the module
     *
     * @param width  New with of the module
     * @param height New height of the module
     */
    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;

        // Update renderer size
        this.moduleRenderer.setSize(width, height);
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
    public void onMouseDragged(int x, int y, int mouseButton) {
        if (this.dragging) {
            int moduleX = this.moduleRenderer.getX() + (x - this.lastMouseClickOffsetX);
            int moduleY = this.moduleRenderer.getY() + (y - this.lastMouseClickOffsetY);

            // Keep the module in bounds
            IScreenBounds screenBounds = this.moduleRenderer.getScreenBounds();
            if (moduleX < screenBounds.getMinX())
                moduleX = screenBounds.getMinX();
            if (moduleX > screenBounds.getMaxX() - this.moduleRenderer.getWidth())
                moduleX = screenBounds.getMaxX() - this.moduleRenderer.getWidth();
            if (moduleY < screenBounds.getMinY())
                moduleY = screenBounds.getMinY();
            if (moduleY > screenBounds.getMaxY() - this.moduleRenderer.getHeight())
                moduleY = screenBounds.getMaxY() - this.moduleRenderer.getHeight();

            // Update module location
            this.moduleRenderer.setLocation(moduleX, moduleY);

            updateRightBoundState();
        }
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
     * Called on each application tick
     */
    public void onTick() {
        // No implementation
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
     * Is the module currently dragged by the mouse
     *
     * @return module is currently dragged
     */
    public boolean isDragging() {
        return this.dragging;
    }

    /**
     * Is mouse over a rendered part of the module
     *
     * @return Mouse is over the module
     */
    public boolean isMouseOver() {
        return this.moduleRenderer.isMouseOver();
    }

    /**
     * Get module renderer interface of module
     *
     * @return The module renderer interface
     */
    public IModuleRenderer getModuleRenderer() {
        return moduleRenderer;
    }

    @Override
    public void onMouseReleased(int x, int y, int mouseButton) {
        this.dragging = false;
    }

    @Override
    public void onMouseScroll(int x, int y, int scrollAmount) {
        // No implementation
    }

    @Deprecated
    @Override
    public void onRender(IRenderContext context, int width, int height) {
        // Deprecated
    }

    @Override
    public void onRender(IRenderContext context, int width, int height, int mouseX, int mouseY) {
        // Implementation in Module class will be removed in future versions
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
