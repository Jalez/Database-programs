package invoicesystem.model;


public class Location {
    private int locationId;
    private Customer customer;
    private String address;
    private String htcEligible;

    public Location(int locationId, Customer customer, String address, String htc) {
        this.locationId = locationId;
        this.customer = customer;
        this.address = address;
        this.htcEligible = htc;
    }

    public int getId() {
        return this.locationId;
    }

    public String getAddress() {
        return this.address;
    }

    public int getCustomerId() {
        return this.customer.getId();
    }

    public String getHtcEligible() {
        return this.htcEligible;
    }

    public boolean isHtcEligible() {
        return this.htcEligible.toLowerCase() == "k";
    }

    public String getCustomerName() {
        return this.customer.getName();
    }

    @Override
    public String toString() {
        return "Kohteen tunniste: " + this.locationId + "\n" +
               "Asiakas: " + this.customer.getName() + "\n" +
               "Osoite: " + this.address + "\n" +
               "Kotitalousvähennys: " + this.htcEligible;
    }
}