package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The {@link Game} is the {@link com.badlogic.gdx.ApplicationListener} shared by all platforms
 */
class HeslingtonHustle extends Game {
    /**
     * 'PPM' denotes the number of pixels-per-(in-game)-metre.
     * TODO: Derive this from Gdx.graphics properties
     */
    public static final float PPM = 100;
    /**
     * TODO: WIDTH_METRES_BOUND?
     */
    public static final float WIDTH_METRES_BOUND = 4;
    /**
     * TODO: HEIGHT_METRES_BOUND?
     */
    public static final float HEIGHT_METRES_BOUND = 2.4f;
    public SpriteBatch batch;
    private Screen[] screens;

    /**
     * Handles the creation of the {@link com.badlogic.gdx.Application}
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        screens = new Screen[] { new PlayScreen(batch) };

        setScreen(screens[0]);
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
