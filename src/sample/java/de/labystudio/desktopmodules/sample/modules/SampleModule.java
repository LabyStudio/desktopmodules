package de.labystudio.desktopmodules.sample.modules;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.Module;
import de.labystudio.desktopmodules.core.module.wrapper.IModuleRenderer;
import de.labystudio.desktopmodules.core.renderer.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.swing.SwingModuleRenderer;
import de.labystudio.desktopmodules.core.renderer.font.Font;
import de.labystudio.desktopmodules.core.renderer.font.FontStyle;
import de.labystudio.desktopmodules.core.renderer.font.StringAlignment;
import de.labystudio.desktopmodules.core.renderer.font.StringEffect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SampleModule extends Module {

    private static final Font SAMPLE_FONT = new Font("Impact", FontStyle.PLAIN, 24);

    private BufferedImage sampleTexture;

    @Override
    public void onInitialize(Addon addon, boolean enabled) {
        super.onInitialize(addon, enabled);

        System.out.println("Module of " + this.addon.getDisplayName() + " initialized");
    }

    @Override
    public void loadTextures(TextureLoader textureLoader) {
        this.sampleTexture = textureLoader.loadTexture("textures/sample/sample.png");
    }

    @Override
    protected IModuleRenderer createRenderer() {
        return new SwingModuleRenderer(this, 250, 60);
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
        context.fillRect(0, 0, width - 1, height - 1, new Color(50, 50, 50, 130));
        context.setFont(SAMPLE_FONT);
        context.drawString("Sample Module!", width - 20, 38, StringAlignment.RIGHT, StringEffect.NONE, Color.WHITE);
        context.drawImage(this.sampleTexture, 0, 0, height, height);
    }
}
