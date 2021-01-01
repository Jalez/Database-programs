package invoicesystem.dao;

import java.util.*;
import java.sql.*;
import invoicesystem.model.*;
import invoicesystem.DatabaseConnection;


public class InvoiceDao implements Dao<Invoice, Integer> {
    private DatabaseConnection dbConnection;

    public InvoiceDao(DatabaseConnection connection) {
        this.dbConnection = connection;
    }

    public Invoice read(Integer key) {
        Connection conn = this.dbConnection.getConnection();
        Invoice invoice = null;

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM lasku " +
                           "INNER JOIN sopimus using (sopimus_id) " +
                           "INNER JOIN työkohde using (kohde_id) " +
                           "INNER JOIN asiakas using (asiakas_id) " +
                           "WHERE lasku.lasku_id = " + key + ";";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                Customer customer = new Customer(rset.getInt("asiakas_id"),
                                                 rset.getString("asiakas_nimi"),
                                                 rset.getString("asiakas_osoite"));

                Location location = new Location(rset.getInt("kohde_id"),
                                                 customer,
                                                 rset.getString("kohteen_osoite"),
                                                 rset.getString("vähennyskelpoisuus"));

                Contract contract = new Contract(rset.getInt("sopimus_id"),
                                                 location,
                                                 rset.getString("sopimus_tyyppi"),
                                                 rset.getInt("sopimus_erät"));

                invoice = new Invoice(rset.getInt("lasku_id"),
                                              contract,
                                              rset.getString("lasku_tyyppi"),
                                              rset.getString("laskun_tila"),
                                              rset.getDate("laskutuspvm"),
                                              rset.getDate("eräpvm"),
                                              rset.getInt("laskutuskerta"),
                                              rset.getInt("laskun_erä"),
                                              rset.getDate("maksupvm"),
                                              rset.getString("maksun_tila"),
                                              rset.getInt("laskutuslisä"),
                                              rset.getInt("viivästyskorko"),
                                              listContractWorks(contract),
                                              listContractProducts(contract));
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return invoice;
    }

    public void create(Invoice invoice) {
        Connection conn = this.dbConnection.getConnection();
        try {
            String query = "INSERT INTO lasku (" +
                           "lasku_id, sopimus_id, lasku_tyyppi, laskun_tila, " +
                           "laskutuspvm, eräpvm, laskutuskerta, laskun_erä, maksupvm, " +
                           "maksun_tila, laskutuslisä, viivästyskorko) VALUES " +
                           "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            pstmt.setInt(1, invoice.getId());
            pstmt.setInt(2, invoice.getContractId());
            pstmt.setString(3, invoice.getInvoiceType());
            pstmt.setString(4, invoice.getInvoiceStatus());
            pstmt.setDate(5, invoice.getBillingDate());
            pstmt.setDate(6, invoice.getDueDate());
            pstmt.setInt(7, invoice.getBillingCount());
            pstmt.setInt(8, invoice.getPaymentPart());
            pstmt.setDate(9, invoice.getPaidDate());
            pstmt.setString(10, invoice.getPaymentStatus());
            pstmt.setInt(11, invoice.getBillingFee());
            pstmt.setInt(12, invoice.getPenaltyInterest());

            pstmt.execute();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    public Invoice update(Invoice invoice) {
        // Not implemented
        return null;
    }

    public void delete(Integer key) {
        // Not implemeted
    }

    public List<Work> listContractWorks(Contract contract) {
        Connection conn = this.dbConnection.getConnection();
        List<Work> workList = new ArrayList<Work>();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM työsuoritus t " +
                           "INNER JOIN työ USING (työ_tyyppi) " +
                           "WHERE t.sopimus_id = " + contract.getId() + ";";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                WorkType wt = new WorkType(rset.getString("työ_tyyppi"),
                                           rset.getInt("tuntihinta"),
                                           rset.getInt("alv"));
                
                Work w = new Work(wt,
                                  contract,
                                  rset.getInt("tunnit"),
                                  rset.getInt("alennus"));
                workList.add(w);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return workList;
    }
        
    public List<BillableProduct> listContractProducts(Contract contract) {
        Connection conn = this.dbConnection.getConnection();
        List<BillableProduct> products = new ArrayList<BillableProduct>();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM tarvikeluettelo t " +
                           "INNER JOIN tarvike USING (tarvike_id) " +
                           "WHERE t.sopimus_id = " + contract.getId() + ";";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                InventoryProduct ip = new InventoryProduct(rset.getInt("tarvike_id"),
                                                           rset.getString("tarvike_nimi"),
                                                           rset.getString("yksikkö"),
                                                           rset.getInt("varastotilanne"),
                                                           rset.getInt("sisäänostohinta"),
                                                           rset.getInt("myyntihinta"),
                                                           rset.getInt("alv"));
                
                BillableProduct p = new BillableProduct(contract,
                                                        ip,
                                                        rset.getInt("lkm"),
                                                        rset.getInt("alennus"));
                products.add(p);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return products;
    }

    public List<Invoice> listInvoicesForContract(Contract c) {
        List<Invoice> invoices = new ArrayList<Invoice>();

        Connection conn = this.dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * from LASKU " +
                           "INNER JOIN sopimus using (sopimus_id) " +
                           "INNER JOIN työkohde using (kohde_id) " +
                           "INNER JOIN asiakas using (asiakas_id) " +
                           "WHERE sopimus.sopimus_id = " + c.getId() + ";";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                Customer customer = new Customer(rset.getInt("asiakas_id"),
                                                 rset.getString("asiakas_nimi"),
                                                 rset.getString("asiakas_osoite"));
                Location location = new Location(rset.getInt("kohde_id"),
                                                 customer,
                                                 rset.getString("kohteen_osoite"),
                                                 rset.getString("vähennyskelpoisuus"));
                Contract contract = new Contract(rset.getInt("sopimus_id"),
                                                 location,
                                                 rset.getString("sopimus_tyyppi"),
                                                 rset.getInt("sopimus_erät"));

                Invoice invoice = new Invoice(rset.getInt("lasku_id"),
                                              contract,
                                              rset.getString("lasku_tyyppi"),
                                              rset.getString("laskun_tila"),
                                              rset.getDate("laskutuspvm"),
                                              rset.getDate("eräpvm"),
                                              rset.getInt("laskutuskerta"),
                                              rset.getInt("laskun_erä"),
                                              rset.getDate("maksupvm"),
                                              rset.getString("maksun_tila"),
                                              rset.getInt("laskutuslisä"),
                                              rset.getInt("viivästyskorko"),
                                              listContractWorks(contract),
                                              listContractProducts(contract));

                invoices.add(invoice);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return invoices;
    }

    public List<Invoice> list() {
        List<Invoice> invoices = new ArrayList<Invoice>();

        Connection conn = this.dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM lasku " +
                           "INNER JOIN sopimus using (sopimus_id) " +
                           "INNER JOIN työkohde using (kohde_id) " +
                           "INNER JOIN asiakas using (asiakas_id);";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                Customer customer = new Customer(rset.getInt("asiakas_id"),
                                                 rset.getString("asiakas_nimi"),
                                                 rset.getString("asiakas_osoite"));
                Location location = new Location(rset.getInt("kohde_id"),
                                                 customer,
                                                 rset.getString("kohteen_osoite"),
                                                 rset.getString("vähennyskelpoisuus"));
                Contract contract = new Contract(rset.getInt("sopimus_id"),
                                                 location,
                                                 rset.getString("sopimus_tyyppi"),
                                                 rset.getInt("sopimus_erät"));

                Invoice invoice = new Invoice(rset.getInt("lasku_id"),
                                              contract,
                                              rset.getString("lasku_tyyppi"),
                                              rset.getString("laskun_tila"),
                                              rset.getDate("laskutuspvm"),
                                              rset.getDate("eräpvm"),
                                              rset.getInt("laskutuskerta"),
                                              rset.getInt("laskun_erä"),
                                              rset.getDate("maksupvm"),
                                              rset.getString("maksun_tila"),
                                              rset.getInt("laskutuslisä"),
                                              rset.getInt("viivästyskorko"),
                                              listContractWorks(contract),
                                              listContractProducts(contract));

                invoices.add(invoice);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return invoices;
    }

    public int createNewId() {
        int id = list().size();
        while (true) {
            Invoice i = read(id);
            if (i == null) {
                break;
            }
            id++;
        }
        return id;
    }
}