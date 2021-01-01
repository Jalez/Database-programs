package invoicesystem.model;

import java.util.*;
import invoicesystem.util.*;


public class Invoice {
    private int invoiceId;
    private Contract contract;
    private String invoiceType;
    private String invoiceStatus;
    private java.sql.Date billingDate;
    private java.sql.Date dueDate;
    private int billingCount;
    private int paymentPart;
    private java.sql.Date paidDate;
    private String paymentStatus;
    private int billingFee;
    private int penaltyInterest;

    public List<Work> works;
    private List<BillableProduct> products;


    public Invoice(int invoiceId, Contract contract, String invoiceType, String invoiceStatus,
                   java.sql.Date billingDate, java.sql.Date dueDate, int billingCount, int paymentPart, 
                   java.sql.Date paidDate, String paymentStatus, int billingFee, int penaltyInterest,
                   List<Work> works, List<BillableProduct> products) {
        this.invoiceId = invoiceId;
        this.contract = contract;
        this.invoiceType = invoiceType;
        this.invoiceStatus = invoiceStatus;
        this.billingDate = billingDate;
        this.dueDate = dueDate;
        this.billingCount = billingCount;
        this.paymentPart = paymentPart;
        this.paidDate = paidDate;
        this.paymentStatus = paymentStatus;
        this.billingFee = billingFee;
        this.penaltyInterest = penaltyInterest;
        this.works = works;
        this.products = products;
    }

    public int getId() {
        return this.invoiceId;
    }

    public String getInvoiceType() {
        return this.invoiceType;
    }

    public String getInvoiceStatus() {
        return this.invoiceStatus;
    }

    public java.sql.Date getBillingDate() {
        return this.billingDate;
    }

    public java.sql.Date getDueDate() {
        return this.dueDate;
    }

    public int getBillingCount() {
        return this.billingCount;
    }

    public int getPaymentPart() {
        return this.paymentPart;
    }

    public java.sql.Date getPaidDate() {
        return this.paidDate;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public int getBillingFee() {
        return this.billingFee;
    }

    public int getPenaltyInterest() {
        return this.penaltyInterest;
    }

    public int getContractId() {
        return this.contract.getId();
    }

    public String getInvoiceDetails() {
        ReportFormatter form = new ReportFormatter();
        return form.getCostReport(works, products, this.contract.getHTCEligible());
    }

    @Override
    public String toString() {
        String paymentDateLine = this.paymentStatus.equals("maksettu") ? "Maksupäivä: " + this.paidDate + "\n" : "";
        return "Laskun tunniste: " + this.invoiceId + "\n" +
               "Asiakas: " + this.contract.getCustomerName() + "\n" +
               "Sopimusnumero: " + this.contract.getId() + "\n" +
               "Kotitalousvähennyskelpoinen : " + this.contract.getHTCEligible() + "\n\n" +

               "Maksun tila: " + this.paymentStatus + "\n" +
               paymentDateLine + "\n" +

               "Laskun tyyppi: " + this.invoiceType + "\n" +
               "Laskun tila: " + this.invoiceStatus + "\n" +
               "Laskutuskerta: " + this.billingCount + "\n" +
               "Laskun erä: " + this.paymentPart + "/" + this.contract.getPaymentParts() + "\n" +

               "Laskutuspäivämäärä: " + this.billingDate + "\n" +
               "Eräpäivä: " + this.dueDate + "\n" +
               "Laskuerittely: \n" + getInvoiceDetails();
    }
}
