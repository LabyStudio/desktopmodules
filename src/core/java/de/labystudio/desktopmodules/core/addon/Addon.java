package de.labystudio.desktopmodules.core.addon;

import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.module.Module;

/**
 * @author LabyStudio
 */
public abstract class Addon {

    protected DesktopModules desktopModules;

    /**
     * Called on initialize
     * Override this method instead of creating a constructor
     *
     * @param desktopModules DesktopModules core instance
     */
    public void onInitialize(DesktopModules desktopModules) {
        this.desktopModules = desktopModules;
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
     * Get the internal name of the addon
     *
     * @return Class name
     */
    public String getDisplayName() {
        return getClass().getSimpleName();
    }

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

    public DesktopModules getDesktopModules() {
        return desktopModules;
    }
}
