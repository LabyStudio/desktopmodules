package de.labystudio.desktopmodules.sample;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.sample.modules.SampleModule;

public class SampleAddon extends Addon {

    @Override
    public void onEnable() throws Exception {
        System.out.println("Sample addon enabled!");

        registerModule(SampleModule.class);
    }

    @Override
    public void onDisable() {
        System.out.println("Sample addon disabled!");
    }
}
