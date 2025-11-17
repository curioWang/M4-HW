package theater;

/**
 * Calculated data for a single performance.
 */
public class PerformanceData {
    // --- play type literals extracted to constants (MultipleStringLiterals) ---
    private static final String TYPE_TRAGEDY = "tragedy";
    private static final String TYPE_COMEDY = "comedy";

    private final String name;
    private final int audience;
    private final int amount;
    private final int volumeCredits;
    // 3.1: 保留原始 Performance 字段供未来使用，如果需要。
    private final Performance performance;
    // 3.1: 保留原始 Play 字段供未来使用。
    private final Play play;

    public PerformanceData(final Performance performance, final Play play) {
        this.performance = performance;
        this.play = play;
        this.name = play.getName();
        this.audience = performance.getAudience();

        // 3.1: 在构造函数中完成计算并存储结果 (amount, volumeCredits)
        this.amount = calculateAmount();
        this.volumeCredits = calculateVolumeCredits();
    }

    /* ===================== Calculation Logic (Moved from StatementPrinter) ===================== */

    /**
     * Calculates the total amount for this performance. (Formerly getAmount in StatementPrinter)
     * @return the calculated amount
     * @throws RuntimeException if the play type is unknown
     */
    private int calculateAmount() {
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

    /**
     * Calculates the volume credits earned for this performance. (Formerly getVolumeCredits in StatementPrinter)
     * @return the earned credits
     */
    private int calculateVolumeCredits() {
        int result = 0;

        result += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);

        if (TYPE_COMEDY.equals(play.getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }

        return result;
    }

    /* ===================== Getters ===================== */

    public String getName() {
        return name;
    }

    public int getAudience() {
        return audience;
    }

    public int getAmount() {
        return amount;
    }

    public int getVolumeCredits() {
        return volumeCredits;
    }
}

