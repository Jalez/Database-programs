package invoicesystem.model;


public class Customer {
    private int customerId;
    private String name;
    private String address;

    public Customer(int id, String name, String address) {
        this.customerId = id;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return this.customerId;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return "Asiakastunnus: " + this.customerId + "\n" +
               "Nimi: " + this.name + "\n" +
               "Osoite: " + this.address;
    }
}