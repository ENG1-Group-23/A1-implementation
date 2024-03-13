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
 * The {@link Character} class represents the avatar of the player in the game, extending the {@link Sprite}.
 */
class Character extends Sprite implements Drawable {
    private static final float WIDTH = 0.3f;
    private static final float HEIGHT = 0.3f;
    private static final float MOVEMENT_VELOCITY = 4.0f;
    private static final String TEXTURE_PATH = "prototype-4.png";
    private final Body body;
    private final Texture playerTexture;
    private final Vector2 velocity = new Vector2();

    /**
     * Moves the {@link Character} upwards on the Y-axis
     */
    public void moveUp() {
        velocity.y = MOVEMENT_VELOCITY;
    }

    /**
     * Moves the {@link Character} downwards on the Y-axis
     */
    public void moveDown() {
        velocity.y = -MOVEMENT_VELOCITY;
    }

    /**
     * Moves the {@link Character} leftwards on the X-axis
     */
    public void moveLeft() {
        velocity.x = -MOVEMENT_VELOCITY;
    }

    /**
     * Moves the {@link Character} rightwards on the X-axis
     */
    public void moveRight() {
        velocity.x = MOVEMENT_VELOCITY;
    }

    /**
     * Updates the position of the {@link Character} given its transient velocity
     * TODO: consistent movement speeds on a resized window
     */
    public void move() {
        if (velocity.x != 0 && velocity.y != 0) {
            velocity.x /= 1.5f;
            velocity.y /= 1.5f;
        }

        body.setLinearVelocity(velocity);
        velocity.setZero();
    }

    /**
     * Checks if the {@link Character} is out of the given {@link Area} boundaries, on the horizontal axis
     *
     * @param area The {@link Area} against which the boundary should be tested
     * @return Is the {@link Character} out-of-bounds on the horizontal of the {@link Area}?
     */
    public boolean isOutOfHorizontalBound(Area area) {
        return (getXPosition() >= HeslingtonHustle.WIDTH_METRES_BOUND &&
            getXPosition() <= area.getMapWidth() - HeslingtonHustle.WIDTH_METRES_BOUND);
    }

    /**
     * Checks if the {@link Character} is out of the given {@link Area} boundaries, on the vertical axis
     *
     * @param area The {@link Area} against which the boundary should be tested
     * @return Is the {@link Character} out-of-bounds on the vertical of the {@link Area}?
     */
    public boolean isOutOfVerticalBound(Area area) {
        return (getYPosition() >= HeslingtonHustle.HEIGHT_METRES_BOUND &&
            getYPosition() <= area.getMapHeight() - HeslingtonHustle.HEIGHT_METRES_BOUND);
    }

    /**
     * Retrieves the {@link Character} position with respect to the horizontal axis
     *
     * @return The X position
     */
    public float getXPosition() {
        return body.getPosition().x;
    }

    /**
     * Retrieves the {@link Character} position with respect to the vertical axis
     *
     * @return The Y position
     */
    public float getYPosition() {
        return body.getPosition().y;
    }

    /**
     * Releases all resources used by the {@link Character}
     */
    @Override
    public void dispose() {
        playerTexture.dispose();
    }

    /**
     * Registers the current {@link Character} onto the given game instance {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} to which the {@link Character} should be polled
     */
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(playerTexture, getXPosition() - WIDTH / 2, getYPosition() - HEIGHT / 2,
            WIDTH, HEIGHT);
    }

    /**
     * Initialises a new {@link Character} body as a player-movable {@link Sprite}.
     *
     * @param world The LibGDX {@link World} into which the {@link Character} should exist
     * @param initialPosition The initial position of the {@link Character}
     */
    public Character(World world, Vector2 initialPosition) {
        BodyDef bodyDefinition = new BodyDef();

        bodyDefinition.position.set(initialPosition.x, initialPosition.y);
        bodyDefinition.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / HeslingtonHustle.PPM, 15 / HeslingtonHustle.PPM);
        body.createFixture(shape, 0.0f);
        shape.dispose();

        playerTexture = new Texture(TEXTURE_PATH);
    }
}
