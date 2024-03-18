package bytemusketeers.heslingtonhustle.metrics;

/**
 * A {@link MetricEntry} implementation denotes a metric of a particular type, e.g. numerical or textual
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
interface MetricEntry {
    /**
     * Retrieves the human-readable {@link String} value of the {@link MetricEntry}
     *
     * @return The requested value, formatted as a {@link String}
     */
    String getValue();
}
