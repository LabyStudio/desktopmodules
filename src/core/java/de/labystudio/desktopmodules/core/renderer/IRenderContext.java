package de.labystudio.desktopmodules.core.renderer;

import de.labystudio.desktopmodules.core.renderer.font.Font;
import de.labystudio.desktopmodules.core.renderer.font.StringAlignment;
import de.labystudio.desktopmodules.core.renderer.font.StringEffect;

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
    void drawOutline(int left, int top, int right, int bottom, Color color);

    /**
     * Fill a rectangle using LEFT, TOP, RIGHT and BOTTOM coordinates
     *
     * @param left   Left edge position relative to the module position
     * @param top    Top edge position relative to the module position
     * @param right  Right edge position relative to the module position
     * @param bottom Bottom edge position relative to the module position
     * @param color  Color of the rectangle
     */
    void drawRect(int left, int top, int right, int bottom, Color color);

    /**
     * Fill a rectangle using X, Y, WIDTH and HEIGHT
     *
     * @param x      Left edge position relative to the module position
     * @param y      Top edge position relative to the module position
     * @param width  Width of the rectangle
     * @param height Height of the rectangle
     * @param color
     */
    void drawRectWH(int x, int y, int width, int height, Color color);

    /**
     * Draw gradient rectangle
     *
     * @param left   Left edge position relative to the module position
     * @param top    Top edge position relative to the module position
     * @param right  Right edge position relative to the module position
     * @param bottom Bottom edge position relative to the module position
     * @param from   Color from
     * @param fromX  X from-color position relative to the module position
     * @param fromY  Y from-color position relative to the module position
     * @param to     Color to
     * @param toX    X to-color position relative to the module position
     * @param toY    Y to-color position relative to the module position
     */
    void drawGradientRect(int left, int top, int right, int bottom, Color from, int fromX, int fromY, Color to, int toX, int toY);

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
    void drawString(String text, float x, float y, StringAlignment alignment, StringEffect effect, Color color, Font font);

    /**
     * Get the string width of the given text with the given font style
     *
     * @param text The string
     * @param font Font to apply to the calculation
     * @return The string width in pixel
     */
    int getStringWidth(String text, Font font);

    /**
     * Render an image at the given position
     *
     * @param image BufferedImage to render
     * @param x     X position relative to the module position
     * @param y     Y position relative to the module position
     */
    void drawImage(BufferedImage image, int x, int y);


    /**
     * Render an image at the given position
     *
     * @param image  BufferedImage to render
     * @param x      X position relative to the module position
     * @param y      Y position relative to the module position
     * @param width  Image width
     * @param height Image height
     */
    void drawImage(BufferedImage image, int x, int y, int width, int height);
}
