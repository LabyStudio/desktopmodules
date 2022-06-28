package de.labystudio.desktopmodules.demo;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.demo.modules.DemoModule;

public class DemoAddon extends Addon {

    @Override
    public void onInitialize() throws Exception {
        // Config example
        if (!this.config.has("flag")) {
            this.config.addProperty("flag", true);

            System.out.println("This addon started for the first time. My config can remember that!");
        }

        // Another config example with default values
        String startupMessage = getConfigValue(this.config, "startup_message", "Hello World!");
        System.out.println(startupMessage);

        this.registerModule(DemoModule.class);
    }

    @Override
    public void onEnable() throws Exception {
        System.out.println("Demo addon enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("Demo addon disabled!");
    }
}
