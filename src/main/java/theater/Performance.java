package theater;

/**
 * Represents a single performance of a play.
 */
public class Performance {
    private final String playID;
    private final int audience;

    /**
     * Creates a performance record.
     *
     * @param playID   the ID of the play performed
     * @param audience the number of audience members
     */
    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    /**
     * Returns the ID of the play performed.
     *
     * @return the play's ID
     */
    public String getPlayID() {
        return playID;
    }

    /**
     * Returns the audience size for this performance.
     *
     * @return the audience count
     */
    public int getAudience() {
        return audience;
    }
}

