# DesktopModules
DesktopModules is an application that allows you to create overlays for your operating system.
The modules are displayed over all your programs and games and are freely movable.

![preview](.github/assets/preview.png)

## Available Addons
- [Spotify](https://github.com/LabyStudio/spotify-addon)
- [SmartHome](https://github.com/LabyStudio/smarthome-addon) (FritzBox client list and IP-Camera feed)

*Feel free to add your addons!*

## Installation
1. Download the latest executable file [here](https://github.com/LabyStudio/desktopmodules/releases/).
2. Execute the ``DesktopModules.exe``
3. Right-click the DesktopModules tray icon in your task bar and click on ``Open addons directory``
4. Place your [addon jars](#available-addons) in the opened directory (``C:/Users/<name>/AppData/Roaming/DesktopModules/addons/``)
5. Close the DesktopModules application. (Right-click the tray icon -> ``Exit``)
6. Execute the ``DesktopModules.exe`` again to load all addons

## Settings
You can left-click on the tray icon to manage your installed addons.<br>
You can exit the application by right-clicking the tray icon and selecting "Exit"

![settings](.github/assets/settings.png)

## Create your own addon
### Setup gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.LabyStudio:desktopmodules:2.5.3:all'
}
```

### Setup your code

The main class of your addon. You have to register your modules in the onEnable method.
```java
import de.labystudio.desktopmodules.core.addon.Addon;

public class TestAddon extends Addon {

    @Override
    public void onInitialize() throws Exception {
        // Config example
        if (!this.config.has("flag")) {
            this.config.addProperty("flag", true);

            System.out.println("This addon started for the first time. My config can remember that!");
        }

        // Another config example with default values
        String startupMessage = getConfigValue(this.config, "startup_message", "Hello World!");
        System.out.println(startupMessage);
        
        // Register your modules
        registerModule(TestModule.class);
    }

    @Override
    public void onEnable() {
        // Called when at least one module of the addon activates
        System.out.println("Test addon enabled!");
    }

    @Override
    public void onDisable() {
        // Called when all modules of the addon are disabled
        System.out.println("Test addon disabled!");
    }
}
```

You can create multiple modules for one addon

```java
import de.labystudio.desktopmodules.core.addon.Addon;
import de.labystudio.desktopmodules.core.loader.TextureLoader;
import de.labystudio.desktopmodules.core.module.Module;
import de.labystudio.desktopmodules.core.module.render.IModuleRenderer;
import de.labystudio.desktopmodules.core.renderer.IRenderContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TestModule extends Module<TestAddon> {

    private BufferedImage testTexture;

    public TestModule() {
        super(250, 60); // The fixed size of the module
    }

    @Override
    public void onTick() {
        // Do backend stuff..
    }

    @Override
    public void onRender(IRenderContext context, int width, int height) {
        context.drawRect(0, 0, width - 1, height - 1, new Color(50, 50, 50, 130));
        context.drawImage(this.testTexture, 0, 0, height, height);

        // Render your module...
    }
    
    @Override
    public void loadTextures(TextureLoader textureLoader) {
        this.testTexture = textureLoader.loadTexture("textures/test/test.png"); // Load a texture
    }
    
    @Override
    protected String getIconPath() {
        return "textures/test/test.png"; // The settings icon of the module
    }

    @Override
    public String getDisplayName() {
        return "Test Module"; // The setting name of the module
    }
}
```

### Test your addon
Launch the main class ``Start`` with the program parameter ``your.package.name.TestAddon``

### Build your addon
To make it available as a jar file, you have to define the addon class name in the ``/addon.json``
```json
{
  "main": "your.package.name.TestAddon"
}
```

To use your addon, put the jar file into the following directory: ``C:/Users/<name>/AppData/Roaming/DesktopModules/addons/``
