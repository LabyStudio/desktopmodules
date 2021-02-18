package de.labystudio.desktopmodules.core.renderer.wrapper;

import de.labystudio.desktopmodules.core.renderer.wrapper.font.Font;
import de.labystudio.desktopmodules.core.renderer.wrapper.font.StringAlignment;
import de.labystudio.desktopmodules.core.renderer.wrapper.font.StringEffect;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * All render calls
 *
 * @author LabyStudio
 */
public interface IRenderContext {

    /**
     * Draw outline of a rectangle
     *
     * @param left   Left edge position relative to the module position
     * @param top    Top edge position relative to the module position
     * @param right  Right edge position relative to the module position
     * @param bottom Bottom edge position relative to the module position
     * @param color  Color of the outline
     */
    void drawRect(int left, int top, int right, int bottom, Color color);

    /**
     * @param left   Left edge position relative to the module position
     * @param top    Top edge position relative to the module position
     * @param right  Right edge position relative to the module position
     * @param bottom Bottom edge position relative to the module position
     * @param color  Color of the rectangle
     */
    void fillRect(int left, int top, int right, int bottom, Color color);

    /**
     * Draw a string at given position with given settings
     *
     * @param text      Text to render
     * @param x         X position relative to the module position
     * @param y         Y position relative to the module position
     * @param alignment Text alignment option
     * @param effect    Text effect option
     * @param color     Text color option
     */
    void drawString(String text, float x, float y, StringAlignment alignment, StringEffect effect, Color color);

    /**
     * Set font of the current context
     *
     * @param font Font type
     */
    void setFont(Font font);

    /**
     * Render an image at the given position
     *
     * @param image BufferedImage to render
     * @param x     X position relative to the module position
     * @param y     Y position relative to the module position
     */
    void drawImage(BufferedImage image, int x, int y);
}
