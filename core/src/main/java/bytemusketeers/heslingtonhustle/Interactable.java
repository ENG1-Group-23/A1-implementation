package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * An {@link Interactable} is an {@link Item} with which the {@link Character} may interact
 */
class Interactable extends Item {
    /**
     * The square of the maximum interaction distance; that is, the maximum distance, measured by the Euclidean metric,
     * that a {@link Character} may be from an {@link Interactable} to fire an {@link #interact()}.
     */
    private static final float INTERACTION_DISTANCE_SQ = 0.3f;
    private final Runnable action;

    /**
     * Indicate that the {@link Character} has interacted with the {@link Item}
     */
    public void interact() {
        if (action != null)
            action.run();
    }

    /**
     * Determines if the described {@link Vector2} represents an object that is sufficiently close to the
     * {@link Interactable}; see {@link #INTERACTION_DISTANCE_SQ}.
     *
     * @param position The position to test
     * @return Is the given {@link Vector2} within the defined Euclidean distance of the {@link Interactable}?
     */
    public boolean isClose(Vector2 position) {
        return getPosition().dst2(position) <= INTERACTION_DISTANCE_SQ;
    }

    /**
     * If applicable, renders the {@link Interactable} as an {@link Item}, otherwise do nothing
     *
     * @param batch The {@link SpriteBatch} to which the {@link Item} should be polled
     */
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    /**
     * Instantiates a new {@link Interactable} {@link Item} with the given initial parameters
     *
     * @param position The initial position of the {@link Interactable}
     * @param texture The initial {@link Sprite} {@link Texture}
     * @param area The {@link Area} into which the {@link Interactable} should be drawn
     * @param width The initial width
     * @param height The initial height
     * @param action The {@link Runnable} method to execute upon interaction
     */
    public Interactable(Vector2 position, Texture texture, Area area, float width, float height, Runnable action) {
        super(position, texture, area, width, height);
        this.action = action;
    }
}
