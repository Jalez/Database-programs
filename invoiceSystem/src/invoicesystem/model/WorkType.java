package invoicesystem.model;


public class WorkType {
    private String workType;
    private int hourPrice;
    private int VAT;

    public WorkType(String type, int price, int vat) {
        this.workType = type;
        this.hourPrice = price;
        this.VAT = vat;
    }

    public String getWorkType() {
        return this.workType;
    }

    public int getHourPrice() {
        return this.hourPrice;
    }

    public int getVat() {
        return this.VAT;
    }

    public float getPriceWithVat() {
        return this.hourPrice * (this.VAT/100);
    }
}