package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link HeadsUpDisplay} displays persistent player and game information throughout the gameplay
 *
 * @see PlayScreen
 */
class HeadsUpDisplay implements Drawable {
    private final Stage stage;
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
     * Releases all resources used by the {@link HeadsUpDisplay}
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Renders the current {@link HeadsUpDisplay} to the given {@link SpriteBatch}
     *
     * @param batch Target of the rendering operation
     */
    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(stage.getCamera().combined);
        stage.act();
        stage.draw();
    }

    /**
     * Instantiates a new {@link HeadsUpDisplay} to be displayed over the {@link PlayScreen}
     */
    public HeadsUpDisplay(SpriteBatch batch) {
        final int PADDING = 10;

        // Set up the viewport and HUD stage
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);

        // Create the HUD table: each row consists of a metric label and its value label
        Table table = new Table();
        table.setFillParent(true);
        table.top().left().pad(PADDING);

        for (MetricManager.Metric metric : MetricManager.Metric.values()) {
            Label value = new Label(MetricManager.DEFAULT_VALUE.toString(), labelStyle);
            metricLabels.put(metric, value);

            table.add(new Label(metric.toString(), labelStyle)).right();
            table.add(value).padLeft(PADDING).left();
            table.row();
        }

        // The table needs to be the sole actor visible on the stage
        stage.addActor(table);

        // TODO: temporary tooltip advice
        Label advice = new Label("Press E anywhere away from the interactables to switch to Piazza", labelStyle);
        advice.setPosition(PADDING, PADDING);
        stage.addActor(advice);
    }
}
