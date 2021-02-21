package de.labystudio.desktopmodules.core.gui.util;

import de.labystudio.desktopmodules.core.DesktopModules;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WinUtils {

    public static void openFileEditor(File file) {
        try {
            if (DesktopModules.OS_WIN) {
                // Use windows command to open the default text editor
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath());
            } else {
                // Use java api to open the text editor
                Desktop.getDesktop().edit(file);
            }
        } catch (IOException exception) {
            exception.printStackTrace();

            // Open it in the browser
            try {
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
