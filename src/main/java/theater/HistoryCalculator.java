package theater;

/**
 * Calculator for history plays.
 */
public class HistoryCalculator extends AbstractPerformanceCalculator {

    /**
     * Constructs a calculator for a history play.
     *
     * @param performance the performance data
     * @param play        the play
     */
    public HistoryCalculator(final Performance performance, final Play play) {
        super(performance, play);
    }

    @Override
    public int getAmount() {
        int result = Constants.HISTORY_BASE_AMOUNT;

        if (getPerformance().getAudience() > Constants.HISTORY_AUDIENCE_THRESHOLD) {
            result += Constants.HISTORY_OVER_BASE_CAPACITY_PER_PERSON
                    * (getPerformance().getAudience()
                    - Constants.HISTORY_AUDIENCE_THRESHOLD);
        }

        return result;
    }

    @Override
    public int getVolumeCredits() {
        return Math.max(
                getPerformance().getAudience() - Constants.HISTORY_VOLUME_CREDIT_THRESHOLD,
                0);
    }
}
