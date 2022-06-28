package de.labystudio.desktopmodules.core.renderer.swing;

import de.labystudio.desktopmodules.core.module.render.IModuleRenderer;
import de.labystudio.desktopmodules.core.module.render.IRenderCallback;
import de.labystudio.desktopmodules.core.renderer.IScreenBounds;

import javax.swing.JDialog;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

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

    private final SwingCanvasRender canvas;
    private final IRenderCallback renderCallback;

    private long lastToFontCall = -1L;

    private boolean mouseOver;
    private int mouseX;
    private int mouseY;

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

        // Canvas
        this.setContentPane(this.canvas = new SwingCanvasRender(this));

        // Init
        this.setSize(width, height);
        this.setUndecorated(true);
        this.setResizable(false);

        // Overlay
        this.setType(Type.UTILITY);
        this.setBackground(new Color(255, 255, 255, 0));
        this.setAlwaysOnTop(true);
        this.setFocusableWindowState(false);
        this.setAutoRequestFocus(false);

        // Show
        this.toFront();

        // Listener
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    public void requestFrame() {
        this.canvas.repaint();

        // Make visible
        if (!this.isVisible()) {
            this.setVisible(true);
        }

        // The window will no longer be on top of you click on the taskbar
        if (this.lastToFontCall + TO_FONT_PERIOD_MS < System.currentTimeMillis()) {
            this.lastToFontCall = System.currentTimeMillis();

            // Keep it over the taskbar
            this.toFront();
        }
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);

        // Repaint on position change
        if (this.canvas != null) {
            this.canvas.repaint();
        }
    }

    @Override
    public IScreenBounds getScreenBounds() {
        return new SwingScreenBounds();
    }

    @Override
    public IScreenBounds getScreenBoundsOfTargetMonitor() {
        return new SwingScreenBounds(this.getX() + this.getWidth() / 2, this.getY());
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
    public void mouseEntered(MouseEvent event) {
        this.mouseOver = true;
        this.mouseX = event.getX();
        this.mouseY = event.getY();
    }

    @Override
    public void mouseExited(MouseEvent event) {
        this.mouseOver = false;
        this.mouseX = event.getX();
        this.mouseY = event.getY();
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
    public void mouseMoved(MouseEvent event) {
        this.mouseX = event.getX();
        this.mouseY = event.getY();
    }

    public IRenderCallback getRenderCallback() {
        return this.renderCallback;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }
}
