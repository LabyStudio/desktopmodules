package de.labystudio.desktopmodules.core.renderer.swing;

import de.labystudio.desktopmodules.core.renderer.wrapper.IModuleRenderer;
import de.labystudio.desktopmodules.core.renderer.wrapper.IRenderCallback;
import de.labystudio.desktopmodules.core.renderer.wrapper.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.wrapper.IScreenBounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Swing implementation of the module renderer
 * Each module renderer creates a JDialog/window frame
 *
 * @author LabyStudio
 */
public class SwingModuleRenderer extends JDialog implements IModuleRenderer, MouseListener, MouseMotionListener {

    protected final int width;
    protected final int height;

    protected final IRenderContext renderContext = new SwingRenderContext();
    private final IRenderCallback renderCallback;

    /**
     * Create new swing module renderer
     *
     * @param renderCallback Render callback
     * @param width          Module width
     * @param height         Module height
     */
    public SwingModuleRenderer(IRenderCallback renderCallback, int width, int height) {
        this.renderCallback = renderCallback;
        this.width = width;
        this.height = height;

        // Init
        setSize(width, height);
        setUndecorated(true);
        setResizable(false);

        // Overlay
        setType(Type.UTILITY);
        setBackground(new Color(255, 255, 255, 0));
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setAutoRequestFocus(false);

        // Show
        toFront();
        setVisible(true);

        // Listener
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Update graphics instance
        ((SwingRenderContext) this.renderContext).updateGraphics((Graphics2D) g);

        // Call render callback
        this.renderCallback.onRender(this.renderContext, this.width, this.height);
    }

    @Override
    public void requestFrame() {
        repaint();
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);

        // Repaint on position change
        repaint();
    }

    @Override
    public IScreenBounds getScreenBounds() {
        return new SwingScreenBounds();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        this.renderCallback.onMousePressed(event.getX(), event.getY(), event.getButton());
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        this.renderCallback.onMouseDragged(event.getX(), event.getY(), event.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        this.renderCallback.onMouseReleased(event.getX(), event.getY(), event.getButton());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Unused
    }
}
