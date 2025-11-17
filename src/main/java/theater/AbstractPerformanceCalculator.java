package theater;

/**
 * Abstract base class for calculating performance details (amount and credits).
 * Serves as a base for polymorphic behavior in Task 4.
 */
// 4.3: 改回 abstract
public abstract class AbstractPerformanceCalculator {

    private static final String TYPE_COMEDY = "comedy";
    private static final String TYPE_TRAGEDY = "tragedy";

    private final Performance performance;
    private final Play play;

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
     * Factory Method - Responsible for returning the correct calculator instance based on play type.
     * @param performance the performance data
     * @param play the associated play information
     * @return a concrete subclass of AbstractPerformanceCalculator
     * @throws RuntimeException if the play type is unknown
     */
    public static AbstractPerformanceCalculator createPerformanceCalculator(
            final Performance performance, final Play play) {

        // 4.3: 降级为 Java 8 兼容的传统 switch 语句
        switch (play.getType()) {
            case TYPE_TRAGEDY:
                return new TragedyCalculator(performance, play);
            case TYPE_COMEDY:
                return new ComedyCalculator(performance, play);
            default:
                throw new RuntimeException(String.format("unknown type: %s", play.getType()));
        }
    }

    // --- 抽象和通用计算方法 ---

    /**
     * Calculates the total amount for the performance.
     * This method must be implemented by concrete calculator subclasses.
     * @return the calculated amount in cents.
     */
    public abstract int getAmount();

    /**
     * Calculates the volume credits earned for the performance. (通用实现)
     * @return the earned credits.
     */
    public int getVolumeCredits() {
        // 4.3: 保留基础积分计算逻辑
        return Math.max(getPerformance().getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
    }

    // --- Getter Methods ---

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
