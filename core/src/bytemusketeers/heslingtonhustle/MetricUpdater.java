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
class MetricUpdater implements Runnable {
    /**
     * The source of the {@link MetricController} updates
     *
     * @see #metricListener
     */
    private final MetricController metricController;

    /**
     * The recipient of the {@link MetricController} updates
     *
     * @see #metricController
     */
    private final MetricListener metricListener;

    /**
     * Sends an update of a single {@link MetricController.Metric} to the registered {@link MetricListener}. This method
     * is implicitly responsible for determining the
     *
     * @see #metricController
     * @see #metricListener
     */
    private void sendUpdate(MetricController.Metric metric) {
        metricListener.updateMetricText(metric, metricController.getMetricStringValue(metric));
    }

    /**
     * Update the HUD with the last-updated metric from {@link MetricController}
     */
    @Override
    public void run() {
        sendUpdate(metricController.getLastChangedMetric());
    }

    /**
     * Instantiates a new {@link MetricUpdater} to provide updates to a {@link MetricListener} on the transient states
     * of a {@link MetricController}. Upon construction, an initial pulse is of each {@link MetricController.Metric} to
     * the given {@link MetricListener}.
     *
     * @param metricController The data-source {@link MetricController}
     * @param metricListener The data-recipient {@link MetricListener}
     */
    public MetricUpdater(MetricController metricController, MetricListener metricListener) {
        this.metricController = metricController;
        this.metricListener = metricListener;

        // Send an initial pulse of the status of each metric at the time of the updater construction
        for (MetricController.Metric metric : MetricController.Metric.values())
            sendUpdate(metric);
    }
}
