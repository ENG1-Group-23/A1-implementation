package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * An {@link Interactable} is an {@link Item} with which the {@link Character} may interact.
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class Interactable extends Item {
    /**
     * The square of the maximum interaction distance; that is, the maximum distance, measured by the Euclidean metric,
     * that a {@link Character} may be from an {@link Interactable} to fire an {@link #interact()}.
     * TODO: this should depend on the size of the {@link Item}
     *
     * @see #isClose(Vector2)
     */
    private static final float INTERACTION_DISTANCE_SQ = 1f;

    /**
     * The action to execute after {@link #interact()} has been finished. The execution should be deferred to LibGDX to
     * avoid potential threading-related race conditions.
     *
     * @see Runnable#run()
     * @see com.badlogic.gdx.Application#postRunnable(Runnable)
     * @see #interact()
     */
    private final Runnable action;

    /**
     * Indicate that the {@link Character} has interacted with the {@link Item}
     *
     * @see #action
     */
    public void interact() {
        if (action != null)
            Gdx.app.postRunnable(action);
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
     * Instantiates a new {@link Interactable} {@link Item} with the given initial parameters
     *
     * @param position The initial position of the {@link Interactable}, specified as in-game metre components
     * @param texture The initial {@link Sprite} {@link Texture}
     * @param area The {@link Area} into which the {@link Interactable} should be drawn
     * @param width The initial width, in in-game metres
     * @param height The initial height, in in-game metres
     * @param action The {@link Runnable} method to execute upon interaction
     */
    public Interactable(Vector2 position, Texture texture, Area area, float width, float height, Runnable action) {
        super(position, texture, area, width, height);
        this.action = action;
    }
}
