package de.labystudio.desktopmodules.core;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.SourceLoader;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.Module;
import de.labystudio.desktopmodules.core.module.render.IModuleRenderer;
import de.labystudio.desktopmodules.core.os.WorkingDirectory;
import de.labystudio.desktopmodules.core.tray.TrayHandler;

import java.io.File;
import java.net.URLClassLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * DesktopModules core main class
 *
 * @author LabyStudio
 */
public class DesktopModules {

    public static final int TICKS_PER_SECOND = 20;

    private final Thread SHUTDOWN_HOOK = new Thread(this::shutdown);

    private final URLClassLoader classLoader;

    private final File workingDirectory = WorkingDirectory.get("DesktopModules");

    private final SourceLoader sourceLoader = new SourceLoader(this, this.workingDirectory);
    private final TextureLoader textureLoader = new TextureLoader(this);

    private final TrayHandler tray;

    private ScheduledFuture<?> tickTask;

    /**
     * Create an instance of the DesktopModules application and load all addons using the given classloader
     *
     * @param classLoader Class loader to use for the addons
     */
    public DesktopModules(URLClassLoader classLoader) throws Exception {
        this.classLoader = classLoader;

        // Add system tray
        this.tray = new TrayHandler(this);
        this.tray.init();

        // Create shutdown hook
        Runtime.getRuntime().addShutdownHook(SHUTDOWN_HOOK);
    }

    /**
     * Initialize the application
     */
    public void init() {
        // Load all addons
        this.sourceLoader.loadAddonsInDirectoryAsync();

        // Start render thread
        this.tickTask = Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::onTick, 0, 1000 / TICKS_PER_SECOND, TimeUnit.MILLISECONDS);
    }

    /**
     * Called each tick
     */
    private void onTick() {
        try {
            // Tick all loaded modules
            for (Module<? extends Addon> module : this.sourceLoader.getModules()) {
                if (module.isEnabled()) {
                    // Tick module
                    module.onTick();

                    // Repaint module
                    IModuleRenderer moduleRenderer = module.getModuleRenderer();
                    if (moduleRenderer != null) {
                        moduleRenderer.requestFrame();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop all tasks, remove the tray icon, save all config files
     */
    private void shutdown() {
        // Stop tick update task
        if (this.tickTask != null) {
            this.tickTask.cancel(true);
        }

        // Disable all addons
        for (Addon addon : this.sourceLoader.getAddons()) {

            // Store necessary values of all modules
            for (Module<? extends Addon> module : addon.getModules()) {
                module.onSaveConfig(module.getConfig());
            }

            try {
                addon.onDisable();
                addon.saveConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save and quit everything entirely
     */
    public void shutdownProperly() {
        // Shutdown hook not required when properly closed
        Runtime.getRuntime().removeShutdownHook(SHUTDOWN_HOOK);

        // Remove system tray (It's not possible to call it from the shutdown hook)
        this.tray.remove();

        // Stop all tasks, remove the tray icon, save all config files
        shutdown();

        // Close all windows
        for (Module<? extends Addon> module : this.sourceLoader.getModules()) {
            IModuleRenderer moduleRenderer = module.getModuleRenderer();
            if (moduleRenderer != null) {
                moduleRenderer.setVisible(false);
            }
        }

        // Force exit
        Runtime.getRuntime().halt(0);
    }

    public SourceLoader getSourceLoader() {
        return sourceLoader;
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    public TextureLoader getTextureLoader() {
        return textureLoader;
    }
}
