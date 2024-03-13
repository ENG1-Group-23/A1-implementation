package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * An {@link Interactable} is an {@link Item} with which the {@link Character} may interact
 */
class Interactable extends Item {
    private boolean isHidden = false;

    /**
     * Indicate that the {@link Character} has interacted with the {@link Item}
     */
    public void interact() {
        toggleHide();
    }

    /**
     * Toggle the visibility of the {@link Interactable}
     */
    private void toggleHide() {
        this.isHidden = !this.isHidden;
    }

    /**
     * Determine the visibility of the {@link Interactable}
     *
     * @return Is the {@link Interactable} hidden?
     */
    public boolean isHidden() {
        return this.isHidden;
    }

    /**
     * Instantiates a new {@link Interactable} {@link Item} with the given initial parameters
     *
     * @param position The initial position of the {@link Interactable}
     * @param texture The initial {@link Sprite} {@link Texture}
     * @param world The {@link World} into which the {@link Interactable} should be drawn
     * @param width The initial width
     * @param height The initial height
     */
    public Interactable(Vector2 position, Texture texture, World world, float width, float height) {
        super(position, texture, world, width, height);
    }
}
