package main.java.bytemusketeers.heslingtonhustle;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link MetricManager} manages the storing and manipulation of the game and player metrics, each type of which is
 * identified by a unique {@link Metric} enumerable key.
 */
final class MetricManager {
    private static final int DELTA = 1;
    public static final Float DEFAULT_VALUE = 0f;
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
    private final Runnable updateAction;
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
        updateAction.run();
    }

    /**
     * Increments a {@link Metric} by a multiple of the standard change unit
     *
     * @param metric The {@link Metric} to increment
     * @param multiplier The amount by which to scale the magnitude of the increment
     * @see #DELTA
     */
    public void incrementMetric(Metric metric, float multiplier) {
        modifyMetric(metric, getMetricValue(metric) + DELTA * multiplier);
    }

    /**
     * Decrements a {@link Metric} by a multiple of the standard change unit
     *
     * @param metric The {@link Metric} to increment
     * @param multiplier The amount by which to scale the magnitude of the decrement
     */
    public void decrementMetric(Metric metric, float multiplier) {
        modifyMetric(metric, getMetricValue(metric) - DELTA * multiplier);
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
