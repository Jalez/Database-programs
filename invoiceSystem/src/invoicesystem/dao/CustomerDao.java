package invoicesystem.dao;

import java.util.*;
import java.sql.*;
import invoicesystem.model.Customer;
import invoicesystem.DatabaseConnection;


public class CustomerDao implements Dao<Customer, Integer> {
    private DatabaseConnection dbConnection;

    public CustomerDao(DatabaseConnection connection) {
        this.dbConnection = connection;
    }

    public Customer read(Integer key) {
        Connection conn = this.dbConnection.getConnection();
        Customer customer = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM asiakas WHERE asiakas_id = " + key);
            if (rset.next()) {
                customer = new Customer(rset.getInt("asiakas_id"),
                                        rset.getString("asiakas_nimi"),
                                        rset.getString("asiakas_osoite"));
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return customer;
    }

    public void create(Customer customer) {
        Connection conn = this.dbConnection.getConnection();
        // TODO: make sure doesn't exist
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO asiakas VALUES (" +
                               customer.getId() + ", '" +
                               customer.getName() + "', '" +
                               customer.getAddress() + "');");
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public Customer update(Customer customer) {
        // Not implemented
        return null;
    }

    public void delete(Integer key) {
        // Not implemeted
    }

    public List<Customer> list() {
        List<Customer> customers = new ArrayList<Customer>();

        Connection conn = this.dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM asiakas");
            while (rset.next()) {
                Customer customer = new Customer(rset.getInt("asiakas_id"),
                                        rset.getString("asiakas_nimi"),
                                        rset.getString("asiakas_osoite"));
                customers.add(customer);
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return customers;
    }

    public int createNewId() {
        int id = list().size();
        while (true) {
            Customer c = read(id);
            if (c == null) {
                break;
            }
            id++;
        }
        return id;
    }
}