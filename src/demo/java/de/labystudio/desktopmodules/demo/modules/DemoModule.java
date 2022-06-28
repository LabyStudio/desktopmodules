package de.labystudio.desktopmodules.demo.modules;

import com.google.gson.JsonObject;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.Module;
import de.labystudio.desktopmodules.core.renderer.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.font.Font;
import de.labystudio.desktopmodules.core.renderer.font.FontStyle;
import de.labystudio.desktopmodules.core.renderer.font.StringEffect;
import de.labystudio.desktopmodules.demo.DemoAddon;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DemoModule extends Module<DemoAddon> {

    private static final Font DEMO_FONT = new Font("Impact", FontStyle.PLAIN, 24);

    private BufferedImage demoTexture;

    public DemoModule() {
        super(250, 60);
    }

    @Override
    public void onInitialize(DemoAddon addon, JsonObject config) {
        super.onInitialize(addon, config);

        System.out.println("Module of " + this.addon.getDisplayName() + " initialized");
    }

    @Override
    public void loadTextures(TextureLoader textureLoader) {
        this.demoTexture = textureLoader.load("textures/demo/demo.png");
    }

    @Override
    protected String getIconPath() {
        return "textures/demo/demo.png";
    }

    @Override
    public String getDisplayName() {
        return "Demo Module";
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onRender(IRenderContext context, int width, int height) {
        context.drawRect(0, 0, width - 1, height - 1, new Color(50, 50, 50, 130));
        context.drawString("Demo Module!", width, height + 20, 38, this.rightBound, StringEffect.NONE, Color.WHITE, DEMO_FONT);
        context.drawImage(this.demoTexture, this.rightBound ? width - height : 0, 0, height, height);
    }
}
