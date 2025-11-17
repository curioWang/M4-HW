package theater;

/**
 * Calculated data for a single performance.
 * This class serves as a data holder and delegates calculation responsibilities
 * to an internal AbstractPerformanceCalculator instance.
 */
public class PerformanceData {

    private final String name;
    private final int audience;

    // 4.1: 存储计算器实例
    private final AbstractPerformanceCalculator calculator;

    /**
     * Constructs a PerformanceData object and stores the calculator instance.
     * Calculations are delegated to the provided calculator instance.
     * @param performance the raw performance data.
     * @param play the associated play information.
     * @param calculator the specialized calculator instance for this performance type.
     */
    public PerformanceData(final Performance performance,
                           final Play play,
                           final AbstractPerformanceCalculator calculator) {
        // 3.1: 字段现在直接从传入的 Play 实例中获取
        this.name = play.getName();
        this.audience = performance.getAudience();

        // 4.1: 存储计算器实例
        this.calculator = calculator;

        // 4.2: 删除了 amount 和 volumeCredits 字段及其计算逻辑
    }

    /* ===================== Getters (委托给计算器) ===================== */

    public String getName() {
        return name;
    }

    public int getAudience() {
        return audience;
    }

    /**
     * 将计算职责委托给 calculator 实例.
     * @return the calculated amount.
     */
    public int getAmount() {
        return calculator.getAmount();
    }

    /**
     * 将计算职责委托给 calculator 实例.
     * @return the earned credits.
     */
    public int getVolumeCredits() {
        return calculator.getVolumeCredits();
    }
}
