package de.labystudio.desktopmodules.core.os;

/**
 * An enum with different OS
 *
 * @author LabyStudio
 */
public enum OperatingSystem {
    LINUX("linux", "unix"),
    SOLARIS("solaris", "sunos"),
    WINDOWS("win"),
    MACOS("mac"),
    UNKNOWN;

    private final String[] osNames;

    OperatingSystem(String... osNames) {
        this.osNames = osNames;
    }

    public String[] getOsNames() {
        return osNames;
    }

    /**
     * Gets the OS/platform
     *
     * @return the platform's enum
     */
    public static OperatingSystem getPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();

        for (OperatingSystem operatingSystem : OperatingSystem.values()) {
            if (operatingSystem == UNKNOWN)
                continue;

            for (String name : operatingSystem.getOsNames()) {
                if (osName.contains(name)) {
                    return operatingSystem;
                }
            }
        }

        return UNKNOWN;
    }
}
