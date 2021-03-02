package de.labystudio.desktopmodules.core.os;

import java.io.File;

/**
 * Utils to get the operating system and to get the working directory
 *
 * @author LabyStudio
 */
public class WorkingDirectory {

    /**
     * Gets an application's directory from AppData
     *
     * @param applicationName the application's name
     * @return the application's directory
     */
    public static File get(String applicationName) {
        String userHome = System.getProperty("user.home", ".");
        File workingDirectory;

        switch (OperatingSystem.getPlatform()) {
            case UNKNOWN:
            case MACOS:
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
            case WINDOWS:
            case SOLARIS:
                String applicationData = System.getenv("APPDATA");

                if (applicationData != null) {
                    workingDirectory = new File(applicationData, applicationName + '/');
                } else {
                    workingDirectory = new File(userHome, applicationName + '/');
                }
                break;
            default:
                workingDirectory = new File(userHome, applicationName + '/');
        }

        if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + workingDirectory);
        }

        return workingDirectory;
    }

}
