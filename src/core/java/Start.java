import de.labystudio.desktopmodules.core.DesktopModules;
import de.labystudio.desktopmodules.core.loader.SourceClassLoader;

import javax.swing.UIManager;
import java.net.URL;

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
        SourceClassLoader classLoader = new SourceClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
        Thread.currentThread().setContextClassLoader(classLoader);

        // Set system look
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Create application
        DesktopModules desktopModules = new DesktopModules(classLoader);

        // Load internal addon
        boolean loadAddons = args.length == 0 || desktopModules.getSourceLoader().registerAddon(args[0]) == null;

        // Initialize core application
        desktopModules.init(loadAddons);
    }


}
