package de.labystudio.desktopmodules.core.loader;

import java.net.URL;
import java.net.URLClassLoader;

public class SourceClassLoader extends URLClassLoader {

    public SourceClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }
}
