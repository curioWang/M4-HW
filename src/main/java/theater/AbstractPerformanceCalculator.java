package theater;

/**
 * Base calculator responsible for computing the amount and volume credits
 * for a single performance and its play.
 */
public abstract class AbstractPerformanceCalculator {

    private final Performance performance;
    private final Play play;

    /**
     * Constructs a calculator for the given performance and play.
     *
     * @param performance the performance being evaluated
     * @param play        the play for this performance
     */
    public AbstractPerformanceCalculator(final Performance performance, final Play play) {
        this.performance = performance;
        this.play = play;
    }

    /**
     * Returns the performance associated with this calculator.
     *
     * @return the performance
     */
    protected Performance getPerformance() {
        return performance;
    }

    /**
     * Returns the play associated with this calculator.
     *
     * @return the play
     */
    protected Play getPlay() {
        return play;
    }

    /**
     * Calculates the amount (in cents) for this performance.
     *
     * @return the amount in cents
     */
    public abstract int getAmount();

    /**
     * Calculates the base volume credits for this performance.
     * Subclasses may override this to add extra credits.
     *
     * @return the volume credits
     */
    public int getVolumeCredits() {
        int result = 0;
        result += Math.max(
                performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD,
                0);
        return result;
    }

    /**
     * Factory method that creates an appropriate calculator subclass
     * based on the play's type.
     *
     * @param performance the performance
     * @param play        the play
     * @return a calculator for that performance/play pair
     * @throws RuntimeException if the play type is unknown
     */
    public static AbstractPerformanceCalculator createPerformanceCalculator(
            final Performance performance,
            final Play play) {

        switch (play.getType()) {
            case "tragedy":
                return new TragedyCalculator(performance, play);
            case "comedy":
                return new ComedyCalculator(performance, play);
            case "history":
                return new HistoryCalculator(performance, play);
            case "pastoral":
                return new PastoralCalculator(performance, play);
            default:
                throw new RuntimeException(
                        String.format("unknown type: %s", play.getType()));
        }
    }
}

