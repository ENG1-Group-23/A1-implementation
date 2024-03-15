package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * The {@link AreaFactory} provides a non-static factory for initialising particular {@link Area} objects, populated
 * with {@link Interactable} objects
 *
 * @see Area
 * @see Interactable
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
    public Area createTestMap() {
        Area area = new Area("Maps/test-map.tmx", new Vector2((float) Gdx.graphics.getWidth() / 2,
            (float) Gdx.graphics.getHeight() / 2).scl(1 / HeslingtonHustle.PPM));

        area.addInteractable(new Interactable(
            new Vector2(
                MathUtils.random(0, Gdx.graphics.getWidth()),
                MathUtils.random(0, Gdx.graphics.getHeight())).scl(1 / HeslingtonHustle.PPM),
            new Texture("prototype-1.png"),
            area, 0.5f, 0.5f,
            () -> metricManager.incrementMetric(MetricManager.Metric.Preparedness, 1)));

        area.addInteractable(new Interactable(
            new Vector2(
                MathUtils.random(0, Gdx.graphics.getWidth()),
                MathUtils.random(0, Gdx.graphics.getHeight())).scl(1 / HeslingtonHustle.PPM),
            new Texture("prototype-2.png"),
            area, 0.5f, 0.5f,
            () -> metricManager.decrementMetric(MetricManager.Metric.Preparedness, 1)));

        return area;
    }

    /**
     * Creates an {@link Area} of the Piazza building
     * TODO: currently incomplete
     *
     * @return The generated Piazza map
     */
    public Area createPiazzaMap() {
        return new Area("Maps/piazza-map.tmx", new Vector2(304, 32).scl(Area.MAP_SCALE));
    }

    /**
     * Creates an {@link Area} of the Ian Wand Computer Science building
     * TODO: currently incomplete
     *
     * @return The generated Computer Science building
     */
    public Area createCSMap() {
        return new Area("Maps/comp-sci-map.tmx", new Vector2(398, 16).scl(Area.MAP_SCALE));
    }

    /**
     * Creates an {@link Area} of the bedroom building
     * TODO: currently incomplete
     *
     * @return The generated bedroom building
     */
    public Area createBedroomMap() {
        return new Area("Maps/bedroom-map.tmx", new Vector2(160, 16).scl(Area.MAP_SCALE));
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
