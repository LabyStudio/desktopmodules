package de.labystudio.desktopmodules.core.renderer.swing;

import de.labystudio.desktopmodules.core.renderer.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.font.Font;
import de.labystudio.desktopmodules.core.renderer.font.StringAlignment;
import de.labystudio.desktopmodules.core.renderer.font.StringEffect;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


/**
 * Swing implementation of the render context
 *
 * @author LabyStudio
 */
public class SwingRenderContext implements IRenderContext {

    private Graphics2D graphics;

    /**
     * Update the graphics instance
     *
     * @param graphics New graphics instance
     */
    public void updateGraphics(Graphics2D graphics) {
        this.graphics = graphics;

        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    @Override
    public void drawOutline(int left, int top, int right, int bottom, Color color) {
        this.graphics.setColor(color);
        this.graphics.drawRect(left, top, right - left, bottom - top);
    }

    @Override
    public void drawRect(int left, int top, int right, int bottom, Color color) {
        this.graphics.setColor(color);
        this.graphics.fillRect(left, top, right - left, bottom - top);
    }

    @Override
    public void drawRectWH(int x, int y, int width, int height, Color color) {
        this.graphics.setColor(color);
        this.graphics.fillRect(x, y, width, height);
    }

    @Override
    public void drawGradientRect(int left, int top, int right, int bottom, Color from, int fromX, int fromY, Color to, int toX, int toY) {
        this.graphics.setPaint(new GradientPaint(
                new Point2D.Double(fromX, fromY), from,
                new Point2D.Double(toX, toY), to));
        this.graphics.fill(new Rectangle2D.Double(left, top, right - left, bottom - top));
    }

    @Override
    public void drawString(String text, float x, float y, StringAlignment alignment, StringEffect effect, Color color, Font font) {
        setFont(font);

        FontMetrics fontMetrics = this.graphics.getFontMetrics();

        int textWidth = fontMetrics.stringWidth(text);
        int xOffset = alignment == StringAlignment.CENTERED ? textWidth / 2 : alignment == StringAlignment.RIGHT ? textWidth : 0;

        // Draw effects
        if (effect == StringEffect.SHADOW) {
            this.graphics.setColor(Color.BLACK);
            this.graphics.drawString(text, x - xOffset + 1, y + 1);
        }

        // Draw text
        this.graphics.setColor(color);
        this.graphics.drawString(text, x - xOffset, y);
    }

    @Override
    public int getStringWidth(String text, Font font) {
        setFont(font);
        return this.graphics.getFontMetrics().stringWidth(text);
    }

    @Override
    public void drawImage(BufferedImage image, int x, int y) {
        this.graphics.drawImage(image, x, y, null);
    }

    @Override
    public void drawImage(BufferedImage image, int x, int y, int width, int height) {
        this.graphics.drawImage(image, x, y, width, height, null, null);
    }

    @Override
    public void translate(double x, double y) {
        this.graphics.translate(x, y);
    }

    @Override
    public void scale(double x, double y) {
        this.graphics.scale(x, y);
    }

    @Override
    public void rotate(double degrees) {
        this.graphics.rotate(degrees);
    }

    @Override
    public void setAlpha(float alpha) {
        this.graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1.0F, alpha)));
    }

    /**
     * Set font of the current context
     */
    private void setFont(Font font) {
        this.graphics.setFont(new java.awt.Font(font.getFontFamily(), font.getStyle().ordinal(), font.getSize()));
    }
}
