package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Area} is a single room or outdoor areas in which the player can exist, containing its own {@link TiledMap}
 * texture and set of {@link Interactable} objects.
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class Area implements Drawable {
    /**
     * Distinguish between instantiations of particular {@link Area} objects
     *
     * @see Area
     * @see AreaFactory
     */
    enum AreaName {
        TestMap("Test Map"),
        PiazzaBuilding("Piazza Building"),
        CompSciBuilding("Computer Science Building"),
        BedroomBuilding("University Accommodation");

        /**
         * The display name of the {@link AreaName}
         */
        private final String displayName;

        /**
         * Retrieves the display name of the {@link AreaName}
         *
         * @return The requested display name
         */
        @Override
        public String toString() {
            return displayName;
        }

        /**
         * Constructs a new {@link AreaName} enumerable entry with a suitable ordinal value and given display name
         *
         * @param displayName The {@link AreaName} display name
         */
        AreaName(String displayName) {
            this.displayName = displayName;
        }
    }

    /**
     * The list of {@link Interactable} objects existing in the {@link Area}
     *
     * @see Interactable
     */
    private final List<Interactable> interactables = new ArrayList<>();

    /**
     * The LibGDX world responsible for holding {@link com.badlogic.gdx.physics.box2d.BodyDef.BodyType#StaticBody}
     * and {@link com.badlogic.gdx.physics.box2d.BodyDef.BodyType#DynamicBody} collision zones, generally used for
     * {@link Interactable} and {@link TiledMap} border objects
     */
    private final World world;

    /**
     * The initial position of the player-controlled {@link Character} upon being spawned into the {@link Area}. This
     * position {@link Vector2} is specified in in-game metres.
     *
     * @see Character#setPosition(Vector2)
     * @see #getInitialCharacterPosition()
     */
    private final Vector2 initialCharacterPosition;

    /**
     * The {@link GameMap} forming the background of the {@link Area}, including its respective rendering mechanisms
     *
     * @see GameMap
     * @see GameMap#render(SpriteBatch)
     */
    private final GameMap map;

    /**
     * Adds an {@link Interactable} to the set of interactable tiles in the current {@link Area}.
     *
     * @param interactable The {@link Interactable} to add
     */
    public void addInteractable(Interactable interactable) {
        interactables.add(interactable);
    }

    /**
     * Given the current position of the {@link Character}, interact with any nearby {@link Interactable} objects
     *
     * @param characterPosition The position of the player-controlled {@link Character}
     * @see Interactable#interact()
     */
    public void triggerInteractables(Vector2 characterPosition) {
        for (Interactable interactable : interactables)
            if (interactable.isClose(characterPosition))
                interactable.interact();
    }

    /**
     * Bounds the given candidate vector along the given gutter boundaries and {@link com.badlogic.gdx.maps.Map} edges
     *
     * @param candidate The vector to be bounded
     * @param horizontalGutter The gutter, specified as in-game metres, on the horizontal axis
     * @param verticalGutter The gutter, specified as in-game metres, on the vertical axis
     * @return The bounded vector
     * @implNote For performance reasons, in particular its likely usage during a {@link Drawable#render(SpriteBatch)}
     *           cycle, this method modifies the original candidate {@link com.badlogic.gdx.math.Vector}. The bounded
     *           variant of the candidate is returned for the convenience of the caller.
     * @see GameMap#bound(Vector2, float, float)
     */
    public Vector2 bound(Vector2 candidate, final float horizontalGutter, final float verticalGutter) {
        // Bound the candidate vector on the given gutters
        if (candidate.x < horizontalGutter) candidate.x = horizontalGutter;
        if (candidate.y < verticalGutter)   candidate.y = verticalGutter;

        // Restrict the gutter-bounded vector along the map borders
        return map.bound(candidate, horizontalGutter, verticalGutter);
    }

    /**
     * Instructs the {@link GameMap} rendering object to update its viewing position with respect to the
     * {@link OrthographicCamera} game camera
     *
     * @param gameCam The {@link OrthographicCamera} game camera against which the {@link Area} viewport should be
     *                aligned
     */
    public void updateView(OrthographicCamera gameCam) {
        map.updateView(gameCam);
    }

    /**
     * Steps the {@link World} associated with the {@link Area}
     *
     * @see World#step(float, int, int)
     */
    public void step() {
        world.step(1/60f, 6, 2);
    }

    /**
     * Registers a new body with a standard collision box in the {@link World} corresponding to the {@link Area}
     *
     * @param initialPosition The initial position of the body, specified as in-game metre components
     * @param type The type of the body, according to {@link com.badlogic.gdx.physics.box2d.BodyDef.BodyType}
     * @param width The fixed width of the body, in in-game metres
     * @param height The fixed height of the body, in in-game metres
     * @return The newly registered body
     */
    public Body registerCollisionBody(Vector2 initialPosition, BodyDef.BodyType type, float width, float height) {
        BodyDef bodyDefinition = new BodyDef();

        bodyDefinition.position.set(initialPosition);
        bodyDefinition.type = type;

        Body body = world.createBody(bodyDefinition);

        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(width / 2, height / 2);
        body.createFixture(collisionBox,0.0f);
        collisionBox.dispose();

        return body;
    }

    /**
     * Registers a new {@link Character} of the given width and height to the current {@link Area}
     *
     * @param width The width of the character
     * @param height The height of the character
     * @return The newly registered character body
     * @see #registerCollisionBody(Vector2, BodyDef.BodyType, float, float)
     * @see Character
     */
    public Body registerCharacter(float width, float height) {
        return registerCollisionBody(initialCharacterPosition, BodyDef.BodyType.DynamicBody, width, height);
    }

    /**
     * Retrieves the initial {@link Area}-local {@link Character} position, in in-game metres
     *
     * @return The requested position vector
     * @see Character
     */
    public Vector2 getInitialCharacterPosition() {
        return initialCharacterPosition;
    }

    /**
     * Generate the static collision bodies for all given border objects, as defined by the {@link TiledMap}
     *
     * @param borderObjects The array of {@link RectangleMapObject}s represented the border vector
     */
    private void generateBorders(com.badlogic.gdx.utils.Array<RectangleMapObject> borderObjects) {
        if (borderObjects != null)
            for (RectangleMapObject borderObject : borderObjects) {
                Rectangle bounds = borderObject.getRectangle();

                // The TMX object coordinates are specified in pixels, so we have to scale the rectangle bounds to
                // in-game metres
                registerCollisionBody(
                    new Vector2(
                        map.scale(bounds.getX() + bounds.getWidth() / 2),
                        map.scale(bounds.getY() + bounds.getHeight() / 2)
                    ),
                    BodyDef.BodyType.StaticBody,
                    map.scale(bounds.getWidth()),
                    map.scale(bounds.getHeight())
                );
            }
    }

    /**
     * Releases all resources used by the {@link Area}
     */
    @Override
    public void dispose() {
        for (Interactable interactable : interactables)
            interactable.dispose();

        map.dispose();
        world.dispose();
    }

    /**
     * Registers the current {@link Area} onto the given game instance {@link SpriteBatch}, including all
     * {@link Interactable} objects
     *
     * @param batch The {@link SpriteBatch} to which the {@link Area} should be polled
     */
    @Override
    public void render(SpriteBatch batch) {
        map.render(batch);
        for (Interactable interactable : interactables)
            interactable.render(batch);
    }

    /**
     * Constructs a new {@link Area} with a {@link TiledMap} with a TMX file at the given path
     *
     * @param mapPath The path of the {@link Area}'s background texture
     * @param initialCharacterPosition The initial position of the {@link Character} on the {@link Area} map, specified
     *                                 by in-game metre components
     * @see Character
     */
    public Area(String mapPath, Vector2 initialCharacterPosition) throws InvalidAreaException {
        map = new GameMap(mapPath);
        world = new World(new Vector2(0, 0), true);
        generateBorders(map.getBorderObjects());
        this.initialCharacterPosition = initialCharacterPosition;
    }
}
