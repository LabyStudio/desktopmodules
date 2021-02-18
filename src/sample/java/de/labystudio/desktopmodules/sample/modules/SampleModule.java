package de.labystudio.desktopmodules.sample.modules;

import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.addon.Module;
import de.labystudio.desktopmodules.core.renderer.wrapper.IModuleRenderer;
import de.labystudio.desktopmodules.core.renderer.wrapper.IRenderContext;
import de.labystudio.desktopmodules.core.renderer.swing.SwingModuleRenderer;
import de.labystudio.desktopmodules.core.renderer.wrapper.font.Font;
import de.labystudio.desktopmodules.core.renderer.wrapper.font.FontStyle;
import de.labystudio.desktopmodules.core.renderer.wrapper.font.StringAlignment;
import de.labystudio.desktopmodules.core.renderer.wrapper.font.StringEffect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SampleModule extends Module {

    private static final Font SAMPLE_FONT = new Font("Impact", FontStyle.PLAIN, 14);

    private BufferedImage sampleTexture;

    @Override
    public void onInitialize(Addon addon) {
        super.onInitialize(addon);

        System.out.println("Module of " + this.addon.getAddonData().name + " initialized");
    }

    @Override
    public void loadTextures(Addon addon) {
        this.sampleTexture = addon.loadTexture("textures/sample/sample.png");
    }

    @Override
    protected IModuleRenderer createRenderer() {
        return new SwingModuleRenderer(this, 250, 60);
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onRender(IRenderContext context, int width, int height) {
        context.fillRect(0, 0, width - 1, height - 1, new Color(50, 50, 50, 130));
        context.setFont(SAMPLE_FONT);
        context.drawString("Sample Module!", width - 10, 20, StringAlignment.RIGHT, StringEffect.NONE, Color.WHITE);
        context.drawImage(this.sampleTexture, 10, height / 2 - this.sampleTexture.getHeight() / 2);
    }
}
