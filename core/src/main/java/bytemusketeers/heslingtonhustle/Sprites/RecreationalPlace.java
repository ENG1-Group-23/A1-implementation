package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import main.java.bytemusketeers.heslingtonhustle.Screens.PlayScreen;

/**
 * The {@link RecreationalPlace} class represents a place where the player can undertake recreational activities, extending
 * {@link InteractiveTileObject} and calling its constructor
 */
public class RecreationalPlace extends InteractiveTileObject{
    public RecreationalPlace(World world, TileMap map, Rectangle bounds) {
        super(world, map, bounds);
    }
}
