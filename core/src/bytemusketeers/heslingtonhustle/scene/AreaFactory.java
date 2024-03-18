package bytemusketeers.heslingtonhustle.scene;

import bytemusketeers.heslingtonhustle.PlayScreen;
import bytemusketeers.heslingtonhustle.metrics.MetricController;
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
 * @author ENG1 Team 23 (Cohort 3)
 */
final public class AreaFactory {
    /**
     * The {@link MetricController} belonging to the parental {@link PlayScreen}, generally used when registering
     * {@link Runnable} actions on {@link Interactable}s in the created {@link Area}
     */
    private final MetricController metricController;

    /**
     * The {@link PlayScreen} on which the {@link Area} objects generated by the {@link AreaFactory} should be rendered.
     *
     * @implNote This is required to correctly register the {@link Interactable} update actions
     * @see Interactable#interact()
     */
    private final PlayScreen playScreen;

    /**
     * Crates an {@link Area} of the Campus East outdoors area
     *
     * @return The generated test map
     */
    public Area createOutdoorMap() throws InvalidAreaException {
        Area area = new Area("Maps/outdoor-map.tmx", new Vector2(2, 2));

        // The Piazza building entrance
        // TODO: change sprite, make building-specific
        area.addInteractable(new Interactable(
            new Vector2(4, 7),
            new Texture("Building-1.png"),
            area, 2,
            () -> playScreen.switchArea(Area.Name.PiazzaBuilding)
        ));

        // The university accommodation/bedroom building entrance
        // TODO: change sprite, make building-specific
        area.addInteractable(new Interactable(
            new Vector2(23, 11),
            new Texture("Building-1.png"),
            area, 2,
            () -> playScreen.switchArea(Area.Name.BedroomBuilding)
        ));

        // The computer science building entrance
        // TODO: change sprite, make building-specific
        area.addInteractable(new Interactable(
            new Vector2(12, 17),
            new Texture("Building-1.png"),
            area, 2,
            () -> playScreen.switchArea(Area.Name.CompSciBuilding)
        ));

        return area;
    }

    /**
     * Creates an {@link Area} of the Piazza building
     *
     * @return The generated Piazza map
     */
    public Area createPiazzaMap() throws InvalidAreaException {
        Area area = new Area("Maps/piazza-map.tmx", new Vector2(19, 3));

        // Study opportunity (south wing)
        area.addInteractable(new Interactable(
            new Vector2(25.5f, 7.5f),
            new Texture("Paper.png"),
            area, 4,
            () -> metricController.incrementPlayerMetric(MetricController.Metric.Study, 1)
        ));

        // Meal opportunity #1 (east wing)
        area.addInteractable(new Interactable(
            new Vector2(17.5f, 15.5f),
            new Texture("Food-Plate.png"),
            area, 4,
            () -> metricController.incrementPlayerMetric(MetricController.Metric.Eat, 1)
        ));

        // Recreational opportunity (west wing)
        area.addInteractable(new Interactable(
            new Vector2(5, 15.5f),
            new Texture("Alcohol-1.png"),
            area, 4, () -> {
                metricController.incrementPlayerMetric(MetricController.Metric.Play, 1);
                metricController.decrementPlayerMetric(MetricController.Metric.Study, 1);
            }
        ));

        // Return to the outside
        // TODO: change sprite to exit door
        area.addInteractable(new Interactable(
            new Vector2(19, 1),
            new Texture("Building-1.png"),
            area, 2,
            () -> playScreen.switchArea(Area.Name.OutdoorMap)
        ));

        return area;
    }

    /**
     * Creates an {@link Area} of the Ian Wand Computer Science building
     *
     * @return The generated Computer Science building
     */
    public Area createCSMap() throws InvalidAreaException {
        Area area = new Area("Maps/comp-sci-map.tmx", new Vector2(25, 3));

        // Study opportunity (east wing)
        area.addInteractable(new Interactable(
            new Vector2(45, 11),
            new Texture("Paper.png"),
            area, 4,
            () -> metricController.incrementPlayerMetric(MetricController.Metric.Study, 1)
        ));

        // Study opportunity (east wing)
        area.addInteractable(new Interactable(
            new Vector2(42, 15.5f),
            new Texture("Paper.png"),
            area, 4,
            () -> metricController.incrementPlayerMetric(MetricController.Metric.Study, 1)
        ));

        // Meal opportunity (west wing)
        area.addInteractable(new Interactable(
            new Vector2(7, 15.5f),
            new Texture("Food-Plate.png"),
            area, 4,
            () -> metricController.incrementPlayerMetric(MetricController.Metric.Eat, 1)
        ));

        // Return to the outside
        // TODO: change sprite to exit door
        area.addInteractable(new Interactable(
            new Vector2(25, 1),
            new Texture("Building-1.png"),
            area, 2,
            () -> playScreen.switchArea(Area.Name.OutdoorMap)
        ));

        return area;
    }

    /**
     * Creates an {@link Area} of the bedroom building
     *
     * @return The generated bedroom building
     */
    public Area createBedroomMap() throws InvalidAreaException {
        Area area = new Area("Maps/bedroom-map.tmx", new Vector2(6, 3));

        // A bed for advancing the day
        area.addInteractable(new Interactable(
            new Vector2(6, 7),
            new Texture("Bed-1.png"),
            area, 4.5f,
            playScreen::advanceDay
        ));

        // Recreational opportunity
        area.addInteractable(new Interactable(
            new Vector2(2, 3),
            new Texture("Alcohol-1.png"),
            area, 4, () -> {
            metricController.incrementPlayerMetric(MetricController.Metric.Play, 1);
            metricController.decrementPlayerMetric(MetricController.Metric.Study, 1);
        }
        ));

        // Return to the outside
        // TODO: change sprite to exit door
        area.addInteractable(new Interactable(
            new Vector2(6, 1),
            new Texture("Building-1.png"),
            area, 2,
            () -> playScreen.switchArea(Area.Name.OutdoorMap)
        ));

        return area;
    }

    /**
     * Instantiates a new {@link AreaFactory} with the given contextual {@link MetricController}
     *
     * @param metricController The {@link MetricController} belonging to the parental {@link com.badlogic.gdx.Screen}
     */
    public AreaFactory(MetricController metricController, PlayScreen playScreen) {
        this.metricController = metricController;
        this.playScreen = playScreen;
    }
}
