package invoicesystem.model;

import java.util.List;
import invoicesystem.model.Contract;
import invoicesystem.model.InventoryProduct;

public class BillableProduct {
    private Contract contract;
    private InventoryProduct inventoryProduct;
    private int count;
    private int discount;

    public BillableProduct(Contract contract, InventoryProduct ip, int count, int discount) {
        this.contract = contract;
        this.inventoryProduct = ip;
        this.count = count;
        this.discount = discount;
    }

    public int getContractId() {
        return this.contract.getId();
    }

    public int getProductId() {
        return this.inventoryProduct.getId();
    }

    public int getCount() {
        return this.count;
    }

    public int getDiscount() {
        return this.discount;
    }

    public String getName() {
        return this.inventoryProduct.getName();
    }

    public float getPriceWithDiscount() {
        int price = this.inventoryProduct.getSellingPrice() * this.count;
        return price - (price * ((float) discount / 100));
    }

    public float getPriceWithDiscountAndVat() {
        float discountP = getPriceWithDiscount();
        return discountP + (discountP * ((float) this.inventoryProduct.getVat() / 100));
    }

    public String[] getReportLine() {
        String name = getName();
        int unitPrice = this.inventoryProduct.getSellingPrice();
        int count = this.count;
        String unit = this.inventoryProduct.getUnit();
        int totalPrice = unitPrice * count;
        int discount = this.discount;
        float priceWithDiscount = getPriceWithDiscount();
        int vat = this.inventoryProduct.getVat();
        float priceWithVat = getPriceWithDiscountAndVat();

        String[] report = new String[] {name,
                                        String.valueOf(unitPrice),
                                        String.valueOf(count) + " " + unit,
                                        String.valueOf(totalPrice),
                                        String.valueOf(discount),
                                        String.valueOf(priceWithDiscount),
                                        String.valueOf(vat),
                                        String.format("%.2f", priceWithVat)
        };
        return report;
    }
}