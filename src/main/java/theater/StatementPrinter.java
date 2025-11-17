package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Generates a plain-text statement for an invoice.
 */
public class StatementPrinter {
    // --- play type literals extracted to constants (MultipleStringLiterals) ---
    private static final String TYPE_TRAGEDY = "tragedy";
    private static final String TYPE_COMEDY = "comedy";

    private final Invoice invoice;
    private final Map<String, Play> plays;

    public StatementPrinter(final Invoice invoice, final Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     *
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    public String statement() {
        final StringBuilder result =
                new StringBuilder("Statement for " + invoice.getCustomer() + System.lineSeparator());
        // 2.3: 移除了 NumberFormat currency 局部变量

        int totalAmount = 0;
        int volumeCredits = 0;

        // 细项循环：计算金额、积分并拼装字符串
        for (final Performance performance : invoice.getPerformances()) {

            // 2.1: 内联了 play 和 amount 变量，直接调用 helper 方法
            final int thisAmount = getAmount(performance);

            // 2.2: 调用新提取的 helper 方法，并进行累加
            volumeCredits += getVolumeCredits(performance);

            // print line for this order
            result.append(String.format("  %s: %s (%s seats)%n",
                    getPlay(performance).getName(),
                    // 2.3: 调用新的 usd() 辅助方法
                    usd(thisAmount),
                    performance.getAudience()));
            totalAmount += thisAmount;
        }

        // 汇总
        result.append(String.format("Amount owed is %s%n",
                // 2.3: 调用新的 usd() 辅助方法
                usd(totalAmount)));
        result.append(String.format("You earned %s credits%n", volumeCredits));
        return result.toString();
    }

    /* ===================== Task 2.3 Helpers ===================== */

    /**
     * Formats an integer amount (in cents) into a US dollar currency string.
     * 2.3: 提取此方法以封装货币格式化逻辑
     * @param amount The amount in cents.
     * @return The formatted currency string.
     */
    private String usd(final int amount) {
        // 2.3: 在方法内部创建 NumberFormat 实例
        final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        return currency.format(amount / (double) Constants.PERCENT_FACTOR);
    }

    /* ===================== Task 2.1 Helpers ===================== */

    /**
     * Extracts the Play object associated with a Performance.
     * @param performance the performance
     * @return the associated play
     */
    private Play getPlay(final Performance performance) {
        return plays.get(performance.getPlayID());
    }

    /**
     * Calculates the total amount for a given performance.
     * @param performance the performance
     * @return the calculated amount
     * @throws RuntimeException if the play type is unknown
     */
    private int getAmount(final Performance performance) {
        final Play play = getPlay(performance);
        int result;

        switch (play.getType()) {
            case TYPE_TRAGEDY:
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;

            case TYPE_COMEDY:
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;

            default:
                throw new RuntimeException(String.format("unknown type: %s", play.getType()));
        }
        return result;
    }

    /* ===================== Task 2.2 Helpers ===================== */

    /**
     * Calculates the volume credits earned for a single performance.
     * @param performance the performance
     * @return the earned credits
     * @throws RuntimeException if the play type is unknown
     */
    private int getVolumeCredits(final Performance performance) {
        int result = 0;

        result += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);

        if (TYPE_COMEDY.equals(getPlay(performance).getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        return result;
    }
}
