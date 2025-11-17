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
        // 2.4: 移除了 totalAmount 和 volumeCredits 局部变量

        // 细项循环：只负责计算单场金额和拼装字符串
        for (final Performance performance : invoice.getPerformances()) {

            // 2.1: 获取单场金额
            final int thisAmount = getAmount(performance);

            // 2.4: 移除了 volumeCredits 和 totalAmount 的累加操作

            // print line for this order
            result.append(String.format("  %s: %s (%s seats)%n",
                    getPlay(performance).getName(),
                    usd(thisAmount),
                    performance.getAudience()));
        }

        // 汇总：直接调用查询方法 (Replace Temp with Query)
        result.append(String.format("Amount owed is %s%n",
                // 2.4: 调用查询方法
                usd(getTotalAmount())));
        result.append(String.format("You earned %s credits%n",
                // 2.4: 调用查询方法
                getTotalVolumeCredits()));
        return result.toString();
    }

    /* ===================== Task 2.4 Helpers ===================== */

    /**
     * Calculates the total amount owed for all performances in the invoice.
     * @return the total amount
     */
    private int getTotalAmount() {
        int result = 0;
        // 2.4: 实现独立的累加循环
        for (final Performance performance : invoice.getPerformances()) {
            result += getAmount(performance);
        }
        return result;
    }

    /**
     * Calculates the total volume credits earned for all performances in the invoice.
     * @return the total volume credits
     */
    private int getTotalVolumeCredits() {
        int result = 0;
        // 2.4: 实现独立的累加循环
        for (final Performance performance : invoice.getPerformances()) {
            result += getVolumeCredits(performance);
        }
        return result;
    }

    /* ===================== Task 2.3 Helpers ===================== */

    /**
     * Formats an integer amount (in cents) into a US dollar currency string.
     * @param amount The amount in cents.
     * @return The formatted currency string.
     */
    private String usd(final int amount) {
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
