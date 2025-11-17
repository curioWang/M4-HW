package theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Holds all calculated data for a statement (customer, performances, totals).
 */
public class StatementData {
    // --- play type literals extracted to constants (MultipleStringLiterals) ---
    private static final String TYPE_TRAGEDY = "tragedy";
    private static final String TYPE_COMEDY = "comedy";

    private final String customer;
    private final List<PerformanceData> performances = new ArrayList<>();

    public StatementData(final Invoice invoice, final Map<String, Play> plays) {
        this.customer = invoice.getCustomer();

        // 3.1: 在构造函数里完成 performance 级别的计算
        for (final Performance performance : invoice.getPerformances()) {
            this.performances.add(
                    new PerformanceData(performance, plays.get(performance.getPlayID()))
            );
        }
    }

    // 3.1: PerformanceData 的构造函数现在包含了计算逻辑，无需 createPerformanceData helper。
    // 3.1: 删除了 amountFor 和 volumeCreditsFor 方法。

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
     * @return the total amount owed in cents
     */
    public int volumeCredits() {
        int result = 0;
        for (final PerformanceData perfData : performances) {
            result += perfData.getVolumeCredits();
        }
        return result;
    }
}
