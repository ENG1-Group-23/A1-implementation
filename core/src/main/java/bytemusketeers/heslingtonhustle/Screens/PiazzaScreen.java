package main.java.bytemusketeers.heslingtonhustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;
import main.java.bytemusketeers.heslingtonhustle.Interactable;
import main.java.bytemusketeers.heslingtonhustle.Item;
import main.java.bytemusketeers.heslingtonhustle.Player.Metrics;
import main.java.bytemusketeers.heslingtonhustle.Sprites.*;
import main.java.bytemusketeers.heslingtonhustle.Sprites.Character;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link PiazzaScreen} class represents a screen which is shown after the player moves his avatar to the Piazza building, extending {@link PlayScreen}
 * It initialises the world map and its contents, configure the world size and game camera
 */
public class PiazzaScreen extends PlayScreen {
    public PiazzaScreen(HeslingtonHustle game, String map){
        super(game, map);

        // Creating walls by iterating through all the objects which exist at the walls layer on the tiled map, which is under the index 1
        for (MapObject object : tileMap.getTiledMap().getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Border(world, tileMap, rect);
        }

        // Creating eating places by iterating through all the objects which exist at the eating-place layer on the tiled map, which is under the index 2
        for (MapObject object : tileMap.getTiledMap().getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new EatingPlace(world, tileMap, rect);
        }

        // Creating recreational places by iterating through all the objects which exist at the recreational-place layer on the tiled map, which is under the index 3
        for (MapObject object : tileMap.getTiledMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new RecreationalPlace(world, tileMap, rect);
        }
    }

    @Override
    public void show() throws RuntimeException {
        // Set gameCam position
        gameCam.position.set(gamePort.getWorldWidth() / 2, HeslingtonHustle.HEIGHT_METRES_BOUND, 0);

        // Creating the character
        HashMap<String, Float> characterPos = new HashMap<>();
        characterPos.put("x", 304f * this.tileMap.getScale());
        characterPos.put("y", 32f * this.tileMap.getScale());
        character = new Character(world, characterPos);
    }
}
