package invoicesystem.dao;

import java.util.*;
import java.sql.*;
import invoicesystem.model.Work;
import invoicesystem.model.WorkType;
import invoicesystem.model.Contract;
import invoicesystem.DatabaseConnection;


public class WorkDao implements Dao<Work, Integer> {
    private DatabaseConnection dbConnection;

    public WorkDao(DatabaseConnection connection) {
        this.dbConnection = connection;
    }

    public Work read(Integer key) {
        // Not implemented
        return null;
    }

    public void create(Work work) {
        Connection conn = this.dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO työsuoritus VALUES (" +
                               work.getContractId() + ", '" +
                               work.getWorkType().getWorkType() +  "', " +
                               work.getHours() + ");");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public Work update(Work work) {
        // Not implemented
        return null;
    }

    public void delete(Integer key) {
        // Not implemeted
    }

    public List<Work> list() {
        // Not implemented
        return null;
    }

    public List<WorkType> listWorkTypes() {
        List<WorkType> workTypes = new ArrayList<WorkType>();

        Connection conn = this.dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM työ;";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                String workType = rset.getString("työ_tyyppi");
                int hourPrice = rset.getInt("tuntihinta");
                int VAT = rset.getInt("alv");

                WorkType work = new WorkType(workType, hourPrice, VAT);

                workTypes.add(work);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return workTypes;
    }
}