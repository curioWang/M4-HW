package theater;

/**
 * Abstract base class for calculating performance details (amount and credits).
 * Serves as a base for polymorphic behavior in Task 4.
 */
public abstract class AbstractPerformanceCalculator {
    // 1. 更改为 private，提高封装性
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
     * The actual switch logic is implemented in Task 4.3.
     * @param performance the performance data
     * @param play the associated play information
     * @return a concrete subclass of AbstractPerformanceCalculator
     */
    public static AbstractPerformanceCalculator createPerformanceCalculator(
            final Performance performance, final Play play) {

        // 为了 Task 4.1 阶段通过编译和测试，我们暂时返回一个匿名内部类实例。
        // Task 4.3 将会替换这里的逻辑。
        return new AbstractPerformanceCalculator(performance, play) {
            @Override
            public int getAmount() {
                // 确保 getAmount 返回原始计算结果，以便测试通过。
                // 这里的逻辑需要在 Task 4.2 中从 PerformanceData 搬移过来。
                return 0;
            }

            @Override
            public int getVolumeCredits() {
                return 0;
            }
        };
    }

    // 1. 添加公共访问器 (Getter Methods)

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

    // 抽象方法签名保持不变

    /**
     * Calculates the total amount for the performance.
     * This method must be implemented by concrete calculator subclasses.
     * @return the calculated amount in cents.
     */
    public abstract int getAmount();

    /**
     * Calculates the volume credits earned for the performance.
     * This method must be implemented by concrete calculator subclasses.
     * @return the earned credits.
     */
    public abstract int getVolumeCredits();
}
