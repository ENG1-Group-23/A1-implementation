package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
 */
class Area implements Drawable {
    enum AreaName {
        TestMap, PiazzaBuilding
    }
    public static final float MAP_SCALE = 0.04f;
    private final float mapWidth;
    private final float mapHeight;
    private final List<Interactable> interactables = new ArrayList<>();
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final TiledMap tiledMap;
    private final World world;
    private final Vector2 initialCharacterPosition;

    /**
     * Adds an {@link Interactable} to the set of interactable tiles in the current {@link Area}.
     *
     * @param interactable The {@link Interactable} to add
     */
    public void addInteractable(Interactable interactable) {
        interactables.add(interactable);
    }

    /**
     * Removes an {@link Interactable}.
     *
     * @param interactable The {@link Interactable} to remove
     */
    public void removeInteractable(Interactable interactable) {
        interactables.remove(interactable);
        interactable.dispose();
    }

    /**
     * Given the current position of the {@link Character}, interact with any nearby {@link Interactable} objects
     *
     * @param characterPosition The position of the player-controlled {@link Character}
     * @return Were any interactions triggered?
     * @see Interactable#interact()
     */
    public boolean triggerInteractables(Vector2 characterPosition) {
        boolean interacted = false;

        for (Interactable interactable : interactables)
            if (interactable.isClose(characterPosition)) {
                interactable.interact();
                interacted = true;
            }

        return interacted;
    }

    /**
     * Instructs the {@link OrthogonalTiledMapRenderer} to update its viewing position with respect to the
     * {@link OrthographicCamera} game camera
     *
     * @param gameCam The {@link OrthographicCamera} game camera against which the {@link Area} viewport should be
     *                aligned
     */
    public void updateView(OrthographicCamera gameCam) {
        orthogonalTiledMapRenderer.setView(gameCam);
    }

    /**
     * Retrieves the width of the {@link Area} map, in metres
     *
     * @return The metre-width of the map
     */
    public float getMapWidth() {
        return mapWidth;
    }

    /**
     * Retrieves the height of the {@link Area} map, in metres
     *
     * @return The metre-height of the map
     */
    public float getMapHeight() {
        return mapHeight;
    }

    /**
     * Gets this instance of area's world
     *
     * @return The world of this area
     */
    public World getWorld() { return world; }

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
     * @param initialPosition The initial position of the body
     * @param type The type of the body, according to {@link com.badlogic.gdx.physics.box2d.BodyDef.BodyType}
     * @param width The fixed width of the body
     * @param height The fixed height of the body
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
     * Retrieves the initial {@link Character} position, as required by the {@link Area}
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
        for (RectangleMapObject borderObject : borderObjects) {
            Rectangle bounds = borderObject.getRectangle();
            registerCollisionBody(
                new Vector2((bounds.getX() + bounds.getWidth() / 2), (bounds.getY() + bounds.getHeight() / 2))
                    .scl(Area.MAP_SCALE), BodyDef.BodyType.StaticBody,
                bounds.getWidth() * Area.MAP_SCALE,
                bounds.getHeight() * Area.MAP_SCALE
            );
        }
    }

    /**
     *
     */
    public com.badlogic.gdx.utils.Array<RectangleMapObject> getLayerObjects (String nameOfTheLayer) {
        MapLayers layers = tiledMap.getLayers();

        return layers.get(nameOfTheLayer).getObjects().getByType(RectangleMapObject.class);
    }

    /**
     * Releases all resources used by the {@link Area}
     */
    @Override
    public void dispose() {
        for (Interactable interactable : interactables)
            interactable.dispose();

        orthogonalTiledMapRenderer.dispose();
        tiledMap.dispose();
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
        orthogonalTiledMapRenderer.render();

        for (Interactable interactable : interactables)
            interactable.render(batch);
    }

    /**
     * Constructs a new {@link Area} with a {@link TiledMap} with a TMX file at the given path
     *
     * @param map The path of the {@link Area}'s background texture
     * @param initialCharacterPosition The initial position of the {@link Character} on the {@link Area} map
     * @see Character
     */
    public Area(String map, Vector2 initialCharacterPosition) {
        this.initialCharacterPosition = initialCharacterPosition;

        tiledMap = new TmxMapLoader().load(map);
        world = new World(new Vector2(0, 0), true);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, MAP_SCALE);

        MapProperties properties = tiledMap.getProperties();
        MapLayers layers = tiledMap.getLayers();

        mapWidth = properties.get("tilewidth", Integer.class) * properties.get("width", Integer.class)
            * MAP_SCALE;
        mapHeight = properties.get("tileheight", Integer.class) * properties.get("height", Integer.class)
            * MAP_SCALE;

        for (MapLayer layer : layers)
            generateBorders(layer.getObjects().getByType(RectangleMapObject.class));
    }
}
