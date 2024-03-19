package bytemusketeers.heslingtonhustle.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link Character} class represents the avatar of the player in the game, extending the {@link Sprite}.
 *
 * @apiNote The {@link Character} is a unique given its ability to exist in multiple {@link Area}s across gameplay; the
 *          {@link com.badlogic.gdx.Screen} implementation must inform {@link Character} of any {@link Area} changes;
 *          see {@link #switchCharacterContext(Area.Name)}.
 * @author ENG1 Team 23 (Cohort 3)
 */
public class Character extends Sprite implements Drawable {
    /**
     * The width of a {@link Character}, in in-game metres
     */
    private static final float WIDTH = 0.5f;

    /**
     * The height of a {@link Character}, in in-game metres
     */
    private static final float HEIGHT = 0.57f;

    /**
     * The standard moving velocity, across both axes, of a mobile {@link Character}. Specified in in-game metres per
     * second of continuous input.
     *
     * @see #moveUp()
     * @see #moveDown()
     * @see #moveLeft()
     * @see #moveRight()
     * @see #move()
     */
    private static final float MOVEMENT_VELOCITY = 4.0f;

    /**
     * The velocity correction factor by which movement should be slowed if the {@link Character} is traversing both
     * axes simultaneously; i.e. moving diagonally.
     *
     * @see #MOVEMENT_VELOCITY
     * @see #move()
     */
    private static final float MOVEMENT_VELOCITY_CORRECTION = (float) Math.sqrt(2);

    /**
     * The file path of the {@link Character}
     *
     * @see #playerTexture
     */
    private static final String TEXTURE_PATH = "character.png";

    /**
     * The visual representation of the {@link Character}
     *
     * @see #TEXTURE_PATH
     * @see #render(SpriteBatch)
     */
    private final Texture playerTexture;

    /**
     * The transient velocity of the {@link Character}
     *
     * @see #MOVEMENT_VELOCITY
     * @see #move()
     */
    private final Vector2 velocity = new Vector2();

    /**
     * The relationship between the {@link Area} and the {@link Body}, with standard area keys
     *
     * @see #activeBody
     * @see #switchCharacterContext(Area.Name)
     */
    private final Map<Area.Name, Body> bodies = new EnumMap<>(Area.Name.class);

    /**
     * The {@link Body} reference belonging to the current {@link Area} context
     *
     * @see #bodies
     * @see #switchCharacterContext(Area.Name)
     */
    private Body activeBody;

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
     *
     * @see #moveUp()
     * @see #moveDown()
     * @see #moveLeft()
     * @see #moveRight()
     */
    public void move() {
        if (velocity.x != 0 && velocity.y != 0) {
            // Correct faster movement when simultaneously traversing both axes
            velocity.x -= Math.signum(velocity.x) * MOVEMENT_VELOCITY_CORRECTION;
            velocity.y -= Math.signum(velocity.y) * MOVEMENT_VELOCITY_CORRECTION;
        }

        activeBody.setLinearVelocity(velocity);
        velocity.setZero();
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
     * Sets the position of the {@link Character}
     *
     * @param position The new position
     */
    public void setPosition(Vector2 position) {
        activeBody.setTransform(position, 0);
    }

    /**
     * Switch the {@link Character} context to {@link Area} identified by the given index
     *
     * @param areaName The {@link Area.Name} of the new {@link Area}
     */
    public void switchCharacterContext(Area.Name areaName) {
        activeBody = bodies.get(areaName);
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
     */
    public Character(Map<Area.Name, Area> areas, Area.Name defaultArea) {
        for (Map.Entry<Area.Name, Area> area : areas.entrySet())
            bodies.put(area.getKey(), area.getValue().registerCharacter(WIDTH, HEIGHT));

        switchCharacterContext(defaultArea);
        playerTexture = new Texture(TEXTURE_PATH);
    }
}
