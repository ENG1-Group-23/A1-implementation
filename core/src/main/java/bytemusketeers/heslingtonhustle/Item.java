package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * An {@link Item} denotes a drawable non-{@link Character} object.
 */
class Item extends Sprite implements Drawable {
    private final Texture texture;
    private final float width;
    private final float height;
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
     * @param world The {@link World} into which the {@link Item} should be drawn
     * @param width The initial width
     * @param height The initial height
     */
    public Item(Vector2 position, Texture texture, World world, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(position.x, position.y);
        bodyDefinition.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDefinition);

        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(width / 2, height / 2);
        body.createFixture(collisionBox,0.0f);
        collisionBox.dispose();
    }
}
