package main.java.bytemusketeers.heslingtonhustle.Screens;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;
import main.java.bytemusketeers.heslingtonhustle.Sprites.Border;
import main.java.bytemusketeers.heslingtonhustle.Sprites.Character;
import main.java.bytemusketeers.heslingtonhustle.Sprites.EatingPlace;
import main.java.bytemusketeers.heslingtonhustle.Sprites.RecreationalPlace;

import java.util.HashMap;

/**
 * The {@link PiazzaScreen} class represents a screen which is shown after the player moves his avatar to the Piazza building, extending {@link PlayScreen}
 * It initialises the world map and its contents, configure the world size and game camera
 */
public class PiazzaScreen extends PlayScreen {
    public PiazzaScreen(HeslingtonHustle game, String map){
        super(game, map);

        // Creating walls by iterating through all the objects which exist at the walls layer on the tiled map, which is under the index 1
        for (RectangleMapObject object : tileMap.getTiledMap().getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Border(world, tileMap, rect);
        }

        // Creating eating places by iterating through all the objects which exist at the eating-place layer on the tiled map, which is under the index 2
        for (RectangleMapObject object : tileMap.getTiledMap().getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new EatingPlace(world, tileMap, rect);
        }

        // Creating recreational places by iterating through all the objects which exist at the recreational-place layer on the tiled map, which is under the index 3
        for (RectangleMapObject object : tileMap.getTiledMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
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
