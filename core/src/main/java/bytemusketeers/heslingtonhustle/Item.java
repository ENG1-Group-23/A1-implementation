package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * An {@link Item} denotes a drawable non-{@link main.java.bytemusketeers.heslingtonhustle.Sprites.Character} object.
 */
public class Item extends Sprite {
    protected Texture texture;
    protected float width;
    protected float height;
    protected Body body;
    protected World world;

    public void defineBody(Vector2 position){
        BodyDef bodyDef = new BodyDef();
        // Set position for the collision box
        bodyDef.position.set(position.x, position.y);
        // Set the type of the body
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Create a body in the game world
        body = world.createBody(bodyDef);
        // Create a fixture for the body and setting its shape
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(width / 2, height / 2); // Creates a rectangle shaped box around shape
        body.createFixture(collisionBox,0.0f);
        collisionBox.dispose();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2 getPosition() {
        return new Vector2(this.getX(), this.getY());
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
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
        this.world = world;
        this.width = width;
        this.height = height;

        setPosition(position.x, position.y);
        defineBody(position);
    }
}
