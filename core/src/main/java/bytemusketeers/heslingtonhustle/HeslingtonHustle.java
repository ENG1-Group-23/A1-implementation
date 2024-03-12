package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import main.java.bytemusketeers.heslingtonhustle.Screens.PlayScreen;
import main.java.bytemusketeers.heslingtonhustle.Sprites.TileMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HeslingtonHustle extends Game {
    public SpriteBatch batch;
    public PlayScreen gameScreen;
    public static final float W_WIDTH = 800;
    public static final float W_HEIGHT = 480;
    public static final float PPM = 100;
    public static final float WIDTH_METRES_BOUND = 4;
    public static final float HEIGHT_METRES_BOUND = 2.4f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameScreen = new PlayScreen(this, "Maps/test-map.tmx",
            new Vector2(HeslingtonHustle.W_WIDTH / 2 / HeslingtonHustle.PPM,
                HeslingtonHustle.W_HEIGHT / 2 / HeslingtonHustle.PPM),
            new Vector2(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM / 2,
                HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM / 2));
        setScreen(gameScreen);
    }

    public void setPiazzaScreen () {
        setScreen(new PlayScreen(this, "Maps/piazza-map.tmx", new Vector2(304f * TileMap.SCALE, 32f * TileMap.SCALE), new Vector2(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM / 2, HeslingtonHustle.HEIGHT_METRES_BOUND)));
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
