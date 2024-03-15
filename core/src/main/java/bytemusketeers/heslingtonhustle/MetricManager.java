package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link MetricManager} manages the storing and manipulation of the game and player metrics, each type of which is
 * identified by a unique {@link Metric} enumerable key.
 */
class MetricManager {
    /**
     * The standard amount by which a metric value may be increased or decreased. An integer multiplier may be used.
     *
     * @see #incrementMetric(Metric, float)
     * @see #decrementMetric(Metric, float)
     */
    private static final int INCREMENT_DELTA = 1;

    /**
     * The default value of each {@link Float} associated with a {@link Metric}
     */
    public static final Float DEFAULT_VALUE = 0f;

    /**
     * The discriminating key for each metric
     *
     * @see #getMetricValue(Metric)
     * @see #incrementMetric(Metric, float)
     * @see #decrementMetric(Metric, float)
     */
    enum Metric {
        Happiness, Tiredness, Preparedness
    }

    /**
     * The map associating {@link Metric}s with their {@link Float} value. In the interests of avoiding a
     * {@link NullPointerException} on the {@link Float} {@link Map}, no non-private method should access this map
     * directly; some suitable variant of {@link #incrementMetric(Metric, float)} should be used exclusively by external
     * clients.
     */
    private final Map<Metric, Float> metrics = new EnumMap<>(Metric.class);

    /**
     * The action to execute upon any value of a {@link Metric} being changed. The execution should be deferred to
     * LibGDX to avoid potential threading-related race conditions.
     *
     * @see Runnable#run()
     * @see com.badlogic.gdx.Application#postRunnable(Runnable)
     * @see #modifyMetric(Metric, float)
     */
    private final Runnable updateAction;

    /**
     * The last-updated {@link Metric}
     *
     * @see #getLastChangedMetric()
     */
    private Metric lastChangedMetric;

    /**
     * Retrieves the value associated with the given {@link Metric}
     *
     * @param metric The {@link Metric} whose value is required
     * @return The value associated with the {@link Metric}
     */
    public Float getMetricValue(Metric metric) {
        return metrics.get(metric);
    }

    /**
     * Modifies a metric and completes any housekeeping
     *
     * @param metric The {@link Metric} to modify
     * @param value The new value
     */
    private void modifyMetric(Metric metric, float value) {
        metrics.put(metric, value);
        lastChangedMetric = metric;
        Gdx.app.postRunnable(updateAction);
    }

    /**
     * Increments a {@link Metric} by a multiple of the standard change unit
     *
     * @param metric The {@link Metric} to increment
     * @param multiplier The amount by which to scale the magnitude of the increment
     * @see #INCREMENT_DELTA
     */
    public void incrementMetric(Metric metric, float multiplier) {
        modifyMetric(metric, getMetricValue(metric) + INCREMENT_DELTA * multiplier);
    }

    /**
     * Decrements a {@link Metric} by a multiple of the standard change unit
     *
     * @param metric The {@link Metric} to increment
     * @param multiplier The amount by which to scale the magnitude of the decrement
     */
    public void decrementMetric(Metric metric, float multiplier) {
        modifyMetric(metric, getMetricValue(metric) - INCREMENT_DELTA * multiplier);
    }

    /**
     * Retrieves the {@link Metric} whose value was most recently modified
     *
     * @return The type of the recently changed {@link Metric}
     */
    public Metric getLastChangedMetric() {
        return lastChangedMetric;
    }

    /**
     * Instantiates a new {@link MetricManager} with the specified {@link Runnable} update task
     *
     * @param updateAction The {@link Runnable} object, where {@link Runnable#run()} is executed upon any value of
     *                     {@link #metrics} being updated.
     */
    public MetricManager(Runnable updateAction) {
        this.updateAction = updateAction;

        for (Metric metric : Metric.values())
            metrics.put(metric, DEFAULT_VALUE);
    }
}
