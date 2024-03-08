package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;

/**
 * The {@code Character} class represents the avatar of the player in the game, extending {@link Sprite} class
 * It configures the character settings
 */
public class Character extends Sprite {
    public World world;
    public Body b2body;
    public Texture playerTexture;
    public SpriteBatch spriteBatch;

    public Character(World world){
        this.world = world;
        defineCharacter();
    }

    /**
     * Defines the new {@code Character} and sets its configuration
     */
    public void defineCharacter(){
        BodyDef bdef = new BodyDef();
        // Set position for the character
        bdef.position.set(HeslingtonHustle.W_WIDTH / 2 / HeslingtonHustle.PPM, HeslingtonHustle.W_HEIGHT / 2 / HeslingtonHustle.PPM);
        // Set the type of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        // Create a body in the game world
        b2body = world.createBody(bdef);
        // Create a fixture for the body and setting its shape
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / HeslingtonHustle.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef);

        spriteBatch = new SpriteBatch();
        playerTexture = new Texture("prototype-4.png");
    }
}
