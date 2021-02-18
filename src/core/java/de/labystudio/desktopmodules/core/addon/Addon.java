package de.labystudio.desktopmodules.core.addon;

import de.labystudio.desktopmodules.core.DesktopModules;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Objects;

/**
 * @author LabyStudio
 */
public abstract class Addon {

    protected DesktopModules desktopModules;
    protected AddonData addonData;

    /**
     * Called on initialize
     * Override this method instead of creating a constructor
     *
     * @param desktopModules DesktopModules core instance
     * @param addonData      Data of the addon
     * @throws Exception
     */
    public void onInitialize(DesktopModules desktopModules, AddonData addonData) throws Exception {
        this.desktopModules = desktopModules;
        this.addonData = addonData;
    }

    /**
     * Called on enabling the addon
     */
    public abstract void onEnable() throws Exception;

    /**
     * Called on disabling the addon
     */
    public abstract void onDisable() throws Exception;

    /**
     * Register a module for this addon
     *
     * @param moduleClass The class of the module to register
     * @return The loaded module
     * @throws Exception
     */
    public Module registerModule(Class<? extends Module> moduleClass) throws Exception {
        return this.desktopModules.getSourceLoader().loadModule(this, moduleClass);
    }

    /**
     * Load buffered image from resources path
     *
     * @param path Resources path
     * @return Buffered image
     */
    public BufferedImage loadTexture(String path) {
        try {
            URLClassLoader classLoader = this.desktopModules.getClassLoader();
            return ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(path)));
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public AddonData getAddonData() {
        return addonData;
    }

    public DesktopModules getDesktopModules() {
        return desktopModules;
    }
}
