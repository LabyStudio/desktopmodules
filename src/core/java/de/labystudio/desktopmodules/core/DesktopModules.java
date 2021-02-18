package de.labystudio.desktopmodules.core;

import de.labystudio.desktopmodules.core.addon.Module;
import de.labystudio.desktopmodules.core.loader.LoadableAddon;
import de.labystudio.desktopmodules.core.loader.SourceLoader;

import java.net.URLClassLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DesktopModules core main class
 *
 * @author LabyStudio
 */
public class DesktopModules {

    private static final int TICKS_PER_SECOND = 20;

    private final URLClassLoader classLoader;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final SourceLoader sourceLoader = new SourceLoader(this);

    /**
     * Create an instance of the DesktopModules application and load all addons using the given classloader
     *
     * @param classLoader Class loader to use for the addons
     */
    public DesktopModules(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Initialize the application
     */
    public void init() {
        loadAddons();

        // Start render thread
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::onTick, 0, 1000 / TICKS_PER_SECOND, TimeUnit.MILLISECONDS);
    }

    /**
     * Called each tick
     */
    private void onTick() {
        try {
            // Tick all loaded modules
            for (Module module : this.sourceLoader.getModules()) {

                // Tick module
                module.onTick();

                // Repaint module
                module.getModuleRenderer().requestFrame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all addons
     */
    public void loadAddons() {
        // Load all addons
        for (LoadableAddon loadableAddon : this.sourceLoader.find()) {

            // Async addon loading
            this.executorService.execute(() -> {
                try {
                    this.sourceLoader.loadAddon(loadableAddon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public SourceLoader getSourceLoader() {
        return sourceLoader;
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }
}
