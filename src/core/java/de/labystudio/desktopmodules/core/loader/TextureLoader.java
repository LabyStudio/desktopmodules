package de.labystudio.desktopmodules.core.loader;

import de.labystudio.desktopmodules.core.DesktopModules;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
    public BufferedImage load(String path) {
        try {
            URLClassLoader classLoader = this.desktopModules.getClassLoader();
            try (InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(path))) {
                return ImageIO.read(inputStream);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Load buffered image from resources path
     *
     * @param path Resources path
     * @return Buffered image
     * @deprecated Renamed to {@link TextureLoader#load(String)}
     */
    @Deprecated
    public BufferedImage loadTexture(String path) {
        return load(path);
    }

    /**
     * Load buffered image from resources path with given size
     *
     * @param path   Resources path
     * @param width  Image width
     * @param height Image height
     * @return Buffered image
     */
    public BufferedImage load(String path, int width, int height) {
        BufferedImage image = load(path);

        // Resize
        BufferedImage resized = new BufferedImage(width, height, image.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();

        return resized;
    }

    /**
     * Load buffered image from resources path with given size
     *
     * @param path   Resources path
     * @param width  Image width
     * @param height Image height
     * @return Buffered image
     * @deprecated Renamed to {@link TextureLoader#load(String, int, int)}
     */
    @Deprecated
    public BufferedImage loadTexture(String path, int width, int height) {
        return load(path, width, height);
    }

}
