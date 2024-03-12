package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
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
    protected TiledMap tiledMap;
    protected Rectangle bounds;
    protected Body body;

    public InteractiveTileObject(World world, TileMap map, Rectangle bounds) {
        this.tiledMap = map.getTiledMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();

        // Creating new static body and setting its position
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) * TileMap.SCALE, (bounds.getY() + bounds.getHeight() / 2) * TileMap.SCALE);

        // Adding the body to the world
        body = world.createBody(bdef);

        // Creating a fixture, setting up is parameters and adding it to a body
        shape.setAsBox(bounds.getWidth() / 2 * TileMap.SCALE, bounds.getHeight() / 2 * TileMap.SCALE);
        body.createFixture(shape, 0.0f);
    }
}