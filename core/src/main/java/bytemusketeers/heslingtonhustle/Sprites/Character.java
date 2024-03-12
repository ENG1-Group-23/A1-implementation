package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;

/**
 * The {@link Character} class represents the avatar of the player in the game, extending the {@link Sprite}.
 */
public class Character extends Sprite {
    public static final float WIDTH = 0.3f;
    public static final float HEIGHT = 0.3f;
    private static final float MOVEMENT_VELOCITY = 4.0f;
    public Body b2body;
    public Texture playerTexture;
    private final Vector2 velocity = new Vector2();

    public Character(World world, Vector2 initialPosition) {
        BodyDef bodyDefinition = new BodyDef();

        bodyDefinition.position.set(initialPosition.x, initialPosition.y);
        bodyDefinition.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / HeslingtonHustle.PPM, 15 / HeslingtonHustle.PPM);
        b2body.createFixture(shape, 0.0f);
        shape.dispose();

        playerTexture = new Texture("prototype-4.png");
    }

    public void moveUp() {
        velocity.y = MOVEMENT_VELOCITY;
    }

    public void moveDown() {
        velocity.y = -MOVEMENT_VELOCITY;
    }

    public void moveLeft() {
        velocity.x = -MOVEMENT_VELOCITY;
    }

    public void moveRight() {
        velocity.x = MOVEMENT_VELOCITY;
    }

    public void move() {
        if (velocity.x != 0 && velocity.y != 0) {
            velocity.x /= 1.5f;
            velocity.y /= 1.5f;
        }

        b2body.setLinearVelocity(velocity);
        velocity.setZero();
    }
}
