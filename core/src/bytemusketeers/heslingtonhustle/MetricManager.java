package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link MetricManager} manages the storing and manipulation of the game and player metrics, each type of which is
 * identified by a unique {@link Metric} enumerable key.
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class MetricManager {
    /**
     * The standard amount by which a metric value may be increased or decreased. An integer multiplier may be used.
     *
     * @see #incrementMetric(Metric, int)
     * @see #decrementMetric(Metric, int)
     */
    private static final int INCREMENT_DELTA = 1;

    /**
     * The default value of each {@link Float} associated with a {@link Metric}
     */
    private static final int DEFAULT_VALUE = 0;

    /**
     * The discriminating key for each metric
     *
     * @see #getMetricValue(Metric)
     * @see #incrementMetric(Metric, int)
     * @see #decrementMetric(Metric, int)
     */
    enum Metric {
        Sleep("Number of Sleeps"),
        Study("Number of Study Sessions"),
        Eat("Number of Meals"),
        Play("Number of Recreational Sessions"),
        Area("Current Area"),
        Day("Current Day");

        /**
         * The display name of the {@link Metric}
         */
        private final String displayName;

        /**
         * Retrieves the display name of the {@link Metric}
         *
         * @return The requested display name
         */
        @Override
        public String toString() {
            return displayName;
        }

        /**
         * Constructs a new {@link Metric} enumerable entry with a suitable ordinal value and given display name
         *
         * @param displayName The {@link Metric} display name
         */
        Metric(String displayName) {
            this.displayName = displayName;
        }
    }

    /**
     * The map associating {@link Metric}s with their {@link Float} value.
     *
     * @apiNote In the interests of avoiding a {@link NullPointerException} on the {@link Integer} {@link Map}, no
     *          non-private method should access this map directly; some suitable variant of
     *          {@link #incrementMetric(Metric, int)} should be used exclusively by external clients.
     *          {@link #setMetric(Metric, int)} is acceptable when manipulating non-integral values.
     */
    private final Map<Metric, Integer> metrics = new EnumMap<>(Metric.class);

    /**
     * The action to execute upon any value of a {@link Metric} being changed. The execution should be deferred to
     * LibGDX to avoid potential threading-related race conditions.
     *
     * @see Runnable#run()
     * @see com.badlogic.gdx.Application#postRunnable(Runnable)
     * @see #setMetric(Metric, int)
     */
    private Runnable updateAction;

    /**
     * The last-updated {@link Metric}
     *
     * @see #getLastChangedMetric()
     */
    private Metric lastChangedMetric;

    /**
     * Day-of-the-week names
     *
     * @see Metric#Day
     * @see #getMetricStringValue(Metric)
     */
    private final static String[] DAY_NAMES = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    /**
     * Retrieves the value associated with the given {@link Metric}
     *
     * @param metric The {@link Metric} whose value is required
     * @return The value associated with the {@link Metric}
     */
    public Integer getMetricValue(Metric metric) {
        return metrics.get(metric);
    }

    /**
     * Retrieves the corresponding value of the given {@link Metric} as a formatted human-readable {@link String}
     *
     * @param metric The {@link Metric} whose {@link String} value is required
     * @return The {@link String} associated with the {@link Metric}
     * @implNote This is a crude solution. To deal with larger sets of {@link Metric} key-value pairs, we would need a
     *           dynamically typed 'Metric' entry-like class, akin to {@link Map.Entry}.
     */
    public String getMetricStringValue(Metric metric) {
        final Integer value = getMetricValue(metric);
        String text;

        switch (metric) {
            case Sleep:
            case Study:
            case Eat:
            case Play:
                text = value.toString();
                break;

            case Day:
                text = DAY_NAMES[value % DAY_NAMES.length];
                break;

            case Area:
                final Area.AreaName[] areaNames = Area.AreaName.values();
                text = areaNames[value % areaNames.length].toString();
                break;

            default:
                text = "Unknown value";
        }

        return text;
    }

    /**
     * Modifies a metric and completes any applicable housekeeping
     *
     * @param metric The {@link Metric} to modify
     * @param value The new value
     */
    public void setMetric(Metric metric, int value) {
        metrics.put(metric, value);
        lastChangedMetric = metric;

        if (updateAction != null)
            Gdx.app.postRunnable(updateAction);
    }

    /**
     * Increments a {@link Metric} by a multiple of the standard change unit
     *
     * @param metric The {@link Metric} to increment
     * @param multiplier The amount by which to scale the magnitude of the increment
     * @see #INCREMENT_DELTA
     */
    public void incrementMetric(Metric metric, int multiplier) {
        setMetric(metric, getMetricValue(metric) + INCREMENT_DELTA * multiplier);
    }

    /**
     * Decrements a {@link Metric} by a multiple of the standard change unit
     *
     * @param metric The {@link Metric} to increment
     * @param multiplier The amount by which to scale the magnitude of the decrement
     */
    public void decrementMetric(Metric metric, int multiplier) {
        setMetric(metric, getMetricValue(metric) - INCREMENT_DELTA * multiplier);
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
     * Sets the {@link Runnable} object on which {@link Runnable#run()} should be executed by
     * {@link com.badlogic.gdx.Application#postRunnable(Runnable)} upon a {@link Metric} value being updated.
     *
     * @param updateAction The {@link Runnable} object
     * @see #getLastChangedMetric()
     * @see #getMetricValue(Metric)
     */
    public void assignUpdater(Runnable updateAction) {
        this.updateAction = updateAction;
    }

    /**
     * Instantiates a new {@link MetricManager}, providing sensible a sensible default value to each {@link Metric}
     *
     * @see #DEFAULT_VALUE
     */
    public MetricManager() {
        for (Metric metric : Metric.values())
            metrics.put(metric, DEFAULT_VALUE);
    }
}
