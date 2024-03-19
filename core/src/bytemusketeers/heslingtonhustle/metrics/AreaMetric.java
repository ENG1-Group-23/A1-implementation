package bytemusketeers.heslingtonhustle.metrics;

import bytemusketeers.heslingtonhustle.scene.Area;

/**
 * An {@link AreaMetric} represents an {@link Area} as a {@link MetricEntry} to be managed by the
 * {@link MetricController}
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class AreaMetric implements MetricEntry {
    /**
     * The {@link Area.Name} representing the {@link Area} embedded within the {@link AreaMetric}
     *
     * @see #getValue()
     */
    private Area.Name area;

    /**
     * Retrieves the {@link #area} associated with the {@link AreaMetric}
     *
     * @return A {@link String} representation of the {@link #area}
     */
    @Override
    public String getValue() {
        return area.toString();
    }

    /**
     * Updates the current {@link #area} with the given {@link Area.Name}
     *
     * @param areaName The updated {@link Area.Name}
     */
    void setArea(Area.Name areaName) {
        area = areaName;
    }

    /**
     * Instantiates a new {@link AreaMetric} with the given initial {@link Area.Name}
     *
     * @param initialArea The initial {@link Area.Name}
     */
    AreaMetric(@SuppressWarnings("SameParameterValue") Area.Name initialArea) {
        area = initialArea;
    }
}
