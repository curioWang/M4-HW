package theater;

/**
 * Calculator implementation for 'tragedy' plays.
 */
public class TragedyCalculator extends AbstractPerformanceCalculator {

    public TragedyCalculator(final Performance performance, final Play play) {
        super(performance, play);
    }

    /**
     * 4.3: 实现悲剧的 getAmount 逻辑.
     */
    @Override
    public int getAmount() {
        int result = Constants.TRAGEDY_BASE_AMOUNT;
        if (getPerformance().getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
            result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                    * (getPerformance().getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
        }
        return result;
    }

    // getVolumeCredits 继承自 AbstractPerformanceCalculator 的通用逻辑
}
