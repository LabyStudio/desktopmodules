package de.labystudio.desktopmodules.core.loader;

import de.labystudio.desktopmodules.core.DesktopModules;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Objects;

public class TextureLoader {

    private final DesktopModules desktopModules;

    public TextureLoader(DesktopModules desktopModules) {
        this.desktopModules = desktopModules;
    }

    /**
     * Load buffered image from resources path
     *
     * @param path Resources path
     * @return Buffered image
     */
    public BufferedImage loadTexture(String path) {
        try {
            URLClassLoader classLoader = this.desktopModules.getClassLoader();
            return ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(path)));
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Load buffered image from resources path with given size
     *
     * @param path   Resources path
     * @param width  Image width
     * @param height Image height
     * @return Buffered image
     */
    public BufferedImage loadTexture(String path, int width, int height) {
        BufferedImage image = loadTexture(path);

        // Resize
        BufferedImage resized = new BufferedImage(width, height, image.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();

        return resized;
    }

}
