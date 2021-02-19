package de.labystudio.desktopmodules.core.loader;

import com.google.gson.Gson;
import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.model.ModelAddonData;
import de.labystudio.desktopmodules.core.module.Module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Source loader for addon classes and modules
 *
 * @author LabyStudio
 */
public class SourceLoader {

    private static final Gson GSON = new Gson();

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final DesktopModules desktopModules;

    private final List<Addon> addons = new CopyOnWriteArrayList<>();
    private final List<Module> modules = new CopyOnWriteArrayList<>();

    private final File addonsDirectory;

    private final Method addURL;

    /**
     * Create a source loader instance at the given directory
     *
     * @param desktopModules   Main instance for texture loader and class loader
     * @param workingDirectory Home directory of the application to load the addons folder
     */
    public SourceLoader(DesktopModules desktopModules, File workingDirectory) throws NoSuchMethodException {
        this.desktopModules = desktopModules;
        this.addonsDirectory = new File(workingDirectory, "addons");

        // Get add URL method to add files to the class loader
        this.addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        this.addURL.setAccessible(true);
    }

    /**
     * Create addons folder if doesn't exists
     */
    public void setupDirectory() {
        if (!this.addonsDirectory.exists()) {
            this.addonsDirectory.mkdir();
        }
    }

    /**
     * Load all jar files in the addons directory asynchronously
     */
    public void loadAddonsInDirectoryAsync() {
        // Load addon jar files
        if (this.addonsDirectory.exists() && this.addonsDirectory.isDirectory()) {

            // Get all files inside of the addons folder
            File[] files = this.addonsDirectory.listFiles();
            if (files != null) {

                // Iterate jar files
                for (File file : files) {
                    if (file.getName().endsWith(".jar")) {
                        loadAddonAsync(file);
                    }
                }
            }
        }
    }

    /**
     * Load an addon file asynchronously
     *
     * @param file The addon jar file to load
     */
    public void loadAddonAsync(File file) {
        this.executorService.execute(() -> {
            try {
                // Read entry point path of the jar file
                String className = resolveAddonFile(file);

                if (className != null) {
                    // Load jar into classpath
                    addFileToClassPath(file);

                    // Register the addon
                    registerAddon(className);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Load addon asynchronously inside of the applications jar file
     *
     * @param pathToJson Path to the internal json file
     */
    public void loadInternalAddonAsync(String pathToJson) {
        this.executorService.execute(() -> {
            try {
                // Load test addon
                InputStream internalAddonInput = this.desktopModules.getClassLoader().getResourceAsStream(pathToJson);
                if (internalAddonInput != null) {
                    // Load json file
                    String addonClassName = GSON.fromJson(new InputStreamReader(internalAddonInput), ModelAddonData.class).main;

                    // Close the file
                    internalAddonInput.close();

                    // Return the class name
                    registerAddon(addonClassName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Get the entry point of the given addon jar file
     *
     * @param file Jar file to resolve
     * @return The entry point class name of the addon
     */
    private String resolveAddonFile(File file) throws IOException {
        // Open the jar file
        JarFile jarFile = new JarFile(file);

        String jsonPath = "addon.json";
        String addonClassName = null;

        // Check if json is available
        if (jarFile.getJarEntry(jsonPath) != null) {
            // Read jar entry
            JarEntry jarEntry = jarFile.getJarEntry(jsonPath);
            InputStream inputStream = jarFile.getInputStream(jarEntry);

            // Get entry point of addon
            addonClassName = GSON.fromJson(new InputStreamReader(inputStream), ModelAddonData.class).main;
        }

        // Close the jar file
        jarFile.close();

        return addonClassName;
    }

    /**
     * Load the given jar file into the classpath of the application
     *
     * @param file Jar file to add
     */
    private void addFileToClassPath(File file) throws MalformedURLException, InvocationTargetException, IllegalAccessException {
        this.addURL.invoke(this.desktopModules.getClassLoader(), file.toURI().toURL());
    }

    /**
     * Load an addon into the application
     *
     * @param addonClassName Addon class name to load
     * @return The loaded addon
     * @throws Exception Exception during class load
     */
    public Addon registerAddon(String addonClassName) throws Exception {
        // Load class of the addon
        Class<? extends Addon> clazz = (Class<? extends Addon>) this.desktopModules.getClassLoader().loadClass(addonClassName);

        // Create instance of the addon
        Addon addon = load(clazz);

        // Initialize the addon
        addon.onInitialize(this.desktopModules);

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

        // TODO Read config value
        boolean enabled = true;

        // Initialize the module
        module.loadTextures(this.desktopModules.getTextureLoader());
        module.onInitialize(addon, enabled);

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

    public List<Addon> getAddons() {
        return addons;
    }
}
