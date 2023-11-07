import java.sql.Connection;
import java.util.Scanner;

public interface Car_Interface {
    void bookCar(Connection connection, Scanner scanner);
    void viewCar(Connection connection, Scanner scanner);
    void addCar(Connection connection, Scanner scanner);
    void removeCar(Connection connection, Scanner scanner);
    void returnCar(Connection connection, Scanner scanner);
    void seeCarDetails(Connection connection,Scanner scanner);
    void seeCustomerDetails(Connection connection,Scanner scanner);
}
