package theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Holds all calculated data for a statement (customer, performances, totals).
 */
public class StatementData {
    // 3.1: 删除了未使用的 TYPE_TRAGEDY 和 TYPE_COMEDY 常量

    private final String customer;
    private final List<PerformanceData> performances = new ArrayList<>();
    private final Map<String, Play> plays;

    public StatementData(final Invoice invoice, final Map<String, Play> plays) {
        this.customer = invoice.getCustomer();
        this.plays = plays;

        // 3.1: 在构造函数里完成 performance 级别的计算
        for (final Performance performance : invoice.getPerformances()) {
            // 4.1: 调用 createPerformanceData 辅助方法
            this.performances.add(createPerformanceData(performance));
        }
    }

    /**
     * 4.1: 辅助方法，使用 Factory Method 创建计算器并实例化 PerformanceData。
     * @param performance the performance data to be processed.
     * @return a new PerformanceData instance with an associated calculator.
     */
    private PerformanceData createPerformanceData(final Performance performance) {
        // 4.1: 获取 Play 实例
        final Play play = this.plays.get(performance.getPlayID());

        // 4.1: 使用 Factory Method 创建计算器
        final AbstractPerformanceCalculator calculator =
                AbstractPerformanceCalculator.createPerformanceCalculator(performance, play);

        // 4.1: 使用计算器创建 PerformanceData (PerformanceData 构造函数需要计算器)
        return new PerformanceData(performance, play, calculator);
    }

    /**
     * Returns the customer name.
     * @return the customer name
     */
    public String getCustomer() {
        return customer;
    }

    public List<PerformanceData> getPerformances() {
        return performances;
    }

    /**
     * Calculates the total amount owed for all performances.
     * @return the total amount owed in cents
     */
    public int totalAmount() {
        int result = 0;
        for (final PerformanceData perfData : performances) {
            result += perfData.getAmount();
        }
        return result;
    }

    /**
     * Calculates the total volume credits earned for all performances.
     * @return the total volume credits earned
     */
    public int volumeCredits() {
        int result = 0;
        for (final PerformanceData perfData : performances) {
            result += perfData.getVolumeCredits();
        }
        return result;
    }
}
