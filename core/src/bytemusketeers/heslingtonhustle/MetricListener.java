package bytemusketeers.heslingtonhustle;

/**
 * A {@link MetricListener} is interested in receives updates from a {@link MetricController} through the corresponding
 * {@link MetricUpdater}.
 *
 * @see MetricController
 * @see MetricUpdater
 * @author ENG1 Team 23 (Cohort 3)
 */
interface MetricListener {
    /**
     * Informs the implementor that the text associated with the given {@link MetricController.Metric} should be updated
     *
     * @param metric The key of the {@link MetricController.Metric} to be updated
     * @param text The revised text
     */
    void updateMetricText(MetricController.Metric metric, String text);
}
