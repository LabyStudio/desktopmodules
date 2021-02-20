import de.labystudio.desktopmodules.core.DesktopModules;

import javax.swing.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Entry point of the core
 *
 * @author LabyStudio
 */
public class Start {

    /**
     * The entry point
     *
     * @param args Program arguments (unused)
     * @throws Exception Can throw exceptions of the core class
     */
    public static void main(String[] args) throws Exception {
        // Create URL class loader for this application
        URLClassLoader classLoader = new URLClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
        Thread.currentThread().setContextClassLoader(classLoader);

        // Set system look
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Create and initialize application
        DesktopModules desktopModules = new DesktopModules(classLoader);
        desktopModules.init();

        // Load internal addon
        if (args.length != 0) {
            desktopModules.getSourceLoader().registerAddon(args[0]);
        }
    }


}
