package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The {@link EatingPlace} class represents a place where the player can eat, extending
 * {@link InteractiveTileObject} and calling its constructor
 */
public class EatingPlace extends InteractiveTileObject{
    public EatingPlace(World world, TileMap map, Rectangle bounds) {
        super(world, map, bounds);
    }
}
