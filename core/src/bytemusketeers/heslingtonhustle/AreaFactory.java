package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * The {@link AreaFactory} provides a non-static factory for initialising particular {@link Area} objects, populated
 * with {@link Interactable} objects
 *
 * @see Area
 * @see Interactable
 * @implNote Any future programmer wishing to add {@link Area} initialisers should note that all coordinate vectors are
 *           specified by components described in in-game metres, not pixels.
 * @apiNote Every initialiser contained herein may throw an {@link InvalidAreaException} in event of the requested TMX
 *          tile-map file being externally corrupt. Users of the {@link AreaFactory} should deal with such cases
 *          gracefully, as continuing with an uninitialised area will invoke undefined GPU behaviour from LibGDX during
 *          the {@link Drawable#render(SpriteBatch)} cycle.
 */
final class AreaFactory {
    /**
     * The {@link MetricManager} belonging to the parental {@link PlayScreen}, generally used when registering
     * {@link Runnable} actions on {@link Interactable}s in the created {@link Area}
     *
     * @see Interactable#interact()
     */
    private final MetricManager metricManager;

    /**
     * Creates an {@link Area} for testing purposes with a couple of {@link Interactable} objects at randomly generated
     * locations, incrementing and decrementing the
     * {@link MetricManager.Metric#Preparedness} metric.
     *
     * @return The generated test map
     */
    public Area createTestMap() throws InvalidAreaException {
        Area area = new Area("Maps/test-map.tmx", new Vector2(2, 2));

        area.addInteractable(new Interactable(
            new Vector2(0, 2),
            new Texture("prototype-1.png"),
            area, 1, 1,
            () -> metricManager.incrementMetric(MetricManager.Metric.Preparedness, 1)));

        area.addInteractable(new Interactable(
            new Vector2(8, 2),
            new Texture("prototype-2.png"),
            area, 1, 1,
            () -> metricManager.decrementMetric(MetricManager.Metric.Preparedness, 1)));

        return area;
    }

    /**
     * Creates an {@link Area} of the Piazza building
     * TODO: currently incomplete
     *
     * @return The generated Piazza map
     */
    public Area createPiazzaMap() throws InvalidAreaException {
        return new Area("Maps/piazza-map.tmx", new Vector2(19, 1.4f));
    }

    /**
     * Creates an {@link Area} of the Ian Wand Computer Science building
     * TODO: currently incomplete
     *
     * @return The generated Computer Science building
     */
    public Area createCSMap() throws InvalidAreaException {
        return new Area("Maps/comp-sci-map.tmx", new Vector2(25, 1));
    }

    /**
     * Creates an {@link Area} of the bedroom building
     * TODO: currently incomplete
     *
     * @return The generated bedroom building
     */
    public Area createBedroomMap() throws InvalidAreaException {
        return new Area("Maps/bedroom-map.tmx", new Vector2(10, 1));
    }

    /**
     * Instantiates a new {@link AreaFactory} with the given contextual {@link MetricManager}
     *
     * @param metricManager The {@link MetricManager} belonging to the parental {@link PlayScreen}
     */
    public AreaFactory(MetricManager metricManager) {
        this.metricManager = metricManager;
    }
}
