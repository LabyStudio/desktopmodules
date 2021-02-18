package de.labystudio.desktopmodules.core.loader;

import com.google.gson.Gson;
import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.addon.AddonData;
import de.labystudio.desktopmodules.core.addon.Module;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LabyStudio
 */
public class SourceLoader {

    private static final Gson GSON = new Gson();

    private final DesktopModules desktopModules;

    private final List<Addon> addons = new CopyOnWriteArrayList<>();
    private final List<Module> modules = new CopyOnWriteArrayList<>();

    public SourceLoader(DesktopModules desktopModules) {
        this.desktopModules = desktopModules;
    }

    /**
     * Get a list of all available addons to load
     *
     * @return List of loadable addons
     */
    public List<LoadableAddon> find() {
        List<LoadableAddon> loadableAddons = new ArrayList<>();

        // TODO load jar files

        try {
            // Load test addon
            InputStream testAddonJsonInput = this.desktopModules.getClassLoader().getResourceAsStream("addon.json");
            if (testAddonJsonInput != null) {
                LoadableAddon loadableAddon = new LoadableAddon(null, GSON.fromJson(new InputStreamReader(testAddonJsonInput), AddonData.class));
                loadableAddons.add(loadableAddon);
                testAddonJsonInput.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadableAddons;
    }

    /**
     * Load an addon into the application
     *
     * @param loadableAddon Addon to load
     * @return The loaded addon
     * @throws Exception
     */
    public Addon loadAddon(LoadableAddon loadableAddon) throws Exception {
        // Load class of the addon
        String mainClassName = loadableAddon.getAddonData().main;
        Class<? extends Addon> clazz = (Class<? extends Addon>) this.desktopModules.getClassLoader().loadClass(mainClassName);

        // Create instance of the addon
        Addon addon = load(clazz);

        // Initialize the addon
        addon.onInitialize(this.desktopModules, loadableAddon.getAddonData());

        // Enable the addon
        addon.onEnable();

        // Register addon
        this.addons.add(addon);

        return addon;
    }

    /**
     * Load a module into the application
     *
     * @param addon       Addon of the module
     * @param moduleClass Module class to load
     * @return The loaded module instance
     * @throws Exception
     */
    public Module loadModule(Addon addon, Class<? extends Module> moduleClass) throws Exception {
        // Create instance of the module
        Module module = load(moduleClass);

        // Initialize the module
        module.loadTextures(addon);
        module.onInitialize(addon);

        // Register the module
        this.modules.add(module);

        return module;
    }

    /**
     * Create a instance of a class
     *
     * @param clazz Class to create an instance of
     * @return Created instance
     * @throws Exception
     */
    private <T> T load(Class<? extends T> clazz) throws Exception {
        Constructor<? extends T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    /**
     * Get a list of all loaded modules
     *
     * @return List of all loaded modules
     */
    public List<Module> getModules() {
        return modules;
    }
}
