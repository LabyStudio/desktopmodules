package de.labystudio.desktopmodules.core.renderer.swing;

import de.labystudio.desktopmodules.core.renderer.IScreenBounds;

import java.awt.*;

/**
 * Swing implementation of the swing bounds
 *
 * @author LabyStudio
 */
public class SwingScreenBounds implements IScreenBounds {

    private int minX, minY, maxX, maxY;

    /**
     * Create screen bounds of all monitors
     */
    public SwingScreenBounds() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // Iterate trough all monitors
        for (GraphicsDevice device : environment.getScreenDevices()) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();

            this.minX = Math.min(this.minX, bounds.x);
            this.minY = Math.min(this.minY, bounds.y);
            this.maxX = Math.max(this.maxX, bounds.x + bounds.width);
            this.maxY = Math.max(this.maxY, bounds.y + bounds.height);
        }
    }

    /**
     * Create screen bounds of the target monitor
     *
     * @param x Target x position over all monitors
     * @param y Target y position over all monitors
     */
    public SwingScreenBounds(int x, int y) {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // Find target monitor
        for (GraphicsDevice device : environment.getScreenDevices()) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();

            // Is position inside of the monitor
            if (!(x > bounds.x && x < bounds.x + bounds.width && y > bounds.y && y < bounds.y + bounds.height)) {
                continue;
            }

            // Store bounds
            this.minX = bounds.x;
            this.minY = bounds.y;
            this.maxX = bounds.x + bounds.width;
            this.maxY = bounds.y + bounds.height;

            break;
        }
    }

    @Override
    public int getMinX() {
        return this.minX;
    }

    @Override
    public int getMinY() {
        return this.minY;
    }

    @Override
    public int getMaxX() {
        return this.maxX;
    }

    @Override
    public int getMaxY() {
        return this.maxY;
    }
}
