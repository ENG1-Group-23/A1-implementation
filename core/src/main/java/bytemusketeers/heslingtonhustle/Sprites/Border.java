package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The {@link Border} class represents a border of the active screen, extending
 * {@link InteractiveTileObject} and calling its constructor
 */
public class Border extends InteractiveTileObject{
    public Border(World world, TileMap map, Rectangle bounds) {
        super(world, map, bounds);
    }
}
