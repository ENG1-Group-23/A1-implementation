package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Launches the desktop (LWJGL3) application.
 *
 * @author Oliver Dixon
 */
public class DesktopLauncher {
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("Heslington Hustle");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
        configuration.setWindowedMode(800, 480);
        configuration.setWindowIcon("logo.png");

        return configuration;
    }

    public static void main(String[] args) throws RuntimeException {
        if (StartupHelper.startNewJvmIfRequired())
            return; // This handles macOS support and helps on Windows.

        if (Runtime.version().feature() != 11)
            throw new RuntimeException("Not using Java 11");

        new Lwjgl3Application(new HeslingtonHustle(), getDefaultConfiguration());
    }
}
