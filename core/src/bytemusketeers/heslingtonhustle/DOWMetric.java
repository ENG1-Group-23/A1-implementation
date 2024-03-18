package bytemusketeers.heslingtonhustle;

/**
 * A {@link DOWMetric} represents a singular day of the week to be managed by the {@link MetricController}
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class DOWMetric implements MetricEntry {
    /**
     * Distinguish between days of the week with the default {@link DayOfWeek#toString()} methods for display
     */
    private enum DayOfWeek {
        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    }

    /**
     * The {@link DayOfWeek} embedded in the {@link DOWMetric}
     */
    private DayOfWeek value;

    /**
     * Retrieves a human-readable {@link #value} associated with the {@link DOWMetric}
     *
     * @return A {@link String} representation of the embedded {@link DayOfWeek}
     */
    @Override
    public String getValue() {
        return value.toString();
    }

    /**
     * Increments the {@link #value} in the expected fashion, assuming the order specified in {@link DayOfWeek}. If the
     * final day is reached, the first day is silently selected.
     */
    public void nextDay() {
        final DayOfWeek[] values = DayOfWeek.values();
        value = values[(value.ordinal() + 1) % values.length];
    }

    /**
     * Instantiates a new {@link DayOfWeek} with the initial day {@link DayOfWeek#Monday}
     */
    public DOWMetric() {
        value = DayOfWeek.Monday;
    }
}
