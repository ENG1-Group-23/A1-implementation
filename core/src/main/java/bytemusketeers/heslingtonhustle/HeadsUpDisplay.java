package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The {@link HeadsUpDisplay} displays persistent player and game information throughout the gameplay
 *
 * @see PlayScreen
 */
class HeadsUpDisplay implements Drawable {
    private final Stage stage;

    /**
     * Releases all resources used by the {@link HeadsUpDisplay}
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Renders the current {@link HeadsUpDisplay} to the given {@link SpriteBatch}
     *
     * @param batch Target of the rendering operation
     */
    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(stage.getCamera().combined);
        stage.act();
        stage.draw();
    }

    /**
     * Instantiates a new {@link HeadsUpDisplay} to be displayed over the {@link PlayScreen}
     */
    public HeadsUpDisplay(SpriteBatch batch) {
        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label testLabel = new Label("Hello, HeadsUpDisplay!", labelStyle);
        testLabel.setPosition(0, Gdx.graphics.getHeight() - testLabel.getHeight());
        stage.addActor(testLabel);
    }
}
