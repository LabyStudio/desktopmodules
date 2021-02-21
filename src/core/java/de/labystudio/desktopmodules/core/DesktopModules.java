package de.labystudio.desktopmodules.core;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.SourceLoader;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.Module;
import de.labystudio.desktopmodules.core.module.wrapper.IModuleRenderer;
import de.labystudio.desktopmodules.core.tray.TrayHandler;

import java.io.File;
import java.net.URLClassLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DesktopModules core main class
 *
 * @author LabyStudio
 */
public class DesktopModules {

    public static final int TICKS_PER_SECOND = 20;

    private final URLClassLoader classLoader;

    private final File workingDirectory = new File(System.getenv("APPDATA") + "/DesktopModules");

    private final SourceLoader sourceLoader = new SourceLoader(this, this.workingDirectory);
    private final TextureLoader textureLoader = new TextureLoader(this);

    /**
     * Create an instance of the DesktopModules application and load all addons using the given classloader
     *
     * @param classLoader Class loader to use for the addons
     */
    public DesktopModules(URLClassLoader classLoader) throws Exception {
        this.classLoader = classLoader;

        // Create working directory
        if (!this.workingDirectory.exists()) {
            this.workingDirectory.mkdir();

            this.sourceLoader.setupDirectory();
        }

        // Add system tray
        new TrayHandler(this).init();

        // Create shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    /**
     * Initialize the application
     */
    public void init() {
        // Load all addons
        this.sourceLoader.loadAddonsInDirectoryAsync();

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
     * Shutdown the application
     */
    public void shutdown() {
        // Disable all addons
        for (Addon addon : this.sourceLoader.getAddons()) {
            try {
                addon.onDisable();
                addon.saveConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
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
