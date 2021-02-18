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
     * Create screen bounds
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
