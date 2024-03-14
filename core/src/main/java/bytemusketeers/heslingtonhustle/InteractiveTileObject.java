package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The {@link InteractiveTileObject} abstract class provides a functionality which could be inherited
 * to generate tiled objects
 */
public class InteractiveTileObject {
    protected Rectangle bounds;
    protected Body body;

    public InteractiveTileObject(World world, Rectangle bounds) {
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();

        // Creating new static body and setting its position
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) * Area.MAP_SCALE, (bounds.getY() + bounds.getHeight() / 2) * Area.MAP_SCALE);

        // Adding the body to the world
        body = world.createBody(bdef);

        // Creating a fixture, setting up is parameters and adding it to a body
        shape.setAsBox(bounds.getWidth() / 2 * Area.MAP_SCALE, bounds.getHeight() / 2 * Area.MAP_SCALE);
        body.createFixture(shape, 0.0f);
    }
}
