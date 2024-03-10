package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import main.java.bytemusketeers.heslingtonhustle.Screens.PlayScreen;
import main.java.bytemusketeers.heslingtonhustle.Screens.TitleScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HeslingtonHustle extends Game {
    public SpriteBatch batch;
    //added for screens
    public ShapeRenderer shapeRenderer;
    //added for screens
    public BitmapFont font;
    public static final float W_WIDTH = 800;
    public static final float W_HEIGHT = 480;
    public static final float PPM = 100;
    public static final float WIDTH_METRES_BOUND = 4;
    public static final float HEIGHT_METRES_BOUND = 2.4f;


    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        setScreen(new TitleScreen(this));
        //setScreen(new PlayScreen(this));
    }

    //@Override
    //public void render() {super.render();}


    //public void dispose() {batch.dispose();}
    //added for TitleScreen
    @Override
    public void dispose () {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
