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
    void drawOutline(double left, double top, double right, double bottom, Color color);

    /**
     * Fill a rectangle using LEFT, TOP, RIGHT and BOTTOM coordinates
     *
     * @param left   Left edge position relative to the module position
     * @param top    Top edge position relative to the module position
     * @param right  Right edge position relative to the module position
     * @param bottom Bottom edge position relative to the module position
     * @param color  Color of the rectangle
     */
    void drawRect(double left, double top, double right, double bottom, Color color);

    /**
     * Fill a rectangle using X, Y, WIDTH and HEIGHT
     *
     * @param x      Left edge position relative to the module position
     * @param y      Top edge position relative to the module position
     * @param width  Width of the rectangle
     * @param height Height of the rectangle
     * @param color
     */
    void drawRectWH(double x, double y, double width, double height, Color color);

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
    void drawGradientRect(double left, double top, double right, double bottom, Color from, double fromX, double fromY, Color to, double toX, double toY);

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
    void drawString(String text, double x, double y, StringAlignment alignment, StringEffect effect, Color color, Font font);

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
    void drawImage(BufferedImage image, double x, double y);


    /**
     * Render an image at the given position
     *
     * @param image  BufferedImage to render
     * @param x      X position relative to the module position
     * @param y      Y position relative to the module position
     * @param width  Image width
     * @param height Image height
     */
    void drawImage(BufferedImage image, double x, double y, double width, double height);

    /**
     * Translate render context by given offset
     *
     * @param x Offset x
     * @param y Offset y
     */
    void translate(double x, double y);

    /**
     * Scale the render context by given values
     *
     * @param x X factor scale
     * @param y Y factor scale
     */
    void scale(double x, double y);


    /**
     * Rotate the render context by given degrees
     *
     * @param degrees Amount of rotation in degrees
     */
    void rotate(double degrees);

    /**
     * Change the alpha composite of the renderer
     *
     * @param alpha value from 0.0 to 1.0
     */
    void setAlpha(float alpha);
}
