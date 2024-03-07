package main.java.bytemusketeers.heslingtonhustle;

/**
 * An {@link Interactable} denotes a drawable object with which the
 * {@link main.java.bytemusketeers.heslingtonhustle.Sprites.Character} can interact.
 */
abstract public class Interactable extends Drawable {

    /**
     * Facilitates the interaction between items or objects and the player
     */
    abstract public void interact();
}
