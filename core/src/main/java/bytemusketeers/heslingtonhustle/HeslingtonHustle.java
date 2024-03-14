package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import main.java.bytemusketeers.heslingtonhustle.Screens.PiazzaScreen;
import main.java.bytemusketeers.heslingtonhustle.Screens.PlayScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HeslingtonHustle extends Game {
    public SpriteBatch batch;
    public static final float W_WIDTH = 800;
    public static final float W_HEIGHT = 480;
    public static final float PPM = 100;
    public static final float WIDTH_METRES_BOUND = 4;
    public static final float HEIGHT_METRES_BOUND = 2.4f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this, "Maps/test-map.tmx"));
    }

    public void setPiazzaScreen () {
        setScreen(new PiazzaScreen(this, "Maps/piazza-map.tmx"));
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
