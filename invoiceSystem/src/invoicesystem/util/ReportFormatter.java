package invoicesystem.util;

import invoicesystem.model.*;
import java.util.*;

public class ReportFormatter {
    public ReportFormatter() {

    }

    public String getCostReport(List<Work> works, List<BillableProduct> products, String htcElig) {
        String report = "";
        float total = 0;
        report += getWorkReport(works, htcElig);
        report += getProductReport(products);
        report += getLongDivider() + "\n";
        report += String.format("%147s", "Yhteensä");
        for (Work work : works) {
            total += work.getPriceWithDiscountAndVat();
        }
        for (BillableProduct prod : products) {
            total += prod.getPriceWithDiscountAndVat();
        }
        report += String.format("%23s", String.format("%.2f", total)) + "\n";
        report += getLongDivider() + "\n\n";
        return report;
    }

    public String getProductReport(List<BillableProduct> products) {
        int tableLen = products.size() + 2;
        Object[][] productReport = new String[tableLen][];
        String reportStr = "\nTarvikkeet:\n";
        reportStr += getLongDivider() + "\n";

        productReport[0] = new String[] {"Tarvike", "Kpl-hinta (alv 0%)", "Määrä", "Hinta yht (alv 0%)",
                                         "Alennus (%)", "Hinta yht alennuksella",
                                         "ALV (%)", "Maksettavaa"};
        productReport[1] = new String[] {"", "", "", "", "", "", "", ""};
    
        int i = 2;
        float totalCost = 0;
        for (BillableProduct product: products) {
            productReport[i] = product.getReportLine();
            totalCost += product.getPriceWithDiscountAndVat();
            i++;
        }
        for (Object[] row : productReport) {
            reportStr += String.format("%24s%23s%12s%23s%18s%32s%15s%23s\n", row);
        }
        reportStr += "\n" + String.format("%170s", getShortDivider()) + "\n";
        reportStr += String.format("%147s", "Tarvikkeet yhteensä");
        reportStr += String.format("%23s", String.format("%.2f", totalCost)) + "\n\n";
        
        return reportStr;
    }

    public String getWorkReport(List<Work> works, String htcElig) {
        int tableLen = works.size() + 2;
        Object[][] workReport = new String[tableLen][];
        String reportStr = "\nTyöt:\n";
        reportStr += getLongDivider() + "\n";

        workReport[0] = new String[] {"Työtyyppi", "Tuntihinta (alv 0%)", "Tunteja",
                                      "Hinta yht (alv 0%)", "Alennus (%)", "Hinta yht alennuksella",
                                      "ALV (%)", "Maksettavaa"};
        workReport[1] = new String[] {"", "", "", "", "", "", "", ""};
        int i = 2;
        float totalCost = 0;
        for (Work work : works) {
            workReport[i] = work.getReportLine();
            totalCost += work.getPriceWithDiscountAndVat();
            i++;
        }

        for (Object[] row : workReport) {
            reportStr += String.format("%24s%23s%12s%23s%18s%32s%15s%23s\n", row);
        }
        reportStr += "\n" + String.format("%170s", getShortDivider()) + "\n";
        reportStr += String.format("%147s", "Työ yhteensä");
        reportStr += String.format("%23s", String.format("%.2f", totalCost)) + "\n\n";

        float htcEligAmount = (totalCost/2 > 2400) ? 2400 : totalCost / 2;
        if (htcElig.equals("k") || htcElig.equals("K")) {
            reportStr += "\n" + String.format("%170s\n", "Kotitalousvähennykseen oikeuttava summa:");
            reportStr += String.format("%170s\n\n", String.format("%.2f", htcEligAmount));
        } else {
            reportStr += "\n" + String.format("%170s\n\n", "Kohde ei kotitalousvähennyskelpoinen.");
        }
        return reportStr;
    }

    public String getLongDivider() {
        String div = new String(new char[170]).replace("\0", "-");
        return div;
    }

    public String getShortDivider() {
        String div = new String(new char[35]).replace("\0", "-");
        return div;
    }
}
