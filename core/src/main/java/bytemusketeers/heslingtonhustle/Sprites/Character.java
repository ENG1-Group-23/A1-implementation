package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;

import java.util.HashMap;

/**
 * The {@link Character} class represents the avatar of the player in the game, extending the {@link Sprite}.
 */
public class Character extends Sprite {
    public World world;
    public Body b2body;
    public Texture playerTexture;
    public static final float WIDTH = 0.3f;
    public static final float HEIGHT = 0.3f;
    public Character(World world, Vector2 position){
        this.world = world;
        defineCharacter(position);
    }

    /**
     * Defines the new {@link Character} and sets its configuration
     */
    public void defineCharacter(Vector2 position){
        BodyDef bdef = new BodyDef();
        // Set position for the character
        bdef.position.set(position.x, position.y);
        // Set the type of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        // Create a body in the game world
        b2body = world.createBody(bdef);
        // Create a fixture for the body and setting its shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / HeslingtonHustle.PPM, 15 / HeslingtonHustle.PPM);
        b2body.createFixture(shape, 0.0f);
        shape.dispose();

        playerTexture = new Texture("prototype-4.png");
    }
}
