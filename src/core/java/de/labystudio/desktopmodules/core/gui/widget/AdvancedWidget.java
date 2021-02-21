package de.labystudio.desktopmodules.core.gui.widget;

import de.labystudio.desktopmodules.core.loader.TextureLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class AdvancedWidget extends JPanel implements MouseListener {

    private final BufferedImage textureGear;
    private Consumer<Boolean> consumer;

    private boolean mouseOver;

    public AdvancedWidget(TextureLoader textureLoader) {
        this.textureGear = textureLoader.loadTexture("textures/core/gear.png");

        // Widget size
        setBorder(new EmptyBorder(8, 8, 8, 8));

        // Add mouse listener
        addMouseListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int padding = this.mouseOver ? 0 : 1;
        g.drawImage(this.textureGear, padding, padding, getWidth() - padding * 2, getHeight() - padding * 2, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this.consumer != null) {
            this.consumer.accept(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.mouseOver = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.mouseOver = false;
        repaint();
    }

    /**
     * Set the callback for the button
     *
     * @param consumer The action listener
     */
    public void setActionListener(Consumer<Boolean> consumer) {
        this.consumer = consumer;
    }
}
