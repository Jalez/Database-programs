/*
The program will connect to a postgresql database on dbstud2.sis.uta.fi server. 
To run:
java -classpath {path-to-postgres-driver}:. com.invoicesystem.Main {db-name} {db-username} {db-password}
*/
package invoicesystem;

import java.util.Scanner;
import invoicesystem.ui.UserInterface;

public class Main {
    public static void main(String[] args)  { // TODO: If args < 3, send halp
        String database = args[0];
        String user = args[1];
        String password = args[2];

        DatabaseConnection connection = new DatabaseConnection(database, user, password);
        UserInterface ui = new UserInterface(connection);
        Scanner scanner = new Scanner(System.in);
        ui.run(scanner);
    }
}