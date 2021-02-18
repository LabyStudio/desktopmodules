package de.labystudio.desktopmodules.core.tray;

import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.gui.GuiSettings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.Objects;

/**
 * Tray icon to manage the application
 *
 * @author LabyStudio
 */
public class TrayHandler implements MouseListener {

    private final TrayIcon trayIcon;
    private final DesktopModules desktopModules;

    private GuiSettings guiSettings;

    /**
     * Create new tray icon for the operating system
     *
     * @param desktopModules Application instance
     * @throws Exception Can throw an exception when loading the tray icon
     */
    public TrayHandler(DesktopModules desktopModules) throws Exception {
        this.desktopModules = desktopModules;

        // Load try icon
        InputStream inputStream = desktopModules.getClassLoader().getResourceAsStream("textures/core/tray.png");
        Image trayImage = ImageIO.read(Objects.requireNonNull(inputStream));

        // Register system tray
        this.trayIcon = new TrayIcon(trayImage);
        this.trayIcon.addMouseListener(this);
        SystemTray.getSystemTray().add(this.trayIcon);

        // Create menu
        this.trayIcon.setPopupMenu(fillMenu(new PopupMenu()));
    }

    /**
     * Fill the tray menu with entries
     *
     * @param menu Popup menu to fill
     * @return The instance that will be added to the tray
     */
    private PopupMenu fillMenu(PopupMenu menu) {
        // Settings menu entry
        MenuItem itemSettings = new MenuItem("Settings");
        itemSettings.addActionListener(e -> openSettings());
        menu.add(itemSettings);

        // Exit menu entry
        MenuItem itemExit = new MenuItem("Exit");
        itemExit.addActionListener(e -> this.desktopModules.shutdown());
        menu.add(itemExit);

        return menu;
    }

    /**
     * Open settings gui
     */
    private void openSettings() {
        // Create new window or focus active one
        if (this.guiSettings == null || !this.guiSettings.isVisible()) {
            (this.guiSettings = new GuiSettings(this.desktopModules)).moveToMouse().init();
        } else {
            this.guiSettings.toFront();
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getButton() == 1) {
            openSettings();
        }
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
    public void mousePressed(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Unused
    }

}
