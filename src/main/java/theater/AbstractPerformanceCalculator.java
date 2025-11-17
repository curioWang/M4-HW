package theater;

/**
 * Base class for calculating performance details (amount and credits).
 * In Task 4.2, it holds all calculation logic before polymorphism is introduced.
 */
// 4.2: 移除 abstract 关键字，使其可以被实例化
public class AbstractPerformanceCalculator {

    // 4.2/Checkstyle Fix: 将 static 变量移到实例变量之前
    private static final String TYPE_COMEDY = "comedy";
    private static final String TYPE_TRAGEDY = "tragedy";

    private final Performance performance;
    private final Play play;

    // ... (构造函数 and methods) ...

    /**
     * Constructs an AbstractPerformanceCalculator.
     * @param performance the performance data
     * @param play the associated play information
     */
    public AbstractPerformanceCalculator(final Performance performance, final Play play) {
        this.performance = performance;
        this.play = play;
    }

    /**
     * Factory Method - Responsible for returning the correct calculator instance.
     * In Task 4.2, it simply returns the base class instance.
     * @param performance the performance data
     * @param play the associated play information
     * @return a base AbstractPerformanceCalculator instance
     */
    public static AbstractPerformanceCalculator createPerformanceCalculator(
            final Performance performance, final Play play) {

        // 4.2: 返回基类实例
        return new AbstractPerformanceCalculator(performance, play);
    }

    /**
     * Calculates the total amount for the performance (Logic moved from PerformanceData).
     * @return the calculated amount in cents.
     * @throws RuntimeException if the play type is unknown
     */
    public int getAmount() {
        final Play currentPlay = getPlay();
        int result;

        switch (currentPlay.getType()) {
            case TYPE_TRAGEDY:
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (getPerformance().getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (getPerformance().getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;

            case TYPE_COMEDY:
                result = Constants.COMEDY_BASE_AMOUNT;
                if (getPerformance().getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (getPerformance().getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * getPerformance().getAudience();
                break;

            default:
                throw new RuntimeException(String.format("unknown type: %s", currentPlay.getType()));
        }
        return result;
    }

    /**
     * Calculates the volume credits earned for the performance (Logic moved from PerformanceData).
     * @return the earned credits.
     */
    public int getVolumeCredits() {
        int result = 0;

        result += Math.max(getPerformance().getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);

        if (TYPE_COMEDY.equals(getPlay().getType())) {
            result += getPerformance().getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        return result;
    }

    // --- Getter Methods (用于封装私有字段) ---

    /**
     * Returns the raw performance data.
     * @return the performance data.
     */
    protected Performance getPerformance() {
        return performance;
    }

    /**
     * Returns the associated play information.
     * @return the play information.
     */
    protected Play getPlay() {
        return play;
    }
}
