package bytemusketeers.heslingtonhustle.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link GameMap} represents and manages the {@link Drawable} playing {@link Area} background, principally consisting
 * of the {@link TiledMap}.
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class GameMap implements Drawable {
    /**
     * The fixed width and height of all tiles, in pixels
     */
    public static final int TILE_AXIS_LENGTH = 16;

    /**
     * The {@link TiledMap} representing the loaded tilemap TMX file
     *
     * @see TmxMapLoader#load(String)
     */
    private final TiledMap tiledMap;

    /**
     * The LibGDX rendering object responsible for the rendering of the {@link TiledMap}
     *
     * @see TiledMap
     */
    private final OrthogonalTiledMapRenderer renderer;

    /**
     * The width of the {@link GameMap}, in in-game metres
     */
    private final int width;

    /**
     * The height of the {@link GameMap}, in in-game metres
     */
    private final int height;

    /**
     * Retrieve the "borders" layer objects from the {@link TiledMap} of the current {@link GameMap}
     *
     * @return The {@link RectangleMapObject} objects embedded in the requested layer
     */
    com.badlogic.gdx.utils.Array<RectangleMapObject> getBorderObjects()  {
        final MapLayers layers = tiledMap.getLayers();
        com.badlogic.gdx.utils.Array<RectangleMapObject> objects = new Array<>();

        for (MapLayer layer : layers)
            //This triggers if the property is there at all, not just if it's true
            //If you want to make a layer non-collidable, remove the custom property entirely from the tmx file.
            if (layer.getProperties().containsKey("collidable"))
                 objects.addAll(layer.getObjects().getByType(RectangleMapObject.class));

        return objects;
    }
    /**
     * Retrieve any layer objects from the {@link TiledMap} of the current {@link GameMap}
     *
     * @param nameOfTheLayer the name of the layer where objects are
     * @return The {@link RectangleMapObject} objects embedded in the requested layer
     */
    public com.badlogic.gdx.utils.Array<RectangleMapObject> getLayerObjects(String nameOfTheLayer) throws InvalidAreaException {
        MapLayers layers = tiledMap.getLayers();

        try {
            return layers.get(nameOfTheLayer).getObjects().getByType(RectangleMapObject.class);
        }
        catch (Exception e) {
            throw new InvalidAreaException("Map does not contain layer " + nameOfTheLayer);
        }

    }

    /**
     * Scales the given value, specified in pixels, to in-game metres
     *
     * @param value The quantity to scale, in pixels
     * @return The scaled quantity, in in-game metres
     */
    float scale(float value) {
        return value / TILE_AXIS_LENGTH;
    }

    /**
     * Bounds the given candidate vector along the determined {@link #tiledMap} boundaries
     *
     * @param candidate The vector to be bounded
     * @param horizontalGutter The gutter, specified as in-game metres, on the horizontal axis
     * @param verticalGutter The gutter, specified as in-game metres, on the vertical axis
     * @return The bounded vector
     * @implNote For performance and practical reasons, this method modifies the given candidate vector, since it will
     *           be typically invoked as the second stage in a bounding process in which the caller has already created
     *           a copy of the original candidate. The original---potentially modified---{@link Vector2} is returned for
     *           convenience.
     * @see Area#bound(Vector2, float, float)
     */
    Vector2 bound(Vector2 candidate, final float horizontalGutter, final float verticalGutter) {
        final float rightBound = width - horizontalGutter;
        final float topBound = height - verticalGutter;

        if (candidate.x > rightBound) candidate.x = rightBound;
        if (candidate.y > topBound)   candidate.y = topBound;

        return candidate;
    }

    /**
     * Instructs the {@link OrthogonalTiledMapRenderer} to update its viewing position with respect to the
     * {@link OrthographicCamera} game camera
     *
     * @param gameCam The {@link OrthographicCamera} game camera against which the {@link Area} viewport should be
     *                aligned
     */
    void updateView(OrthographicCamera gameCam) {
        renderer.setView(gameCam);
    }

    /**
     * Releases all resources used by the {@link GameMap}
     */
    @Override
    public void dispose() {
        renderer.dispose();
        tiledMap.dispose();
    }

    /**
     * Renders the entire {@link GameMap}
     *
     * @param batch Target of the rendering operation
     * @implNote The {@link SpriteBatch} is not currently used as a target of the {@link OrthogonalTiledMapRenderer}
     */
    @Override
    public void render(SpriteBatch batch) {
        renderer.render();
    }

    /**
     * Creates a new {@link GameMap} with the given map path
     *
     * @param mapPath The path of the map
     * @throws InvalidAreaException The map was invalid could not be loaded into the area
     * @apiNote The provided map path should correspond to a TMX Tiled file for use with {@link TmxMapLoader}
     */
    GameMap(String mapPath) throws InvalidAreaException {
        tiledMap = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1f / TILE_AXIS_LENGTH);

        final MapProperties properties = tiledMap.getProperties();

        width = properties.get("width", Integer.class);
        height = properties.get("height", Integer.class);

        // Check that the tile-map uses tiles of the expected dimensions
        if (properties.get("tilewidth", Integer.class) != TILE_AXIS_LENGTH ||
                properties.get("tileheight", Integer.class) != TILE_AXIS_LENGTH)
            throw new InvalidAreaException("Tiles are of invalid pixel-dimensions");
    }
}
