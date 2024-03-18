package bytemusketeers.heslingtonhustle;

/**
 * The {@link MetricUpdater} provides a common controller to link a single {@link MetricManager} and
 * {@link MetricListener}, by means of enabling the former to post updates to the latter
 *
 * @implNote Future implementors may wish to make {@link #listener} a {@link java.util.List} of {@link MetricListener}
 *           listeners, but for this simple initial implementation, a single listener will suffice.
 * @see MetricManager#assignUpdater(Runnable)
 * @author ENG1 Team 23 (Cohort 3)
 */
class MetricUpdater implements Runnable {
    /**
     * The source of the {@link MetricManager} updates
     *
     * @see #listener
     */
    private final MetricManager manager;

    /**
     * The recipient of the {@link MetricManager} updates
     *
     * @see #manager
     */
    private final MetricListener listener;

    /**
     * Sends an update of a single {@link MetricManager.Metric} to the registered {@link MetricListener}. This method
     * is implicitly responsible for determining the
     *
     * @see #manager
     * @see #listener
     */
    private void sendUpdate(MetricManager.Metric metric) {
        listener.updateMetricText(metric, manager.getMetricStringValue(metric));
    }

    /**
     * Update the HUD with the last-updated metric from {@link MetricManager}
     */
    @Override
    public void run() {
        sendUpdate(manager.getLastChangedMetric());
    }

    /**
     * Instantiates a new {@link MetricUpdater} to provide updates to a {@link MetricListener} on the transient states
     * of a {@link MetricManager}. Upon construction, an initial pulse is of each {@link MetricManager.Metric} to the
     * given {@link MetricListener}.
     *
     * @param manager The data-source {@link MetricManager}
     * @param listener The data-recipient {@link MetricListener}
     */
    public MetricUpdater(MetricManager manager, MetricListener listener) {
        this.manager = manager;
        this.listener = listener;

        // Send an initial pulse of the status of each metric at the time of the updater construction
        for (MetricManager.Metric metric : MetricManager.Metric.values())
            sendUpdate(metric);
    }
}
