package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * An {@link Item} denotes a drawable object with which the
 * {@link main.java.bytemusketeers.heslingtonhustle.Sprites.Character} can interact.
 */
//Item is not abstract for future implementation as an item could be a "Noninteractable
public class Item extends Sprite {

    protected Texture texture;
    protected Vector2 position;

    /**
     * Facilitates the interaction between items or objects and the player
     */
    public void interact() {};

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2 getPosition() {
        return new Vector2(this.getX(), this.getY());
    }

}
