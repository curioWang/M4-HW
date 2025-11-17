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
        final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        int totalAmount = 0;
        int volumeCredits = 0;

        // 细项循环：计算金额、积分并拼装字符串
        for (final Performance performance : invoice.getPerformances()) {

            // 2.1: 内联了 play 和 amount 变量，直接调用 helper 方法
            final int thisAmount = getAmount(performance);

            // add volume credits
            volumeCredits += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
            if (TYPE_COMEDY.equals(getPlay(performance).getType())) {
                volumeCredits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
            }

            // print line for this order
            result.append(String.format("  %s: %s (%s seats)%n",
                    getPlay(performance).getName(),
                    currency.format(thisAmount / (double) Constants.PERCENT_FACTOR),
                    performance.getAudience()));
            totalAmount += thisAmount;
        }

        // 汇总
        result.append(String.format("Amount owed is %s%n",
                currency.format(totalAmount / (double) Constants.PERCENT_FACTOR)));
        result.append(String.format("You earned %s credits%n", volumeCredits));
        return result.toString();
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
     * 2.1: 移除了 Play play 参数，局部变量重命名为 result
     * @param performance the performance
     * @return the calculated amount
     * @throws RuntimeException if the play type is unknown
     */
    private int getAmount(final Performance performance) {
        // 2.1: 在方法内部使用 getPlay(performance) 获取 Play 对象
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
}
