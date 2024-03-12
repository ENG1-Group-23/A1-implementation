package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import main.java.bytemusketeers.heslingtonhustle.Sprites.InteractiveTileObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Area} is a single room or outdoor areas in which the player can exist, containing its own {@link TiledMap}
 * texture and set of {@link InteractiveTileObject} objects.
 */
public class Area {
    public static final float MAP_SCALE = 0.04f;
    private final TiledMap tiledMap;
    public final float mapWidth;
    public final float mapHeight;
    private final List<InteractiveTileObject> interactiveTiles = new ArrayList<>();
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    /**
     * Adds an {@link InteractiveTileObject} to the set of interactable tiles in the current {@link Area}.
     *
     * @param interactable The {@link InteractiveTileObject} to add
     */
    public void addInteractable(InteractiveTileObject interactable) {
        interactiveTiles.add(interactable);
    }

    /**
     * Renders the entire area and all interactable objects and tiles therein
     */
    public void render() {
        orthogonalTiledMapRenderer.render();
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
     * Constructs a new {@link Area} with a {@link TiledMap} with a TMX file at the given path
     *
     * @param map The path of the {@link Area}'s background texture
     */
    public Area(String map) {
        tiledMap = new TmxMapLoader().load(map);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, MAP_SCALE);

        MapProperties properties = tiledMap.getProperties();

        mapWidth = properties.get("tilewidth", Integer.class) * properties.get("width", Integer.class) * MAP_SCALE;
        mapHeight = properties.get("tileheight", Integer.class) * properties.get("height", Integer.class) * MAP_SCALE;
    }
}
