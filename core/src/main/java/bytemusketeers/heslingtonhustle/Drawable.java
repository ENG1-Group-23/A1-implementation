package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * A {@link Drawable} represents a {@link Disposable} that may be rendered to a {@link SpriteBatch}
 */
interface Drawable extends Disposable {
    /**
     * Renders the {@link Drawable} to the given {@link SpriteBatch}. This method must be called during a batch-drawing
     * sequence operation, i.e. between {@link SpriteBatch#begin()} and {@link SpriteBatch#end()}.
     *
     * @param batch Target of the rendering operation
     */
    void render(SpriteBatch batch);
}
