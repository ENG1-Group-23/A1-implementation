package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

abstract public class Drawable {
    public Texture texture;
    public Vector2 position;
    public SpriteBatch spriteBatch;

    /*
    Renders the drawable on the screen

     */
    public void draw() {}

}
