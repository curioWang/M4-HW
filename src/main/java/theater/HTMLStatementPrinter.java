package theater;

import java.util.Map;

/**
 * Statement printer that renders the invoice as HTML instead of plain text.
 */
public class HTMLStatementPrinter extends StatementPrinter {

    /**
     * Creates an HTML statement printer for the given invoice and plays.
     *
     * @param invoice the invoice to print
     * @param plays   the mapping from play id to play
     */
    public HTMLStatementPrinter(final Invoice invoice, final Map<String, Play> plays) {
        super(invoice, plays);
    }

    /**
     * Returns the HTML representation of this printer's invoice.
     *
     * @return the formatted HTML statement
     */
    @Override
    public String statement() {
        final StatementData data = getStatementData();

        final StringBuilder result = new StringBuilder(
                String.format("<h1>Statement for %s</h1>%n", data.getCustomer()));

        result.append("<table>").append(System.lineSeparator());
        result.append(String.format(" <caption>Statement for %s</caption>%n",
                data.getCustomer()));
        result.append(" <tr><th>play</th><th>seats</th><th>cost</th></tr>")
                .append(System.lineSeparator());

        for (PerformanceData perfData : data.getPerformances()) {
            result.append(String.format(
                    " <tr><td>%s</td><td>%s</td><td>%s</td></tr>%n",
                    perfData.getName(),
                    perfData.getAudience(),
                    usd(perfData.getAmount())));
        }
        result.append("</table>").append(System.lineSeparator());

        result.append(String.format("<p>Amount owed is <em>%s</em></p>%n",
                usd(data.totalAmount())));
        result.append(String.format("<p>You earned <em>%s</em> credits</p>%n",
                data.volumeCredits()));

        return result.toString();
    }
}

