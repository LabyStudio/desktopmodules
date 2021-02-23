package de.labystudio.desktopmodules.core.addon;

import com.google.gson.*;
import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.module.Module;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LabyStudio
 */
public abstract class Addon {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final List<Module<? extends Addon>> modules = new CopyOnWriteArrayList<>();

    protected DesktopModules desktopModules;

    /**
     * Config element
     */
    protected JsonObject config;

    /**
     * Called when loading the addon classes into the application.
     * The config is not loaded yet in this event.
     * Don't register any modules here!
     *
     * @param desktopModules DesktopModules core instance
     */
    public void onPreInitialize(DesktopModules desktopModules) throws Exception {
        this.desktopModules = desktopModules;
    }

    /**
     * Called when loading the addon classes into the application.
     * This event has access to the config object of the addon.
     * Register your modules in this event!
     */
    public abstract void onInitialize() throws Exception;

    /**
     * Called when enabling on of your modules
     */
    public abstract void onEnable() throws Exception;

    /**
     * Called when all modules are disabled
     */
    public abstract void onDisable() throws Exception;

    /**
     * Called on a visibility change of a module of this addon.
     * The default implementation will save the config
     *
     * @param module  The module that changed the visibility
     * @param enabled The new visibility state of the module
     */
    public void onModuleVisibilityChanged(Module<? extends Addon> module, boolean enabled) {
        // No implementation
    }

    /**
     * Load config data from json file
     *
     * @throws FileNotFoundException File not found
     */
    public void loadConfig() throws IOException {
        File file = getConfigFile();

        if (file.exists()) {
            FileReader reader = new FileReader(file);
            JsonElement element = JsonParser.parseReader(reader);

            // Load addon config
            this.config = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();

            reader.close();
        } else {
            this.config = new JsonObject();
        }
    }

    /**
     * Save the config element to the json file
     *
     * @throws IOException File write exception
     */
    public void saveConfig() throws IOException {
        // Store necessary values of all modules
        for (Module<? extends Addon> module : this.modules) {
            if (module.isEnabled()) {
                module.onSaveConfig(module.getConfig());
            }
        }

        File file = getConfigFile();

        // Write to file
        FileWriter writer = new FileWriter(file);
        GSON.toJson(this.config, writer);

        // Flush and close
        writer.flush();
        writer.close();
    }


    /**
     * Change the visibility of a module. It will also handle the loading and saving of the module config.
     *
     * @param module  Module to enable or disable
     * @param enabled New visibility state
     */
    public void setModuleVisibility(Module<? extends Addon> module, Boolean enabled) {
        try {
            if (enabled) {
                // Load the config of the entire addon to get the new module config (Loading from file)
                loadConfig();

                // Load config when changing the state to enabled (It's not actually loading the file)
                module.onLoadConfig(getModuleConfig(module));

                // Save config after creating the module renderer (Writing to file)
                saveConfig();
            } else {
                // Save config before destroying the module renderer (Writing to file)
                saveConfig();

                // Disable module
                module.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Get the config json file
     *
     * @return File to json
     */
    public File getConfigFile() {
        return new File(getConfigDirectory(), "config.json");
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

    /**
     * Get the config of the given module
     *
     * @param module Module to get the config from
     * @return Module config object
     */
    public JsonObject getModuleConfig(Module<? extends Addon> module) {
        String moduleId = module.getClass().getSimpleName().toLowerCase();

        // Module list
        JsonObject modulesObject;
        if (this.config.has("modules")) {
            modulesObject = this.config.getAsJsonObject("modules");
        } else {
            // Create modules attribute if it doesn't exists
            this.config.add("modules", modulesObject = new JsonObject());
        }

        // Module entry
        JsonObject moduleObject;
        if (modulesObject.has(moduleId)) {
            moduleObject = modulesObject.getAsJsonObject(moduleId);
        } else {
            // Create modules attribute if it doesn't exists
            modulesObject.add(moduleId, moduleObject = new JsonObject());
        }

        return moduleObject;
    }

    /**
     * Gets the json object from the specified parent with the given key
     *
     * @param parent The parent object that contains the given key. It will create a new json object if it doesn't exists.
     * @param key    The key to get or to create
     * @return Fetched or newly created json object
     */
    public static JsonObject getConfigObject(JsonObject parent, String key) {
        if (!parent.has(key)) {
            parent.add(key, new JsonObject());
        }
        return parent.get(key).getAsJsonObject();
    }

    /**
     * Gets the string from the specified parent with the given key or create the entry if it doesn't exists
     *
     * @param parent       The parent object that contains the given key. It will create a new string entry if it doesn't exists.
     * @param key          The key to get or to create
     * @param defaultValue The default value that will be returned if it doesn't exists
     * @return Fetched or newly created default string
     */
    public static String getConfigValue(JsonObject parent, String key, String defaultValue) {
        if (parent.has(key)) {
            return parent.get(key).getAsString();
        } else {
            parent.addProperty(key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Gets the integer from the specified parent with the given key or create the entry if it doesn't exists
     *
     * @param parent       The parent object that contains the given key. It will create a new integer entry if it doesn't exists.
     * @param key          The key to get or to create
     * @param defaultValue The default value that will be returned if it doesn't exists
     * @return Fetched or newly created default integer
     */
    public static int getConfigValue(JsonObject parent, String key, int defaultValue) {
        if (parent.has(key)) {
            return parent.get(key).getAsInt();
        } else {
            parent.addProperty(key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Gets the boolean from the specified parent with the given key or create the entry if it doesn't exists
     *
     * @param parent       The parent object that contains the given key. It will create a new boolean entry if it doesn't exists.
     * @param key          The key to get or to create
     * @param defaultValue The default value that will be returned if it doesn't exists
     * @return Fetched or newly created default boolean
     */
    public static boolean getConfigValue(JsonObject parent, String key, boolean defaultValue) {
        if (parent.has(key)) {
            return parent.get(key).getAsBoolean();
        } else {
            parent.addProperty(key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Core application instance
     *
     * @return DesktopModules core instance
     */
    public DesktopModules getDesktopModules() {
        return desktopModules;
    }

    /**
     * Get all modules of this addon
     *
     * @return Module list
     */
    public List<Module<? extends Addon>> getModules() {
        return modules;
    }
}
