package de.labystudio.desktopmodules.sample;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.sample.modules.SampleModule;

public class SampleAddon extends Addon {

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

        registerModule(SampleModule.class);
    }

    @Override
    public void onEnable() throws Exception {
        System.out.println("Sample addon enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("Sample addon disabled!");
    }
}
