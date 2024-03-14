package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Character} class represents the avatar of the player in the game, extending the {@link Sprite}. The
 * {@link Character} is a unique given its ability to exist in multiple {@link Area}s across gameplay; the
 * {@link PlayScreen} must inform {@link Character} of any {@link Area} changes; see
 * {@link #switchCharacterContext(int)}.
 * TODO: is there a better way to identify areas instead of a numerical index?
 */
class Character extends Sprite implements Drawable {
    private static final float WIDTH = 0.3f;
    private static final float HEIGHT = 0.3f;
    private static final float MOVEMENT_VELOCITY = 4.0f;
    private static final String TEXTURE_PATH = "prototype-4.png";
    private final List<Body> bodies = new ArrayList<>();
    private Body activeBody;
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

        activeBody.setLinearVelocity(velocity);
        velocity.setZero();
    }

    /**
     * Checks if the {@link Character} is out of the given {@link Area} boundaries, on the horizontal axis
     *
     * @param area The {@link Area} against which the boundary should be tested
     * @return Is the {@link Character} out-of-bounds on the horizontal of the {@link Area}?
     */
    public boolean isWithinHorizontalBound(Area area) {
        Vector2 position = getPosition();
        return (position.x >= HeslingtonHustle.WIDTH_METRES_BOUND && position.x <= area.getMapWidth() -
            HeslingtonHustle.WIDTH_METRES_BOUND);
    }

    /**
     * Checks if the {@link Character} is out of the given {@link Area} boundaries, on the vertical axis
     *
     * @param area The {@link Area} against which the boundary should be tested
     * @return Is the {@link Character} out-of-bounds on the vertical of the {@link Area}?
     */
    public boolean isWithinVerticalBound(Area area) {
        Vector2 position = getPosition();
        return (position.y >= HeslingtonHustle.HEIGHT_METRES_BOUND && position.y <= area.getMapHeight() -
            HeslingtonHustle.HEIGHT_METRES_BOUND);
    }

    /**
     * Retrieves the {@link Character} position
     *
     * @return The position
     */
    public Vector2 getPosition() {
        return activeBody.getPosition();
    }

    /**
     * Switch the {@link Character} context to {@link Area} identified by the given index
     *
     * @param areaIdx The index of the new {@link Area}
     */
    public void switchCharacterContext(int areaIdx) {
        activeBody = bodies.get(areaIdx);
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
        Vector2 position = getPosition();
        batch.draw(playerTexture, position.x - WIDTH / 2, position.y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    /**
     * Initialises a new {@link Character} body as a player-movable {@link Sprite}
     *
     * @param areas All {@link Area}s in which the {@link Character} should exist
     * @param defaultAreaIdx The initial area index
     */
    public Character(List<Area> areas, int defaultAreaIdx) {
        for (Area area : areas)
            bodies.add(area.registerCharacter(WIDTH, HEIGHT));

        switchCharacterContext(defaultAreaIdx);
        playerTexture = new Texture(TEXTURE_PATH);
    }
}
