package bytemusketeers.heslingtonhustle;

/**
 * A {@link MetricListener} is interested in receives updates from a {@link MetricManager} through the corresponding
 * {@link MetricUpdater}.
 *
 * @see MetricManager
 * @see MetricUpdater
 * @author ENG1 Team 23 (Cohort 3)
 */
interface MetricListener {
    /**
     * Informs the implementor that the text associated with the given {@link MetricManager.Metric} should be updated
     *
     * @param metric The key of the {@link MetricManager.Metric} to be updated
     * @param text The revised text
     */
    void updateMetricText(MetricManager.Metric metric, String text);
}
