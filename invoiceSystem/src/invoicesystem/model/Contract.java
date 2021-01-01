package invoicesystem.model;

import java.util.*;

public class Contract {
    private int contractId;
    private Location location;
    private String contractType;
    private int paymentParts;

    public Contract(int contractId, Location location, String contractType, int paymentParts) {
        this.contractId = contractId;
        this.location = location;
        this.contractType = contractType;
        this.paymentParts = paymentParts;
    }

    public int getId() {
        return this.contractId;
    }

    public int getLocationId() {
        return this.location.getId();
    }

    public Location getLocation() {
        return this.location;
    }

    public String getContractType() {
        return this.contractType;
    }

    public int getPaymentParts() {
        return this.paymentParts;
    }

    public String getCustomerName() {
        return this.location.getCustomerName();
    }

    public String getHTCEligible() {
        return this.location.getHtcEligible();
    }

    @Override
    public String toString() {
        return "Sopimuksen tunnus: " + this.contractId + "\n" +
               "Sopimustyyppi: " + this.contractType + "\n" +
               "Laskutuserät: " + this.paymentParts + "\n" +
               "\nTyökohde:\n" + this.location.toString();
    }
}