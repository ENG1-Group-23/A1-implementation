package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * An {@link Overlay} denotes a distinctly {@link Drawable} scene, typically rendered on top of a
 * {@link com.badlogic.gdx.Screen}
 */
abstract class Overlay implements Drawable {
    protected static final int GENERAL_PADDING = 10;
    private final Stage stage;

    /**
     * Adds an {@link Actor} to the {@link Overlay}
     *
     * @param actor The element to add to the {@link Stage}
     * @see Actor
     */
    protected void addActor(Actor actor) {
        stage.addActor(actor);
    }

    /**
     * Releases all resources used by the {@link Overlay}
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Renders the current {@link Overlay} to the given {@link SpriteBatch}
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
     * Creates a new {@link Overlay} relating to the given {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} to which the {@link Overlay} should be connected
     */
    public Overlay(SpriteBatch batch) {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
    }
}
