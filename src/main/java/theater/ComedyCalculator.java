package theater;

/**
 * Calculator implementation for 'comedy' plays.
 */
public class ComedyCalculator extends AbstractPerformanceCalculator {

    public ComedyCalculator(final Performance performance, final Play play) {
        super(performance, play);
    }

    /**
     * 4.3: 实现喜剧的 getAmount 逻辑.
     */
    @Override
    public int getAmount() {
        int result = Constants.COMEDY_BASE_AMOUNT;
        if (getPerformance().getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
            result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                    + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                    * (getPerformance().getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
        }
        result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * getPerformance().getAudience();
        return result;
    }

    /**
     * 4.3: 重写 getVolumeCredits 以添加额外的喜剧积分.
     */
    @Override
    public int getVolumeCredits() {
        // 调用父类的基础积分，然后加上额外积分
        return super.getVolumeCredits() + getPerformance().getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
    }
}
