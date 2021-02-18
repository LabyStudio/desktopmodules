package de.labystudio.desktopmodules.core.gui;

import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.gui.widget.ModuleWidget;
import de.labystudio.desktopmodules.core.module.Module;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.InputStream;
import java.util.Objects;
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

    private final ScheduledFuture<?> fadeInTask;
    private int fadeInTick = 0;


    /**
     * Create a new window
     *
     * @param desktopModules DesktopModules application instance
     */
    public GuiSettings(DesktopModules desktopModules) {
        this.desktopModules = desktopModules;

        // Set icon
        try {
            InputStream inputStream = desktopModules.getClassLoader().getResourceAsStream("textures/core/icon.png");
            setIconImage(ImageIO.read(Objects.requireNonNull(inputStream)));
        } catch (Exception error) {
            error.printStackTrace();
        }

        // Setup window
        setTitle("DesktopModules Settings");
        setSize(400, this.desktopModules.getSourceLoader().getModules().size() * ModuleWidget.WIDGET_HEIGHT);

        // Remove window border
        setUndecorated(true);
        setResizable(false);

        // Overlay
        setAlwaysOnTop(true);
        setOpacity(0.0F);
        setVisible(true);

        // Dispose on focus loss
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                dispose();
            }
        });

        // Fade in animation
        this.fadeInTask = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);
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

        return this;
    }

    /**
     * Move the window to the mouse cursor
     *
     * @return GuiSettings instance
     */
    public GuiSettings moveToMouse() {
        Point point = MouseInfo.getPointerInfo().getLocation();
        boolean taskbarAtTop = point.y < Toolkit.getDefaultToolkit().getScreenSize().height / 2;

        // Update location
        setLocation(point.x - getWidth(), taskbarAtTop ? point.y : point.y - getHeight());

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
            for (Module module : this.desktopModules.getSourceLoader().getModules()) {

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
