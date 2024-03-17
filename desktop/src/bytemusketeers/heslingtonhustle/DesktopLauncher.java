package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Provides the JVM system entry point to launch the desktop {@link Lwjgl3Application} with the {@link HeslingtonHustle}
 * instance, specifying certain fixed application configuration parameters.
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
public class DesktopLauncher {
    /**
     * The initial window height, in pixels
     */
    private static final int INITIAL_WINDOW_WIDTH = 800;

    /**
     * The initial window width, in pixels
     */
    private static final int INITIAL_WINDOW_HEIGHT = 480;

    /**
     * The frames-per-second limit, fixed to ensure a consistent {@link Character} move-speed across differing monitor
     * refresh rates.
     */
    private static final int FPS_LIMIT = 60;

    /**
     * The title of the Java window
     */
    private static final String GAME_TITLE = "Heslington Hustle";

    /**
     * The file-path of the icon to be displayed on non-MacOS systems
     *
     * @see Lwjgl3ApplicationConfiguration#setWindowIcon(String...)
     */
    private static final String ICON_PATH = "logo.png";

    /**
     * Parameterises a {@link Lwjgl3ApplicationConfiguration} instance with sensible pre-determined defaults to be used
     * when constructing the {@link Lwjgl3Application} desktop window.
     *
     * @return The parameterised default configuration for the {@link Lwjgl3Application} window
     */
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setTitle(GAME_TITLE);
        configuration.useVsync(true);
        configuration.setForegroundFPS(FPS_LIMIT);
        configuration.setWindowedMode(INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT);
        configuration.setWindowIcon(ICON_PATH);

        return configuration;
    }

    /**
     * The system entry point: initialises the {@link Lwjgl3Application} and {@link HeslingtonHustle} as a LibGDX
     * {@link com.badlogic.gdx.Game}.
     *
     * @param args Textual command-line argument vector
     * @throws RuntimeException Throws back to the system handler if the runtime is interpreting an unsuitable Java
     *                          language version.
     */
    public static void main(String[] args) throws RuntimeException {
        if (StartupHelper.startNewJvmIfRequired())
            return; // This handles macOS support and helps on Windows.

        if (Runtime.version().feature() != 11)
            throw new RuntimeException("Not using Java 11");

        new Lwjgl3Application(new HeslingtonHustle(), getDefaultConfiguration());
    }
}
