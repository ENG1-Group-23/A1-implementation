package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * An {@link Item} denotes a drawable non-{@link Character} object.
 */
class Item extends Sprite implements Drawable {
    /**
     * The LibGDX {@link Drawable} graphical representation of the {@link Sprite}
     */
    private final Texture texture;

    /**
     * The width of the {@link Item}, in pixels
     */
    private final float width;

    /**
     * The height of the {@link Item}, in pixels
     */
    private final float height;

    /**
     * The collision and interaction {@link Body} zone of the {@link Item}
     *
     * @see com.badlogic.gdx.physics.box2d.World
     */
    private final Body body;

    /**
     * Releases all resources used by the {@link Item}
     */
    @Override
    public void dispose() {
        texture.dispose();
    }

    /**
     * Registers the current {@link Item} onto the given game instance {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} to which the {@link Item} should be polled
     */
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x - (width / 2),body.getPosition().y - (height / 2), width,
            height);
    }

    /**
     * Retrieves the position of the {@link Item}
     *
     * @return The position of the body
     */
    public Vector2 getPosition() {
        return body.getPosition();
    }

    /**
     * Instantiates a new {@link Item} with the given initial parameters
     *
     * @param position The initial position of the {@link Item}
     * @param texture The initial {@link Sprite} {@link Texture}
     * @param area The {@link Area} into which the {@link Item} should be drawn
     * @param width The initial width
     * @param height The initial height
     */
    public Item(Vector2 position, Texture texture, Area area, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;

        body = area.registerCollisionBody(position, BodyDef.BodyType.StaticBody, width, height);
    }
}
