package bytemusketeers.heslingtonhustle.ui;

import bytemusketeers.heslingtonhustle.metrics.MetricController;
import bytemusketeers.heslingtonhustle.metrics.MetricListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link HeadsUpDisplay} displays persistent player and game information throughout the gameplay.
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class HeadsUpDisplay extends Overlay implements MetricListener {
    /**
     * A persistent mapping between {@link MetricController.Metric} elements and the {@link Overlay}-specific
     * {@link Label} form elements
     *
     * @see MetricController
     */
    private final Map<MetricController.Metric, Label> metricLabels = new EnumMap<>(MetricController.Metric.class);

    /**
     * The {@link Label.LabelStyle} to use for all text; this is standard and shipped with LibGDX
     */
    private static final Label.LabelStyle LABEL_STYLE = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    /**
     * Update the {@link MetricController.Metric} label with the given value
     *
     * @param metric The key of the {@link MetricController.Metric} to update
     * @param text The new text
     */
    @Override
    public void updateMetricText(MetricController.Metric metric, String text) {
        Label valueLabel = metricLabels.get(metric);

        if (valueLabel != null)
            valueLabel.setText(text);
    }

    /**
     * Prepares and adds a name-value {@link Label} pair to the given {@link Table}, and ends the row
     *
     * @param metric The {@link MetricController.Metric} associated with the {@link Label}
     */
    private void addLabel(MetricController.Metric metric, Table table) {
        final Label valueLabel = new Label(null, LABEL_STYLE);
        metricLabels.put(metric, valueLabel);
        table.add(new Label(metric.toString() + ":", LABEL_STYLE)).padRight(GENERAL_FORM_PADDING).right();
        table.add(valueLabel).left();
        table.row();
    }

    /**
     * Creates a new {@link HeadsUpDisplay} relating to the given {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} to which the {@link HeadsUpDisplay} should be connected
     * @param leftMetrics The {@link MetricController.Metric} elements to be displayed on the left-hand side
     * @param rightMetrics The {@link MetricController.Metric} elements to be displayed on the right-hand side
     */
    HeadsUpDisplay(SpriteBatch batch, MetricController.Metric[] leftMetrics,
                          MetricController.Metric[] rightMetrics) {
        super(batch);

        // Configure the outer HUD-level table
        final Table outerTable = new Table();
        outerTable.setFillParent(true);
        outerTable.pad(GENERAL_FORM_PADDING);

        // Add the specified LHS metric indicators
        final Table leftTable = new Table();
        for (MetricController.Metric metric : leftMetrics)
            addLabel(metric, leftTable);
        outerTable.add(leftTable).expand().left().top();

        // Add the specified RHS metric indicators
        final Table rightTable = new Table();
        for (MetricController.Metric metric : rightMetrics)
            addLabel(metric, rightTable);
        outerTable.add(rightTable).expand().right().top();

        super.addActor(outerTable);

        final Label advice = new Label("Use W-A-S-D to move; E to interact with nearby objects; " +
            "and Escape to pause", LABEL_STYLE);
        advice.setPosition(GENERAL_FORM_PADDING, GENERAL_FORM_PADDING);
        super.addActor(advice);
    }
}
