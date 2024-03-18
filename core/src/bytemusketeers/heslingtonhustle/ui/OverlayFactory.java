package bytemusketeers.heslingtonhustle.ui;

import bytemusketeers.heslingtonhustle.metrics.MetricController;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The {@link OverlayFactory} constructs types of {@link Overlay} objects given with a persistent {@link SpriteBatch}
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
final public class OverlayFactory {
    /**
     * The {@link SpriteBatch} with which {@link Overlay} instances should be constructed
     */
    private final SpriteBatch batch;

    /**
     * Create a blank {@link PauseMenu} with the {@link #batch}
     *
     * @return A new {@link PauseMenu}
     * @see PauseMenu
     */
    public Overlay createPauseMenu() {
        return new PauseMenu(batch);
    }

    /**
     * Creates a heads-up display with the given left and right metric labels
     *
     * @param leftMetrics Metrics to display on the LHS of the HUD
     * @param rightMetrics Metrics to display on the RHS of the HUD
     * @return A new {@link HeadsUpDisplay}, prepared to display the given {@link MetricController.Metric} items
     */
    public Overlay createHUD(MetricController.Metric[] leftMetrics, MetricController.Metric[] rightMetrics) {
        return new HeadsUpDisplay(batch, leftMetrics, rightMetrics);
    }

    /**
     * Constructs a new {@link Overlay} with the given {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} with which {@link Overlay} instances should be bound
     */
    public OverlayFactory(SpriteBatch batch) {
        this.batch = batch;
    }
}
