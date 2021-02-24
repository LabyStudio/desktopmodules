package de.labystudio.desktopmodules.core.renderer.swing;

import de.labystudio.desktopmodules.core.renderer.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.font.Font;
import de.labystudio.desktopmodules.core.renderer.font.StringAlignment;
import de.labystudio.desktopmodules.core.renderer.font.StringEffect;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    @Override
    public void drawOutline(double left, double top, double right, double bottom, Color color) {
        this.graphics.setColor(color);
        this.graphics.draw(new Rectangle2D.Double(left, top, right, bottom));
    }

    @Override
    public void drawRect(double left, double top, double right, double bottom, Color color) {
        drawRectWH(left, top, right - left, bottom - top, color);
    }

    @Override
    public void drawRectWH(double x, double y, double width, double height, Color color) {
        this.graphics.setColor(color);
        this.graphics.fill(new Rectangle2D.Double(x, y, width, height));
    }

    @Override
    public void drawGradientRect(double left, double top, double right, double bottom, Color from, double fromX, double fromY, Color to, double toX, double toY) {
        this.graphics.setPaint(new GradientPaint(
                new Point2D.Double(fromX, fromY), from,
                new Point2D.Double(toX, toY), to));
        this.graphics.fill(new Rectangle2D.Double(left, top, right - left, bottom - top));
    }

    @Override
    public void drawString(String text, double x, double y, StringAlignment alignment, StringEffect effect, Color color, Font font) {
        setFont(font);

        FontMetrics fontMetrics = this.graphics.getFontMetrics();

        int textWidth = fontMetrics.stringWidth(text);
        int xOffset = alignment == StringAlignment.CENTERED ? textWidth / 2 : alignment == StringAlignment.RIGHT ? textWidth : 0;

        // Draw effects
        if (effect == StringEffect.SHADOW) {
            this.graphics.setColor(Color.BLACK);
            this.graphics.drawString(text, (float) x - xOffset + 1, (float) y + 1);
        }

        // Draw text
        this.graphics.setColor(color);
        this.graphics.drawString(text, (float) x - xOffset, (float) y);
    }

    @Override
    public void drawString(String text, double width, double offsetX, double y, boolean rightBound, StringEffect effect, Color color, Font font) {
        drawString(text, rightBound ? width - offsetX : offsetX, y, StringAlignment.from(rightBound), effect, color, font);
    }

    @Override
    public int getStringWidth(String text, Font font) {
        setFont(font);
        return this.graphics.getFontMetrics().stringWidth(text);
    }

    @Override
    public void drawImage(BufferedImage image, double x, double y) {
        drawImage(image, x, y, image.getWidth(), image.getHeight());
    }

    @Override
    public void drawImage(BufferedImage image, double x, double y, double width, double height) {
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.scale(width / image.getWidth(), height / image.getHeight());
        this.graphics.drawImage(image, transform, null);
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
