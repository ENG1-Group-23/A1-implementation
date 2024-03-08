package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * An {@link Item} denotes a drawable object with which the
 * {@link main.java.bytemusketeers.heslingtonhustle.Sprites.Character} can interact.
 */
//Item is not abstract for future implementation as an item could be a "Noninteractable
public class Item extends Sprite {

    protected Texture texture;
    protected Vector2 position;
    protected float widthInMetres, heightInMetres;
    protected Body body;
    protected World world;
    /**
     * Facilitates the interaction between items or objects and the player
     */
    public void interact() {};

    public void defineBody(Vector2 position){
        BodyDef bodyDef = new BodyDef();
        // Set position for the collision box
        bodyDef.position.set(position.x + (float) texture.getWidth() / 2 / HeslingtonHustle.PPM, position.y + (float) texture.getHeight() / 2 / HeslingtonHustle.PPM);
        // Set the type of the body
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Create a body in the game world
        body = world.createBody(bodyDef);
        // Create a fixture for the body and setting its shape
        PolygonShape collisionBox = new PolygonShape();
        collisionBox.setAsBox(widthInMetres / 2,heightInMetres / 2); // Creates a rectangle shaped box around shape
        body.createFixture(collisionBox,0.0f);
        collisionBox.dispose();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2 getPosition() {
        return new Vector2(this.getX(), this.getY());
    }

    public float getWidth(){ return widthInMetres; }
    public float getHeight(){ return heightInMetres; }

}
