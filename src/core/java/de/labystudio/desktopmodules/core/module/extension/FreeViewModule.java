package de.labystudio.desktopmodules.core.module.extension;

import com.google.gson.JsonObject;
import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.module.Module;

/**
 * A module extension to drag or zoom around the rendered content
 *
 * @author LabyStudio
 */
public abstract class FreeViewModule<T extends Addon> extends Module<T> {

    protected int zoom;

    protected int offsetX;
    protected int offsetY;

    private boolean dragging;

    private int mouseClickedX;
    private int mouseClickedY;

    /**
     * Create module width fixed size
     *
     * @param width  Module width
     * @param height Module height
     */
    public FreeViewModule(int width, int height) {
        super(width, height);
    }

    @Override
    public void onLoadConfig(JsonObject config) {
        super.onLoadConfig(config);

        // Get view controls configuration
        JsonObject view = Addon.getConfigObject(config, "view");
        this.zoom = Addon.getConfigValue(view, "zoom", 0);
        this.offsetX = Addon.getConfigValue(view, "offset_x", 0);
        this.offsetY = Addon.getConfigValue(view, "offset_y", 0);
    }

    @Override
    public void onSaveConfig(JsonObject config) {
        super.onSaveConfig(config);

        JsonObject view = Addon.getConfigObject(config, "view");
        view.addProperty("zoom", this.zoom);
        view.addProperty("offset_x", this.offsetX);
        view.addProperty("offset_y", this.offsetY);
    }

    @Override
    public void onMousePressed(int x, int y, int mouseButton) {
        if (canMoveContent(x, y, mouseButton)) {
            super.onMousePressed(x, y, mouseButton);
        } else {
            this.dragging = true;

            this.mouseClickedX = x - this.offsetX;
            this.mouseClickedY = y - this.offsetY;
        }
    }

    @Override
    public void onMouseReleased(int x, int y, int mouseButton) {
        super.onMouseReleased(x, y, mouseButton);

        this.dragging = false;
    }

    @Override
    public void onMouseDragged(int x, int y, int mouseButton) {
        super.onMouseDragged(x, y, mouseButton);

        if (this.dragging) {
            this.offsetX = x - this.mouseClickedX;
            this.offsetY = y - this.mouseClickedY;

            // Check position out of bounds
            checkOutOfBounds();

            // Update renderer
            this.moduleRenderer.requestFrame();
        }
    }

    @Override
    public void onMouseScroll(int x, int y, int scrollAmount) {
        super.onMouseScroll(x, y, scrollAmount);

        this.zoom -= scrollAmount * 10;
        this.zoom = Math.max(this.zoom, 0);

        // Check position out of bounds
        checkOutOfBounds();

        // Update renderer
        this.moduleRenderer.requestFrame();
    }


    /**
     * Method to determine if the user is allowed to drag around the content
     *
     * @param x           Current mouse x position
     * @param y           Current mouse y position
     * @param mouseButton The clicked mouse button
     * @return Content movable
     */
    protected boolean canMoveContent(int x, int y, int mouseButton) {
        return mouseButton == 1;
    }

    /**
     * Check if the offset position is out of bounds and move it back inside of the render area
     */
    private void checkOutOfBounds() {
        this.offsetX = Math.max(this.offsetX, -this.zoom);
        this.offsetY = Math.max(this.offsetY, -this.zoom);

        this.offsetX = Math.min(this.offsetX, 0);
        this.offsetY = Math.min(this.offsetY, 0);
    }
}
