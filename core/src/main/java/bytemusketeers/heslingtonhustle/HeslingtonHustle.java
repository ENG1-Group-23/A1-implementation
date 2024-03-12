package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import main.java.bytemusketeers.heslingtonhustle.Screens.PlayScreen;
import main.java.bytemusketeers.heslingtonhustle.Sprites.TileMap;

import java.util.HashMap;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HeslingtonHustle extends Game {
    public SpriteBatch batch;
    public PlayScreen gameScreen;
    public static final float W_WIDTH = 800;
    public static final float W_HEIGHT = 480;
    public static final float PPM = 100;
    public static final float WIDTH_METRES_BOUND = 4;
    public static final float HEIGHT_METRES_BOUND = 2.4f;
    public Map<Integer, PlayScreen> screens = new HashMap<>();
    /*
    0 = playscreen
    1 = piazzascreen
     */

    @Override
    public void create() {
        batch = new SpriteBatch();
        PlayScreen playScreen = new PlayScreen(this, "Maps/test-map.tmx", new Vector2(HeslingtonHustle.W_WIDTH / 2 / HeslingtonHustle.PPM,
            HeslingtonHustle.W_HEIGHT / 2 / HeslingtonHustle.PPM),
            new Vector2(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM / 2,
                HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM / 2));
        PlayScreen piazzaScreen = new PlayScreen(this, "Maps/piazza-map.tmx", new Vector2(HeslingtonHustle.W_WIDTH / 2 / HeslingtonHustle.PPM,
            HeslingtonHustle.W_HEIGHT / 2 / HeslingtonHustle.PPM),
            new Vector2(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM / 2,
                HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM / 2));
        screens.put(0, playScreen);
        screens.put(1, piazzaScreen);
        setScreen(playScreen);
    }

    public void setCurrentScreen (int index) {
        setScreen(screens.get(index));
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
