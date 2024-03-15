package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link HeadsUpDisplay} displays persistent player and game information throughout the gameplay
 *
 * @see PlayScreen
 */
class HeadsUpDisplay extends Overlay {
    private final Map<MetricManager.Metric, Label> metricLabels = new EnumMap<>(MetricManager.Metric.class);

    /**
     * Update the {@link MetricManager.Metric} label with the given value
     *
     * @param metric The key of the {@link MetricManager.Metric} to update
     * @param text The new text
     */
    public void updateMetricElement(MetricManager.Metric metric, String text) {
        metricLabels.get(metric).setText(text);
    }

    /**
     * Creates a new {@link HeadsUpDisplay} relating to the given {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} to which the {@link HeadsUpDisplay} should be connected
     */
    public HeadsUpDisplay(SpriteBatch batch) {
        super(batch);

        final Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        // Create the HUD table: each row consists of a metric label and its value label
        Table table = new Table();
        table.setFillParent(true);
        table.top().left().pad(GENERAL_PADDING);

        for (MetricManager.Metric metric : MetricManager.Metric.values()) {
            Label value = new Label(MetricManager.DEFAULT_VALUE.toString(), labelStyle);
            metricLabels.put(metric, value);

            table.add(new Label(metric.toString(), labelStyle)).right();
            table.add(value).padLeft(GENERAL_PADDING).left();
            table.row();
        }

        // The table needs to be the sole actor visible on the stage
        super.addActor(table);

        // TODO: temporary tooltip advice
        Label advice = new Label("Press E anywhere away from the interactables to switch to Piazza; M = CompSci; N = Bedroom; B = World Map", labelStyle);
        advice.setPosition(GENERAL_PADDING, GENERAL_PADDING);
        super.addActor(advice);
    }
}
