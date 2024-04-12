package bytemusketeers.heslingtonhustle.metrics;

import bytemusketeers.heslingtonhustle.scene.Area;

import java.util.EnumMap;
import java.util.Map;

/**
 * A {@link MetricController} stores and manages many {@link MetricEntry} items and their associated {@link Metric}
 * keys, providing a unified interface through which higher-level controllers may manipulate the values of
 * {@link MetricEntry} objects and post runtime alteration updates to the {@link MetricUpdater}.
 *
 * @author ENG1 Team 23 (Cohort 3)
 * @see MetricEntry
 * @see MetricUpdater
 */
public class MetricController {
    /**
     * Distinguish between the standard metrics represented in the {@link MetricEntry} objects
     */
    public enum Metric {
        Sleep("Number of Sleeps"),
        Study("Number of Study Sessions"),
        Eat("Number of Meals"),
        Play("Number of Recreational Sessions"),
        Area("Current Area"),
        Day("Current Day");

        /**
         * A human-readable name for the {@link Metric} ordinal
         */
        private final String displayName;

        /**
         * Retrieves the human-readable {@link Metric} display name
         *
         * @return The name for the requested {@link Metric}
         */
        @Override
        public String toString() {
            return displayName;
        }

        /**
         * Instantiates a new standard {@link Metric} template
         *
         * @param displayName The human-readable name of the new {@link Metric}
         */
        Metric(String displayName) {
            this.displayName = displayName;
        }
    }

    /**
     * The callback to execute following any {@link MetricEntry} value updates
     *
     * @see MetricUpdater
     */
    private final MetricUpdater updateAction;

    /**
     * The associative key-value map between {@link Metric} identifiers and {@link MetricEntry} storage classes
     */
    private final Map<Metric, MetricEntry> metrics = new EnumMap<>(Metric.class);

    /**
     * Retrieves a {@link MetricEntry} of the given variety associated with the given {@link Metric} key
     *
     * @param metricType The expected received value-type from {@link #metrics}
     * @param metric The {@link Metric} associated with the desired {@link MetricEntry}, of the given type
     * @return The requested {@link MetricEntry}, or null if there was a type mismatch or nonexistent key
     * @apiNote An {@link Exception} extension would ideally be thrown in event of a type mismatch, but it is
     *          impractical here due to the size of the call stack at this stage. Any (extremely unlikely) null-return
     *          should be dealt with by the immediate caller with a lightweight comparison; i.e., throwing exceptions
     *          in the LibGDX render thread/stage is undesirable!
     */
    private MetricEntry getDynamicMetricEntry(Class<? extends MetricEntry> metricType, Metric metric) {
        MetricEntry entry = metrics.get(metric);
        return (entry.getClass() == metricType) ? entry : null;
    }

    /**
     * Increments the specified {@link PlayerMetric} with the given integral multiplier
     *
     * @param metric The key associated with the target {@link PlayerMetric}
     * @param multiplier The integral multiplier
     * @see PlayerMetric#incrementMetric(int)
     */
    public void incrementPlayerMetric(Metric metric, int multiplier) {
        PlayerMetric entry = (PlayerMetric) getDynamicMetricEntry(PlayerMetric.class, metric);
        if (entry != null) {
            entry.incrementMetric(multiplier);
            updateAction.sendUpdate(metric, getMetricStringValue(metric));
        }
    }

    /**
     * Decrements the specified {@link PlayerMetric} with the given integral multiplier
     *
     * @param metric The key associated with the target {@link PlayerMetric}
     * @param multiplier The integral multiplier
     * @see PlayerMetric#decrementMetric(int)
     */
    public void decrementPlayerMetric(Metric metric, int multiplier) {
        PlayerMetric entry = (PlayerMetric) getDynamicMetricEntry(PlayerMetric.class, metric);
        if (entry != null) {
            entry.decrementMetric(multiplier);
            updateAction.sendUpdate(metric, getMetricStringValue(metric));
        }
    }

    /**
     * Advances the day-of-the-week metric: the {@link MetricEntry} associated with {@link Metric#Day}
     *
     * @see DOWMetric#nextDay()
     */
    public void advanceDOWMetric() {
        DOWMetric entry = (DOWMetric) getDynamicMetricEntry(DOWMetric.class, Metric.Day);
        if (entry != null) {
            entry.nextDay();
            updateAction.sendUpdate(Metric.Day, getMetricStringValue(Metric.Day));
        }
    }

    /**
     * Does the {@link DOWMetric} entry suggested that the end is nigh?
     *
     * @return Is the game on the final day?
     */
    public boolean isFinalDay() {
        DOWMetric entry = (DOWMetric) getDynamicMetricEntry(DOWMetric.class, Metric.Day);
        return (entry == null || entry.isFinalDay());
    }

    /**
     * Updates the area metric with the given {@link Area.Name}: the {@link MetricEntry} associated with
     * {@link Metric#Area}
     *
     * @param areaName The updated {@link Area.Name}
     * @see AreaMetric#setArea(Area.Name)
     */
    public void changeAreaMetric(Area.Name areaName) {
        AreaMetric entry = (AreaMetric) getDynamicMetricEntry(AreaMetric.class, Metric.Area);
        if (entry != null) {
            entry.setArea(areaName);
            updateAction.sendUpdate(Metric.Area, getMetricStringValue(Metric.Area));
        }
    }

    /**
     * Retrieves the {@link String} value of the {@link MetricEntry} associated with the given {@link Metric} key
     *
     * @param metric The {@link Metric} key associated with the desired {@link MetricEntry}
     * @return A human-readable formatted {@link String} representing the value of the requested {@link MetricEntry}
     * @see MetricEntry#getValue()
     */
    public String getMetricStringValue(Metric metric) {
        return metrics.get(metric).getValue();
    }

    /**
     * Instantiates a new {@link MetricController} and populates the {@link #metrics} with sensible defaults
     *
     * @see MetricEntry
     */
    public MetricController(MetricUpdater updateAction) {
        this.updateAction = updateAction;

        metrics.put(Metric.Sleep, new PlayerMetric(0));
        metrics.put(Metric.Study, new PlayerMetric(0));
        metrics.put(Metric.Eat, new PlayerMetric(0));
        metrics.put(Metric.Play, new PlayerMetric(0));
        metrics.put(Metric.Area, new AreaMetric(Area.Name.OutdoorMap));
        metrics.put(Metric.Day, new DOWMetric());
    }
}
