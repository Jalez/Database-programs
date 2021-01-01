package invoicesystem.model;


public class InventoryProduct {
    private int productId;
    private String name;
    private String unit;
    private int stockBalance;
    private int purchasePrice;
    private int sellingPrice;
    private int VAT;

    public InventoryProduct(int id, String name, String unit, int balance,
                   int purchasePrice, int sellingPrice, int VAT) {
        this.productId = id;
        this.name = name;
        this.unit = unit;
        this.stockBalance = balance;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.VAT = VAT;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.productId;
    }

    public int getStock() {
        return this.stockBalance;
    }

    public String getUnit() {
        return this.unit;
    }

    public int getPurchasePrice() {
        return this.purchasePrice;
    }

    public int getSellingPrice() {
        return this.sellingPrice;
    }

    public int getVat() {
        return this.VAT;
    }
}