package de.labystudio.desktopmodules.core.gui.widget;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.Module;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Module list entry item
 *
 * @author LabyStudio
 */
public class ModuleWidget extends JPanel {

    public static final int WIDGET_HEIGHT = 60;
    private static final Font FONT = new Font("Dubai Medium", Font.PLAIN, 18);

    private final Module<? extends Addon> module;


    /**
     * Create a entry item for the given module
     *
     * @param module The module to wrap
     */
    public ModuleWidget(Module<? extends Addon> module) {
        this.module = module;

        Addon addon = module.getAddon();

        // Create switch widget
        SwitchWidget switchWidget = new SwitchWidget(module.isEnabled());
        switchWidget.setActionListener(enabled -> {
            // Update module visibility
            addon.setModuleVisibility(this.module, enabled);
        });

        // Create advanced gear widget
        TextureLoader textureLoader = module.getAddon().getDesktopModules().getTextureLoader();
        AdvancedWidget advancedWidget = new AdvancedWidget(textureLoader);
        advancedWidget.setActionListener(unused -> {
            File file = addon.getConfigFile();

            // Open config file in editor
            try {
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            // Turn off modules to avoid conflicts while editing the config
            for (Module<? extends Addon> moduleEntry : addon.getModules()) {
                if (moduleEntry.isEnabled()) {
                    addon.setModuleVisibility(moduleEntry, false);
                }
            }
        });

        // Widget height
        setBorder(new EmptyBorder(10, 0, 10, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, WIDGET_HEIGHT));

        // Widget layout
        setLayout(new GridLayout(0, 1, 0, 0));

        // Add widgets to the right side
        JPanel settingsPanel = new JPanel();
        settingsPanel.setOpaque(false);
        settingsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        settingsPanel.add(advancedWidget);
        settingsPanel.add(switchWidget);
        add(settingsPanel);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int iconPadding = 5;

        // Smooth rendering
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw module icon
        g.drawImage(this.module.getIcon(), iconPadding, iconPadding, getHeight() - iconPadding * 2, getHeight() - iconPadding * 2, null);

        // Draw module name
        g.setFont(FONT);
        g.setColor(Color.DARK_GRAY);
        g.drawString(this.module.getDisplayName(), getHeight() + iconPadding, getHeight() / 2 + 7);
    }
}
