package theater;

/**
 * Immutable value object describing a play.
 * <p>Holds the play's {@code name} and {@code type} ("tragedy", "comedy", etc.).</p>
 */
public class Play {
    private final String name;
    private final String type;

    /**
     * Creates a play with the given name and type.
     *
     * @param name the name of the play
     * @param type the type of the play ("tragedy", "comedy", etc.)
     */
    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the name of the play.
     *
     * @return the play's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the play.
     *
     * @return the play's type (e.g., "tragedy", "comedy")
     */
    public String getType() {
        return type;
    }
}

