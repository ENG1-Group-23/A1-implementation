package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The {@link InteractiveTileObject} abstract class provides a functionality which could be inherited
 * to generate tiled objects
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected Rectangle bounds;
    protected Body body;

    public InteractiveTileObject(World world, Rectangle bounds, float mapScale) {
        this.world = world;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();

        // Creating new static body and setting its position
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) * mapScale, (bounds.getY() + bounds.getHeight() / 2) * mapScale);

        // Adding the body to the world
        body = world.createBody(bdef);

        // Creating a fixture, setting up is parameters and adding it to a body
        shape.setAsBox(bounds.getWidth() / 2 * mapScale, bounds.getHeight() / 2 * mapScale);
        body.createFixture(shape, 0.0f);
    }
}
