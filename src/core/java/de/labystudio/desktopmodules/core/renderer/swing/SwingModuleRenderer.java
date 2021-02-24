package de.labystudio.desktopmodules.core.renderer.swing;

import de.labystudio.desktopmodules.core.module.render.IModuleRenderer;
import de.labystudio.desktopmodules.core.module.render.IRenderCallback;
import de.labystudio.desktopmodules.core.renderer.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.IScreenBounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Swing implementation of the module renderer
 * Each module renderer creates a JDialog/window frame
 *
 * @author LabyStudio
 */
public class SwingModuleRenderer extends JDialog implements IModuleRenderer,
        MouseListener, MouseMotionListener, MouseWheelListener {

    private static final long TO_FONT_PERIOD_MS = 1000 * 3;

    protected final int width;
    protected final int height;

    protected final IRenderContext renderContext = new SwingRenderContext();
    private final IRenderCallback renderCallback;

    private long lastToFontCall = -1L;

    private boolean mouseOver;

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

        // Listener
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
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

        // Make visible
        if (!isVisible()) {
            setVisible(true);
        }

        // The window will no longer be on top of you click on the taskbar
        if (this.lastToFontCall + TO_FONT_PERIOD_MS < System.currentTimeMillis()) {
            this.lastToFontCall = System.currentTimeMillis();

            // Keep it over the taskbar
            toFront();
        }
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
    public IScreenBounds getScreenBoundsOfTargetMonitor() {
        return new SwingScreenBounds(getX() + getWidth() / 2, getY());
    }

    @Override
    public boolean isMouseOver() {
        return this.mouseOver;
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
    public void mouseEntered(MouseEvent e) {
        this.mouseOver = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.mouseOver = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        this.renderCallback.onMouseScroll(event.getX(), event.getY(), event.getWheelRotation());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Unused
    }
}
