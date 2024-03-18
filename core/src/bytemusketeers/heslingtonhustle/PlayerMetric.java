package bytemusketeers.heslingtonhustle;

/**
 * A {@link PlayerMetric} represents a {@link MetricEntry} pertaining to the player-controlled {@link Character} to be
 * managed by the {@link MetricController}
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class PlayerMetric implements MetricEntry {
    /**
     * The standard amount by which the {@link #value} may be increased or decreased. Any integer multiple of this value
     * may also be used.
     *
     * @see #incrementMetric(int)
     * @see #decrementMetric(int)
     */
    private static final int STANDARD_INCREMENT = 1;

    /**
     * The integral value of the {@link PlayerMetric}
     */
    private Integer value = 0;

    /**
     * Retrieves the {@link Integer} associated with the {@link PlayerMetric}
     *
     * @return A {@link String} representation of the {@link #value}
     */
    @Override
    public String getValue() {
        return value.toString();
    }

    /**
     * Increments the {@link PlayerMetric#value} by the given multiple of the {@link #STANDARD_INCREMENT}
     *
     * @param multiplier An integer multiplier of the {@link #STANDARD_INCREMENT}
     */
    public void incrementMetric(int multiplier) {
        value += STANDARD_INCREMENT * multiplier;
    }

    /**
     * Decrements the {@link PlayerMetric#value} by the given multiple of the {@link #STANDARD_INCREMENT}
     *
     * @param multiplier An integer multiplier of the {@link #STANDARD_INCREMENT}
     */
    public void decrementMetric(int multiplier) {
        value -= STANDARD_INCREMENT * multiplier;
    }
}
