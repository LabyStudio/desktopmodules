package de.labystudio.desktopmodules.core.gui;

import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.gui.widget.ModuleWidget;
import de.labystudio.desktopmodules.core.module.Module;
import de.labystudio.desktopmodules.core.renderer.IScreenBounds;
import de.labystudio.desktopmodules.core.renderer.swing.SwingScreenBounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * DesktopModules addon configuration user interface
 *
 * @author LabyStudio
 */
public class GuiSettings extends JDialog implements Runnable {

    private final DesktopModules desktopModules;

    private ScheduledFuture<?> fadeInTask;
    private int fadeInTick = 0;

    /**
     * Create a new window
     *
     * @param desktopModules DesktopModules application instance
     */
    public GuiSettings(DesktopModules desktopModules) {
        this.desktopModules = desktopModules;

        // Setup window
        setTitle("DesktopModules Settings");
        setSize(400, this.desktopModules.getSourceLoader().getModules().size() * ModuleWidget.WIDGET_HEIGHT);

        // Remove window border
        setUndecorated(true);
        setResizable(false);

        // Overlay
        setAlwaysOnTop(true);
        setOpacity(0.0F);

        // Dispose on focus loss
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void run() {
        if (!isVisible() || this.fadeInTick >= 100) {
            this.fadeInTask.cancel(true);
        }

        // Update opacity of window
        setOpacity(1.0F / 100F * this.fadeInTick);

        // Next tick
        this.fadeInTick += 10;
    }

    /**
     * Setup the content and make the window visible
     *
     * @return GuiSettings instance
     */
    public GuiSettings init() {
        JScrollPane list = createModuleListElement();
        getContentPane().add(list, BorderLayout.CENTER);

        setVisible(true);
        toFront();

        // Fade in animation
        this.fadeInTask = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);

        return this;
    }

    /**
     * Calculate the position of the tray icon and move the window to it
     *
     * @return GuiSettings instance
     */
    public GuiSettings moveToTray() {
        Point point = MouseInfo.getPointerInfo().getLocation();
        IScreenBounds screen = new SwingScreenBounds(point.x, point.y);

        int mouseX = point.x - screen.getMinX();
        int mouseY = point.y - screen.getMinY();

        // Get target screen width and height
        int screenWidth = Math.abs(screen.getMaxX() - screen.getMinX());
        int screenHeight = Math.abs(screen.getMaxY() - screen.getMinY());

        int taskBarHeight = 40;
        int taskBarWidth = 62;

        // Find the area of the taskbar
        boolean taskbarAtTop = mouseY < screenHeight / 2;
        boolean taskbarAtLeft = mouseX < screenWidth / 2;
        boolean taskbarAtRight = !taskbarAtTop && !taskbarAtLeft && mouseY < screenHeight - taskBarHeight;

        // Predict taskbar location
        if (taskbarAtRight || taskbarAtLeft) {
            int x = taskbarAtLeft ? screen.getMinX() + taskBarWidth : screen.getMaxX() - getWidth() - taskBarWidth;
            int y = mouseY - getHeight();

            // Update location
            setLocation(x, screen.getMinY() + y);
        } else {
            int x = mouseX - getWidth();
            int y = taskbarAtTop ? screen.getMinY() + taskBarHeight : screen.getMaxY() - getHeight() - taskBarHeight;

            // Update location
            setLocation(screen.getMinX() + x, y);
        }

        return this;
    }

    /**
     * Create module list element
     *
     * @return Scrollable list with all module entries
     */
    private JScrollPane createModuleListElement() {
        JPanel panel = new JPanel();

        // Set list layout
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(new Color(0, 0, 0, 0));

        // Add all modules
        for (Addon addon : this.desktopModules.getSourceLoader().getAddons()) {
            for (Module<? extends Addon> module : this.desktopModules.getSourceLoader().getModules()) {

                // Group modules by addon
                if (module.getAddon() == addon) {
                    panel.add(new ModuleWidget(module));
                }
            }
        }

        // Make scrollable
        return new JScrollPane(panel);
    }
}
