package de.labystudio.desktopmodules.sample.modules;

import com.google.gson.JsonObject;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.Module;
import de.labystudio.desktopmodules.core.renderer.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.font.Font;
import de.labystudio.desktopmodules.core.renderer.font.FontStyle;
import de.labystudio.desktopmodules.core.renderer.font.StringAlignment;
import de.labystudio.desktopmodules.core.renderer.font.StringEffect;
import de.labystudio.desktopmodules.sample.SampleAddon;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SampleModule extends Module<SampleAddon> {

    private static final Font SAMPLE_FONT = new Font("Impact", FontStyle.PLAIN, 24);

    private BufferedImage sampleTexture;

    public SampleModule() {
        super(250, 60);
    }

    @Override
    public void onInitialize(SampleAddon addon, JsonObject config) {
        super.onInitialize(addon, config);

        System.out.println("Module of " + this.addon.getDisplayName() + " initialized");
    }

    @Override
    public void loadTextures(TextureLoader textureLoader) {
        this.sampleTexture = textureLoader.load("textures/sample/sample.png");
    }

    @Override
    protected String getIconPath() {
        return "textures/sample/sample.png";
    }

    @Override
    public String getDisplayName() {
        return "Sample Module";
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onRender(IRenderContext context, int width, int height) {
        context.drawRect(0, 0, width - 1, height - 1, new Color(50, 50, 50, 130));
        context.drawString("Sample Module!", width, height + 20, 38, this.rightBound, StringEffect.NONE, Color.WHITE, SAMPLE_FONT);
        context.drawImage(this.sampleTexture, this.rightBound ? width - height : 0, 0, height, height);
    }
}
