package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import main.java.bytemusketeers.heslingtonhustle.Screens.PlayScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HeslingtonHustle extends Game {
    public SpriteBatch batch;
    public static final float W_WIDTH = 800;
    public static final float W_HEIGHT = 480;
    public static final float PPM = 100;


    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
