package invoicesystem.dao;

import java.util.*;
import java.sql.*;
import invoicesystem.model.*;
import invoicesystem.DatabaseConnection;


public class ContractDao implements Dao<Contract, Integer> {
    private DatabaseConnection dbConnection;

    public ContractDao(DatabaseConnection connection) {
        this.dbConnection = connection;
    }

    public Contract read(Integer key) {
        Connection conn = this.dbConnection.getConnection();
        Contract contract = null;

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM sopimus " +
                           "INNER JOIN työkohde USING (kohde_id) " +
                           "INNER JOIN asiakas USING (asiakas_id) " +
                           "WHERE sopimus.sopimus_id = " + key + ";";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                Customer cust = new Customer(rset.getInt("asiakas_id"),
                                             rset.getString("asiakas_nimi"),
                                             rset.getString("asiakas_osoite"));
                Location loc = new Location(rset.getInt("kohde_id"),
                                            cust,
                                            rset.getString("kohteen_osoite"),
                                            rset.getString("vähennyskelpoisuus"));
                contract = new Contract(rset.getInt("sopimus_id"),
                                        loc,
                                        rset.getString("sopimus_tyyppi"),
                                        rset.getInt("sopimus_erät"));
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return contract;
    }

    public List<Contract> getContractsByLocation(Location location) {
        Connection conn = this.dbConnection.getConnection();
        List<Contract> contracts = new ArrayList<Contract>();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM sopimus WHERE kohde_id = " + location.getId();
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                Contract c = new Contract(rset.getInt("sopimus_id"),
                                          location,
                                          rset.getString("sopimus_tyyppi"),
                                          rset.getInt("sopimus_erät"));
                contracts.add(c);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return contracts;
    }

    public void create(Contract contract) {
        Connection conn = this.dbConnection.getConnection();
        try {
            Statement stmt = conn.createStatement();
            String query = "INSERT INTO sopimus VALUES (" +
                           contract.getId() + ", " +
                           contract.getLocationId() + ", '" +
                           contract.getContractType() + "', " +
                           contract.getPaymentParts() + " );";
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public Contract update(Contract contract) {
        // Not implemented
        return null;
    }

    public void delete(Integer key) {
        // Not implemeted
    }

    public List<Contract> list() {
        List<Contract> contracts = new ArrayList<Contract>();
        Connection conn = this.dbConnection.getConnection();
        String query = "SELECT * " +
                       "FROM sopimus s " + 
                       "INNER JOIN työkohde k USING (kohde_id) " +
                       "INNER JOIN asiakas a USING (asiakas_id);";
        try {
            Statement stmt = conn.createStatement();
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
                
                contracts.add(contract);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }

        return contracts;
    }

    public int createNewId() {
        int id = list().size();
        while (true) {
            Contract c = read(id);
            if (c == null) {
                break;
            }
            id++;
        }
        return id;
    }
}