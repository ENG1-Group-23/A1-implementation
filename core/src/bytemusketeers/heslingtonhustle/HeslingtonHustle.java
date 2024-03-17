package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The {@link Game} is the {@link com.badlogic.gdx.ApplicationListener} shared by all platforms
 */
class HeslingtonHustle extends Game {
    /**
     * 'PPM' denotes the number of pixels-per-(in-game)-metre. This is useful for converting between
     * {@link com.badlogic.gdx.Graphics} units and in-game metres.
     *
     * @see #scaleToMetres(int)
     */
    private static final float PPM = 100;

    /**
     * The LibGDX {@link SpriteBatch} used during render-time
     *
     * @see Drawable#render(SpriteBatch)
     * @see com.badlogic.gdx.graphics.g2d.Sprite
     */
    public SpriteBatch batch;

    /**
     * The LibGDX {@link Screen} array used to store all active {@link Game}-wide {@link Screen} instantiations
     *
     * @see #setScreen(Screen)
     */
    private Screen[] screens;

    /**
     * Scales a pixel component to in-game metres
     *
     * @param value The quantity to scale, in pixels
     * @return The corresponding quantity of in-game metres
     * @implNote Future implementors may wish to parameterise the {@link #PPM} in terms of the screen DPI-equivalent;
     *           see {@link Graphics#getPpiX()} and friends.
     */
    public static float scaleToMetres(int value) {
        return value / PPM;
    }

    /**
     * Handles the creation of the {@link com.badlogic.gdx.Application}
     *
     * @apiNote If the {@link PlayScreen} and its constituent {@link Area} objects cannot be created, this method
     *          halts the JVM-level process with a non-zero exit code. This is not graceful, and is primarily used for
     *          testing purposes. In practice, this would only occur if the TMX tile-map assets have been externally
     *          corrupted.
     *
     * @see InvalidAreaException
     */
    @Override
    public void create() {
        batch = new SpriteBatch();

        try {
            screens = new Screen[]{new PlayScreen(batch)};
            setScreen(screens[0]);
        } catch (InvalidAreaException iae) {
            //noinspection CallToPrintStackTrace
            iae.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Releases all resources used by the {@link Game}
     */
    @Override
    public void dispose() {
        for (Screen screen : screens)
            screen.dispose();

        batch.dispose();
        super.dispose();
    }
}
