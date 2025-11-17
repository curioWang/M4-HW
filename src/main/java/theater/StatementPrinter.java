package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Generates a plain-text statement for an invoice.
 * <p>
 * All calculation work is delegated to {@link StatementData};
 * this class is responsible only for rendering the result as text.
 * </p>
 */
public class StatementPrinter {

    /** Prepared data for this invoice statement. */
    private final StatementData statementData;

    /**
     * Creates a printer for the given invoice and plays.
     *
     * @param invoice the invoice to print
     * @param plays   the mapping from play id to play
     */
    public StatementPrinter(final Invoice invoice, final Map<String, Play> plays) {
        this.statementData = new StatementData(invoice, plays);
    }

    /**
     * Returns the plain-text statement for this printer's invoice.
     *
     * @return the formatted statement
     */
    public String statement() {
        return renderPlainText(statementData);
    }

    /**
     * Renders the given statement data as a plain-text invoice.
     *
     * @param data pre-computed statement data
     * @return the formatted invoice
     */
    private String renderPlainText(final StatementData data) {
        final StringBuilder result = new StringBuilder(
                String.format("Statement for %s%n", data.getCustomer()));

        // line items
        for (PerformanceData perfData : data.getPerformances()) {
            result.append(String.format("  %s: %s (%s seats)%n",
                    perfData.getName(),
                    usd(perfData.getAmount()),
                    perfData.getAudience()));
        }

        // totals
        result.append(String.format("Amount owed is %s%n", usd(data.totalAmount())));
        result.append(String.format("You earned %s credits%n", data.volumeCredits()));

        return result.toString();
    }

    /**
     * Formats an integer amount (in cents) into a US dollar currency string.
     *
     * @param amount the amount in cents
     * @return the formatted currency string
     */
    protected String usd(final int amount) {
        final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        return currency.format(amount / (double) Constants.PERCENT_FACTOR);
    }

    /**
     * Exposes the calculated statement data to subclasses such as
     * {@link HTMLStatementPrinter}.
     *
     * @return the prepared {@link StatementData}
     */
    protected StatementData getStatementData() {
        return statementData;
    }
}

