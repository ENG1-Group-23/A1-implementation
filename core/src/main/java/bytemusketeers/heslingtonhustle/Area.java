package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Area} is a single room or outdoor areas in which the player can exist, containing its own {@link TiledMap}
 * texture and set of {@link Interactable} objects.
 */
class Area implements Drawable {
    public static final float MAP_SCALE = 0.04f;
    private final float mapWidth;
    private final float mapHeight;
    private final List<Interactable> interactables = new ArrayList<>();
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final TiledMap tiledMap;

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
     */
    public void triggerInteractables(Vector2 characterPosition) {
        for (Interactable interactable : interactables)
            if (interactable.isClose(characterPosition)) {
                interactable.interact();
            }
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
     * Releases all resources used by the {@link Area}
     */
    @Override
    public void dispose() {
        for (Interactable interactable : interactables)
            interactable.dispose();

        orthogonalTiledMapRenderer.dispose();
        tiledMap.dispose();
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
     */
    public Area(String map) {
        tiledMap = new TmxMapLoader().load(map);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, MAP_SCALE);

        MapProperties properties = tiledMap.getProperties();

        mapWidth = properties.get("tilewidth", Integer.class) * properties.get("width", Integer.class)
            * MAP_SCALE;
        mapHeight = properties.get("tileheight", Integer.class) * properties.get("height", Integer.class)
            * MAP_SCALE;
    }
}
