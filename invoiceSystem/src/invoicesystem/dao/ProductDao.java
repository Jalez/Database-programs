package invoicesystem.dao;

import java.util.*;
import java.sql.*;
import invoicesystem.model.BillableProduct;
import invoicesystem.model.InventoryProduct;
import invoicesystem.model.Contract;
import invoicesystem.DatabaseConnection;

public class ProductDao implements Dao<BillableProduct, Integer> {
    private DatabaseConnection dbConnection;

    public ProductDao(DatabaseConnection connection) {
        this.dbConnection = connection;
    }

    public BillableProduct read(Integer key) {
        // Not implemented
        return null;
    }

    public void create(BillableProduct product) {
        Connection conn = this.dbConnection.getConnection();
    
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO tarvikeluettelo VALUES (" +
                               product.getContractId() + ", " +
                               product.getProductId() +  ", " +
                               product.getCount() + ", " +
                               product.getDiscount() + ");");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public BillableProduct update(BillableProduct product) {
        // Not implemented
        return null;
    }

    public void delete(Integer key) {
        // Not implemeted
    }

    public List<BillableProduct> list() {
        // Not implemented
        return null;
    }

    public List<InventoryProduct> listProductInventory() {
        List<InventoryProduct> products = new ArrayList<InventoryProduct>();
        Connection conn = this.dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM tarvike;";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                int prodId = rset.getInt("tarvike_id");
                String prodName = rset.getString("tarvike_nimi");
                String prodUnit = rset.getString("yksikkö");
                int prodStock = rset.getInt("varastotilanne");
                int buyPrice = rset.getInt("sisäänostohinta");
                int sellPrice = rset.getInt("myyntihinta");
                int vat = rset.getInt("alv");

                InventoryProduct product = new InventoryProduct(
                    prodId, prodName, prodUnit, prodStock, buyPrice, sellPrice, vat
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return products;
    }
}