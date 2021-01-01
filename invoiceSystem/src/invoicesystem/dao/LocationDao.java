package invoicesystem.dao;

import java.util.*;
import java.sql.*;
import invoicesystem.model.Location;
import invoicesystem.model.Customer;
import invoicesystem.DatabaseConnection;


public class LocationDao implements Dao<Location, Integer> {
    private DatabaseConnection dbConnection;

    public LocationDao(DatabaseConnection connection) {
        this.dbConnection = connection;
    }

    public Location read(Integer key) {
        Connection conn = this.dbConnection.getConnection();
        Location location = null;

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT k.kohde_id, k.kohteen_osoite, k.vähennyskelpoisuus, " +
                           "a. asiakas_id, a.asiakas_nimi, a.asiakas_osoite "+
                           "FROM työkohde AS k, asiakas AS a " +
                           "WHERE k.kohde_id = " + key +
                           " AND k.asiakas_id = a.asiakas_id;";
            ResultSet rset = stmt.executeQuery(query);

            if (rset.next()) {
                int locationId = rset.getInt("kohde_id");
                String locationAddress = rset.getString("kohteen_osoite");
                String htc = rset.getString("vähennyskelpoisuus");
                int customerId = rset.getInt("asiakas_id");
                String customerName = rset.getString("asiakas_nimi");
                String customerAddress = rset.getString("asiakas_osoite");
                
                Customer customer = new Customer(customerId, customerName, customerAddress);
                location = new Location(locationId, customer, locationAddress, htc);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return location;
    }

    public void create(Location location) {
        Connection conn = this.dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            String query = "INSERT INTO työkohde VALUES (" +
                           location.getId() + ", '" +
                           location.getAddress() + "', " +
                           location.getCustomerId() + ", '" +
                           location.getHtcEligible() + "');";
            stmt.executeUpdate(query);
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    public Location update(Location location) {
        // Not implemented
        return null;
    }

    public void delete(Integer key) {
        // Not implemeted
    }

    public List<Location> list() {
        List<Location> locations = new ArrayList<Location>();

        Connection conn = this.dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT k.kohde_id, k.kohteen_osoite, k.vähennyskelpoisuus, " +
                           "a.asiakas_id, a.asiakas_nimi, a.asiakas_osoite "+
                           "FROM työkohde AS k, asiakas AS a " +
                           "WHERE k.asiakas_id = a.asiakas_id;";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                int locationId = rset.getInt("kohde_id");
                String locationAddress = rset.getString("kohteen_osoite");
                String htc = rset.getString("vähennyskelpoisuus");
                int customerId = rset.getInt("asiakas_id");
                String customerName = rset.getString("asiakas_nimi");
                String customerAddress = rset.getString("asiakas_osoite");

                Customer customer = new Customer(customerId, customerName, customerAddress);
                Location location = new Location(locationId, customer, locationAddress, htc);

                locations.add(location);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return locations;
    }

    public int createNewId() {
        int id = list().size();
        while (true) {
            Location l = read(id);
            if (l == null) {
                break;
            }
            id++;
        }
        return id;
    }
}