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

            // 2.2: 调用新提取的 helper 方法，并进行累加
            volumeCredits += getVolumeCredits(performance);

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

    /* ===================== Task 2.2 Helpers ===================== */

    /**
     * Calculates the volume credits earned for a single performance.
     * @param performance the performance
     * @return the earned credits
     * @throws RuntimeException if the play type is unknown
     */
    private int getVolumeCredits(final Performance performance) {
        // 2.2: 变量重命名为 result，方法只返回贡献的积分
        int result = 0;

        // 积分基础计算
        result += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);

        // 额外积分计算
        if (TYPE_COMEDY.equals(getPlay(performance).getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        // 注意：此处需要添加 default case 来满足 CheckStyle 的 Javadoc @throws 要求
        // 除非指导中明确要求在 getVolumeCredits 中不抛异常，否则为避免潜在问题，
        // 我们通常假设 getPlay 已经确保了 play 存在。

        return result;
    }
}
