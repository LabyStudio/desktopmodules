package de.labystudio.desktopmodules.core.renderer.swing;

import de.labystudio.desktopmodules.core.module.render.IRenderCallback;
import de.labystudio.desktopmodules.core.renderer.IRenderContext;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Canvas implementation to render the actual module inside the window frame.
 *
 * @author LabyStudio
 */
public class SwingCanvasRender extends JPanel {

    private final SwingModuleRenderer moduleRenderer;
    protected final IRenderContext renderContext = new SwingRenderContext();
    private final IRenderCallback renderCallback;

    public SwingCanvasRender(SwingModuleRenderer moduleRenderer) {
        this.moduleRenderer = moduleRenderer;
        this.renderCallback = moduleRenderer.getRenderCallback();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int width = this.moduleRenderer.getWidth();
        int height = this.moduleRenderer.getHeight();

        int mouseX = this.moduleRenderer.getMouseX();
        int mouseY = this.moduleRenderer.getMouseY();

        // Update graphics instance
        ((SwingRenderContext) this.renderContext).updateGraphics((Graphics2D) g);

        // Call render callback
        this.renderCallback.onRender(this.renderContext, width, height, mouseX, mouseY);
        this.renderCallback.onRender(this.renderContext, width, height);
    }
}
