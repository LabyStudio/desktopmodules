package de.labystudio.desktopmodules.core.gui.widget;

import de.labystudio.desktopmodules.core.module.Module;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Module list entry item
 *
 * @author LabyStudio
 */
public class ModuleWidget extends JPanel implements Consumer<Boolean> {

    public static final int WIDGET_HEIGHT = 60;
    private static final Font FONT = new Font("Dubai Medium", Font.PLAIN, 18);

    private final Module module;

    private final SwitchWidget switchWidget;

    /**
     * Create a entry item for the given module
     *
     * @param module The module to wrap
     */
    public ModuleWidget(Module module) {
        this.module = module;
        this.switchWidget = new SwitchWidget(module.isEnabled());
        this.switchWidget.setActionListener(this);

        // Widget height
        setBorder(new EmptyBorder(10, 0, 10, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, WIDGET_HEIGHT));

        // Widget layout
        setLayout(new GridLayout(0, 1, 0, 0));

        // Add widgets to the right side
        JPanel settingsPanel = new JPanel();
        settingsPanel.setOpaque(false);
        settingsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        settingsPanel.add(this.switchWidget);
        add(settingsPanel);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int iconPadding = 5;

        // Smooth rendering
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw module icon
        g.drawImage(this.module.getIcon(), iconPadding, iconPadding, getHeight() - iconPadding * 2, getHeight() - iconPadding * 2, null);

        // Draw module name
        g.setFont(FONT);
        g.setColor(Color.DARK_GRAY);
        g.drawString(this.module.getDisplayName(), getHeight() + iconPadding, getHeight() / 2 + 7);
    }

    @Override
    public void accept(Boolean value) {
        this.module.setEnabled(value);
    }
}
