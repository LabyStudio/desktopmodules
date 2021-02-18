package de.labystudio.desktopmodules.core.loader;

import de.labystudio.desktopmodules.core.addon.AddonData;

import java.io.File;

/**
 * Possible addon to load
 *
 * @author LabyStudio
 */
public class LoadableAddon {

    private final File jarFile;
    private final AddonData addonData;

    /**
     * Create a possible addon to load
     *
     * @param jarFile   Jar file of the addon. Can be null to use the current application as resource
     * @param addonData Addon data from the json file
     */
    public LoadableAddon(File jarFile, AddonData addonData) {
        this.jarFile = jarFile;
        this.addonData = addonData;
    }

    public File getJarFile() {
        return jarFile;
    }

    public AddonData getAddonData() {
        return addonData;
    }
}
