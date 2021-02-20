package de.labystudio.desktopmodules.core.addon;

import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.module.Module;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LabyStudio
 */
public abstract class Addon {

    private final List<Module<? extends Addon>> modules = new CopyOnWriteArrayList<>();
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
     * Called on a visibility change of a module of this addon
     *
     * @param module  The module that changed the visibility
     * @param enabled The new visibility state of the module
     */
    public void onModuleVisibilityChanged(Module<? extends Addon> module, boolean enabled) {
        // No implementation
    }

    /**
     * Get the internal name of the addon
     *
     * @return Class name
     */
    public String getDisplayName() {
        return getClass().getSimpleName();
    }

    /**
     * Get config directory
     *
     * @return The config directory of the addon
     */
    public File getConfigDirectory() {
        File directory = new File(this.desktopModules.getSourceLoader().getAddonsDirectory(), getDisplayName().toLowerCase());

        // Create directory if it doesn't exists
        if (!directory.exists()) {
            directory.mkdir();
        }

        return directory;
    }

    /**
     * Register a module for this addon
     *
     * @param moduleClass The class of the module to register
     * @return The loaded module
     * @throws Exception
     */
    public Module<? extends Addon> registerModule(Class<? extends Module> moduleClass) throws Exception {
        return this.desktopModules.getSourceLoader().loadModule(this, moduleClass);
    }

    /**
     * Check if this addon has at least one active/visible module
     *
     * @return Has at least one active module
     */
    public boolean hasActiveModules() {
        for (Module<? extends Addon> module : this.modules) {
            if (module.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public DesktopModules getDesktopModules() {
        return desktopModules;
    }

    public List<Module<? extends Addon>> getModules() {
        return modules;
    }
}
