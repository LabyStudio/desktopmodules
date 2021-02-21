package de.labystudio.desktopmodules.core.gui.widget;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Swing toggle button widget
 *
 * @author LabyStudio
 */
public class SwitchWidget extends JPanel implements Runnable, MouseListener {

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(30);

    private static final Color COLOR_OFF = new Color(64, 64, 64);
    private static final Color COLOR_ON = new Color(58, 165, 66);
    private static final Color COLOR_CIRCLE_SHADOW = new Color(64, 64, 64, 140);

    private ScheduledFuture<?> currentAnimationTask;

    private boolean enabled;
    private int animationTick = 0;

    private Consumer<Boolean> consumer;

    public SwitchWidget(boolean value) {
        // Widget size
        setBorder(new EmptyBorder(8, 25, 8, 25));

        // Add mouse listener
        addMouseListener(this);
        setEnabled(value);

        // Skip animation
        this.animationTick = 100;
    }

    /**
     * Set the callback for the button
     *
     * @param consumer The action listener
     */
    public void setActionListener(Consumer<Boolean> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Smooth rendering
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Animation
        float percentage = this.enabled ? this.animationTick : 100 - this.animationTick;
        double offset = (getWidth() - 25) / 100F * percentage;

        // Switch background
        g.setColor(colorTransition(COLOR_ON, COLOR_OFF, percentage / 100F));
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Switch circle
        for (int i = 1; i >= 0; i--) {
            g.setColor(i == 0 ? Color.WHITE : COLOR_CIRCLE_SHADOW);
            g.fillRoundRect((int) (3 + offset), 3 + i * 2, getHeight() - 6, getHeight() - 6, 45, 45);
        }
    }

    /**
     * Create color transition between two colors
     *
     * @param far   First color
     * @param close Second color
     * @param ratio Percentage
     * @return Merged color
     */
    private Color colorTransition(Color far, Color close, float ratio) {
        int red = (int) Math.abs((ratio * far.getRed()) + ((1 - ratio) * close.getRed()));
        int green = (int) Math.abs((ratio * far.getGreen()) + ((1 - ratio) * close.getGreen()));
        int blue = (int) Math.abs((ratio * far.getBlue()) + ((1 - ratio) * close.getBlue()));
        return new Color(red, green, blue);
    }

    @Override
    public void run() {
        this.animationTick++;
        repaint();

        if (this.animationTick >= 100) {
            cancel();
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.animationTick = 0;

        // Call consumer
        if (this.consumer != null) {
            this.consumer.accept(enabled);
        }

        // Cancel previous animation
        cancel();

        // Schedule new animation task
        this.currentAnimationTask = executorService.scheduleAtFixedRate(this, 0, 1, TimeUnit.MILLISECONDS);
    }

    private void cancel() {
        if (this.currentAnimationTask != null && !this.currentAnimationTask.isCancelled() && !this.currentAnimationTask.isDone()) {
            this.currentAnimationTask.cancel(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setEnabled(!this.enabled);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
}
