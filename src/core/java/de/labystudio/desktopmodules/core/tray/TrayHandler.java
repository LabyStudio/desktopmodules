package de.labystudio.desktopmodules.core.tray;

import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.gui.GuiSettings;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Tray icon to manage the application
 *
 * @author LabyStudio
 */
public class TrayHandler implements MouseListener {

    private final PopupMenu popupMenu = fillMenu(new PopupMenu());

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

        // Load tray icon
        Image trayImage = desktopModules.getTextureLoader().load("textures/core/tray.png");

        // Register system tray
        this.trayIcon = new TrayIcon(trayImage);
    }

    /**
     * Add the try to the system and fill the menu
     *
     * @throws Exception Can throw an exception when adding to the system tray
     */
    public void init() throws Exception {
        // Create menu
        this.trayIcon.setPopupMenu(this.popupMenu);
        this.trayIcon.addMouseListener(this);

        SystemTray.getSystemTray().add(this.trayIcon);
    }

    /**
     * Remove the tray icon from the system tray
     */
    public void remove() {
        SystemTray.getSystemTray().remove(this.trayIcon);
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

        // Open directory menu entry
        MenuItem itemAddonsDirectory = new MenuItem("Open addons directory");
        itemAddonsDirectory.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(this.desktopModules.getSourceLoader().getAddonsDirectory().toURI());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        menu.add(itemAddonsDirectory);

        // Separator
        menu.addSeparator();

        // Exit menu entry
        MenuItem itemExit = new MenuItem("Exit");
        itemExit.addActionListener(e -> this.desktopModules.shutdownProperly());
        menu.add(itemExit);

        return menu;
    }

    /**
     * Open settings gui
     */
    private void openSettings() {
        // Create new window or focus active one
        if (this.guiSettings == null || !this.guiSettings.isVisible()) {
            (this.guiSettings = new GuiSettings(this.desktopModules)).moveToTray().init();
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
