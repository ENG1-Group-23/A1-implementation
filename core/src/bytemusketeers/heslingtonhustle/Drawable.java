package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * A {@link Drawable} represents a {@link Disposable} that may be rendered to a {@link SpriteBatch}
 *
 * @implNote Ideally, all {@link Drawable} and/or {@link Disposable} objects would be managed through the standard
 *           LibGDX {@link com.badlogic.gdx.assets.AssetManager}, providing a JVM-like garbage-collection framework to
 *           manage on-board GPU entities at runtime, e.g. {@link com.badlogic.gdx.graphics.Texture} and
 *           {@link com.badlogic.gdx.graphics.g2d.BitmapFont}. Alas, for the relatively small number of {@link Drawable}
 *           implementors currently constructed from {@link com.badlogic.gdx.Game}, there is no material difference
 *           between this and the existing approach of manually invoking {@link Disposable#dispose()} on every allocated
 *           {@link Disposable} come {@link Game#dispose()}.
 *
 * @author ENG1 Team 23 (Cohort 3)
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
