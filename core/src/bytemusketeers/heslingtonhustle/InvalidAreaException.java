package bytemusketeers.heslingtonhustle;

/**
 * An {@link InvalidAreaException} indicates that an {@link Area} could not properly be loaded
 *
 * @see Area
 * @see AreaFactory
 * @see PlayScreen
 * @author ENG1 Team 23 (Cohort 3)
 */
class InvalidAreaException extends Exception {
    /**
     * Create an {@link InvalidAreaException} with the given {@link String} message
     *
     * @param msg A more detailed description of the nature/cause of the {@link InvalidAreaException}
     */
    public InvalidAreaException(String msg) {
        super(msg);
    }
}
