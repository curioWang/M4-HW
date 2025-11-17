package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Generates a plain-text statement for an invoice.
 */
public class StatementPrinter {

    // 3.1: 移除了 invoice 和 plays 字段
    // 3.1: 引入 StatementData 字段，以便子类 HTMLStatementPrinter 访问
    private final StatementData statementData;

    public StatementPrinter(final Invoice invoice, final Map<String, Play> plays) {
        // 3.1: 构造函数现在只初始化 StatementData
        this.statementData = new StatementData(invoice, plays);
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     *
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    public String statement() {
        // 3.1: 整个方法体被提取到 renderPlainText
        return renderPlainText(this.statementData);
    }

    // 3.1: 提取的渲染方法，只负责字符串拼接
    private String renderPlainText(final StatementData data) {
        final StringBuilder result =
                new StringBuilder("Statement for " + data.getCustomer() + System.lineSeparator());

        // 细项循环：遍历 PerformanceData
        for (final PerformanceData perfData : data.getPerformances()) {
            // print line for this order
            result.append(String.format("  %s: %s (%s seats)%n",
                    perfData.getName(),
                    // 2.3/3.1: 调用 usd() 格式化金额
                    usd(perfData.getAmount()),
                    perfData.getAudience()));
        }

        // 汇总：调用 StatementData 中的查询方法 (totalAmount, volumeCredits)
        result.append(String.format("Amount owed is %s%n",
                usd(data.totalAmount())));
        result.append(String.format("You earned %s credits%n",
                data.volumeCredits()));
        return result.toString();
    }

    /* ===================== Task 2.3 Helper ===================== */

    /**
     * Formats an integer amount (in cents) into a US dollar currency string.
     * @param amount The amount in cents.
     * @return The formatted currency string.
     */
    protected String usd(final int amount) {
        final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        return currency.format(amount / (double) Constants.PERCENT_FACTOR);
    }

    // 3.1: 所有计算 helper (getPlay, getAmount, getVolumeCredits, getTotal...) 已被移除
}
