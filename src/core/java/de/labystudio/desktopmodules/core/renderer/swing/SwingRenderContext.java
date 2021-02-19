package de.labystudio.desktopmodules.core.renderer.swing;

import de.labystudio.desktopmodules.core.renderer.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.font.Font;
import de.labystudio.desktopmodules.core.renderer.font.StringAlignment;
import de.labystudio.desktopmodules.core.renderer.font.StringEffect;

import java.awt.*;
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
    }

    @Override
    public void drawRect(int left, int top, int right, int bottom, Color color) {
        this.graphics.setColor(color);
        this.graphics.drawRect(left, top, right - left, bottom - top);
    }

    @Override
    public void fillRect(int left, int top, int right, int bottom, Color color) {
        this.graphics.setColor(color);
        this.graphics.fillRect(left, top, right - left, bottom - top);
    }

    @Override
    public void drawString(String text, float x, float y, StringAlignment alignment, StringEffect effect, Color color) {
        FontMetrics fontMetrics = this.graphics.getFontMetrics();

        int textWidth = fontMetrics.stringWidth(text);
        int xOffset = alignment == StringAlignment.CENTERED ? textWidth / 2 : alignment == StringAlignment.RIGHT ? textWidth : 0;

        // Draw effects
        if (effect == StringEffect.SHADOW) {
            float shadowOffset = fontMetrics.getHeight() / 30F + 1.0F;

            this.graphics.setColor(Color.BLACK);
            this.graphics.drawString(text, x + shadowOffset - xOffset, y + shadowOffset);
        }

        // Draw text
        this.graphics.setColor(color);
        this.graphics.drawString(text, x - xOffset, y);
    }

    @Override
    public void setFont(Font font) {
        this.graphics.setFont(new java.awt.Font(font.getFontFamily(), font.getStyle().ordinal(), font.getSize()));
    }

    @Override
    public void drawImage(BufferedImage image, int x, int y) {
        this.graphics.drawImage(image, x, y, null);
    }

    @Override
    public void drawImage(BufferedImage image, int x, int y, int width, int height) {
        this.graphics.drawImage(image, x, y, width, height, null, null);
    }
}
