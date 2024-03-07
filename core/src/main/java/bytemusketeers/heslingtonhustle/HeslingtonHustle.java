package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import main.java.bytemusketeers.heslingtonhustle.Screens.PlayScreen;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HeslingtonHustle extends Game {
    public SpriteBatch batch;
    private ArrayList<Drawable> drawables = new ArrayList<>();
    public static final float W_WIDTH = 800;
    public static final float W_HEIGHT = 480;
    public static final float PPM = 100;
    public final String assets = "";


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
