package de.labystudio.desktopmodules.core.loader.model;

import java.io.File;

/**
 * Possible addon to load
 *
 * @author LabyStudio
 */
public class LoadableAddon {

    private final File jarFile;
    private final String mainClass;

    /**
     * Create a possible addon to load
     *
     * @param jarFile   Jar file of the addon
     * @param mainClass Addon class path
     */
    public LoadableAddon(File jarFile, String mainClass) {
        this.jarFile = jarFile;
        this.mainClass = mainClass;
    }

    /**
     * Create possible addon to load inside of the application resources
     *
     * @param mainClass Addon class path
     */
    public LoadableAddon(String mainClass) {
        this(null, mainClass);
    }

    /**
     * Create possible addon to load inside of the application resources
     *
     * @param jarFile        Jar file of the addon
     * @param modelAddonData Addon data containing the class path
     */
    public LoadableAddon(File jarFile, ModelAddonData modelAddonData) {
        this(jarFile, modelAddonData.main);
    }

    /**
     * Create possible addon to load inside of the application resources
     *
     * @param modelAddonData Addon data containing the class path
     */
    public LoadableAddon(ModelAddonData modelAddonData) {
        this(modelAddonData.main);
    }

    public File getJarFile() {
        return jarFile;
    }

    public String getMainClass() {
        return mainClass;
    }
}
