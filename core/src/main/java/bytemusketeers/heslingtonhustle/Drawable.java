package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * A {@link Drawable} represents any type that may be rendered on the screen canvas area.
 *
 * @author Oliver Dixon
 * @author Rafael Duarte
 */
abstract public class Drawable {
    public Texture texture;
    public Vector2 position;
    public SpriteBatch spriteBatch;

    /**
     * Renders the drawable on the screen
     */
    abstract public void draw();
}
