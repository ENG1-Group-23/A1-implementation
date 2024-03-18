package bytemusketeers.heslingtonhustle;

/**
 * The {@link MetricUpdater} provides a common controller to link a single {@link MetricController} and
 * {@link MetricListener}, by means of enabling the former to post updates to the latter
 *
 * @implNote Future implementors may wish to make {@link #metricListener} a {@link java.util.List} of
 *           {@link MetricListener} listeners, but for this simple initial implementation, a single listener will
 *           suffice.
 * @author ENG1 Team 23 (Cohort 3)
 */
class MetricUpdater {
    /**
     * The recipient of the {@link MetricController} updates
     */
    private final MetricListener metricListener;

    /**
     * Sends an update of a single {@link MetricController.Metric} to the registered {@link MetricListener}
     *
     * @see #metricListener
     */
    public void sendUpdate(MetricController.Metric metric, String value) {
        metricListener.updateMetricText(metric, value);
    }

    /**
     * Instantiates a new {@link MetricUpdater} to provide updates to a {@link MetricListener} on the transient states
     * of a {@link MetricController}
     *
     * @param metricListener The data-recipient {@link MetricListener}
     */
    public MetricUpdater(MetricListener metricListener) {
        this.metricListener = metricListener;
    }
}
