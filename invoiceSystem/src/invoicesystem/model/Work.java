package invoicesystem.model;


public class Work {
    private WorkType workType;
    private Contract contract;
    private int hours;
    private int discount;

    public Work(WorkType type, Contract contract, int hours, int discount) {
        this.workType = type;
        this.contract = contract;
        this.hours = hours;
        this.discount = discount;
    }

    public WorkType getWorkType() {
        return this.workType;
    }

    public int getContractId() {
        return this.contract.getId();
    }

    public boolean isHtcEligible() {
        System.out.println("HTCHTCHTC::::" +this.contract.getHTCEligible().toLowerCase());
        return this.contract.getHTCEligible().toLowerCase() == "k";
    }

    public int getHours() {
        return this.hours;
    }

    public float getPriceWithDiscount() {
        int price = this.workType.getHourPrice() * this.hours;
        return price - (price * ((float) discount / 100));
    }

    public float getPriceWithDiscountAndVat() {
        float discountPrice = getPriceWithDiscount();
        return discountPrice + (discountPrice * ((float) this.workType.getVat() / 100));
    }

    public String[] getReportLine() {
        String wt = this.workType.getWorkType();
        int hourPrice = this.workType.getHourPrice();
        int hours = this.hours;
        int price = hourPrice * hours;
        int discount = this.discount;
        float priceWithDiscount = getPriceWithDiscount();
        int vat = this.workType.getVat();
        float priceWithVat = getPriceWithDiscountAndVat();

        String[] report = new String[] {wt,
                                        String.valueOf(hourPrice),
                                        String.valueOf(hours),
                                        String.valueOf(price),
                                        String.valueOf(discount),
                                        String.valueOf(priceWithDiscount),
                                        String.valueOf(vat),
                                        String.format("%.2f", priceWithVat)
                                    };
        return report;
    }

    @Override
    public String toString() {
        return "Työ: " + this.workType.getWorkType() + "\n" +
               "Sopimusnumero: " + this.contract.getId() + "\n" +
               "Tunteja: " + this.hours + "\n" +
               "Alennus: " + this.discount;
    }
}