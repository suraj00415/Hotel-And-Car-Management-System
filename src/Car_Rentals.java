import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Car_Rentals implements Car_Interface {
    @Override
    public void bookCar(Connection connection, Scanner scanner) {
//        Checking For Existing Customer or Not
        Customers c = new Customers();
        System.out.println("Select Any One Option:");
        System.out.println("1.New Customer");
        System.out.println("2.Existing Customer");
        int choice = scanner.nextInt();
        int custId = -1;
        if (choice == 1) {
            custId = c.createCustomer(connection, scanner);
        } else if (choice == 2) {
            try {
                custId = c.existingCustomer(connection, scanner);
            } catch (RuntimeException e) {
                return;
            }
        } else {
            System.out.println("Invalid Option");
            return;
        }
        System.out.println("Customer Id:" + custId);
//       Finding Customer Name & Id:
        String custNameFind = String.format("Select * from customer WHERE custId=%d;", custId);
        String custName = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(custNameFind);
            if (resultSet.next()) {
                custName = resultSet.getString("custName");
            } else {
                System.out.println("No such Name exisits");
            }
            System.out.println("Customer Name: " + custName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        Viewing Available Cars
        viewCar(connection, scanner);
//        Entering The details of the car
        System.out.println("Enter the Name of Car:");
        String carName = scanner.next();
        System.out.println("Enter the No. of Days You Want to Rent:");
        int days = scanner.nextInt();
        String query = String.format("Select * from car_booking WHERE car_name='%s';", carName);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int pricePerDay = resultSet.getInt("car_price");
                int price = pricePerDay * days;
                int carId = resultSet.getInt("carId");
                String query1 = String.format("UPDATE car_booking SET isReserved=%d,customersId=%d,cust_name ='%s',reserved_time=current_time,days=%d WHERE car_name='%s';", 1, custId, custName, days, carName);
                String query2 = String.format("UPDATE customer SET cust_car_id=%d ,total_price=%d, car_name='%s' ,car_days=%d WHERE custId=%d;", carId, price, carName, days, custId);
                try {
                    int result = statement.executeUpdate(query1);
                    statement.executeUpdate(query2);
                    if (result > 0) {
                        System.out.println("Booked Successfully");
                        System.out.println("You Need To Pay :" + price + " Rs");
                    } else {
                        System.out.println("Booking Failed!");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Car Does Not Exists!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void viewCar(Connection connection, Scanner scanner) {
        String carAvailable = "Select * from car_booking WHERE isReserved=0;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(carAvailable);
            System.out.println("Available Cars :");
            System.out.println("+----------------+-----------------+---------------+--------------------------+");
            System.out.println("|        ID       | Car Name       | isReserved    | Price Per Day            |");
            System.out.println("+----------------+-----------------+---------------+--------------------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("carId");
                String carName = resultSet.getString("car_name");
                int isReserved = resultSet.getInt("isReserved");
                int ppd = resultSet.getInt("car_price");
                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-24s |\n",
                        id, carName, isReserved, ppd);
            }

            System.out.println("+----------------+-----------------+---------------+--------------------------+");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addCar(Connection connection, Scanner scanner) {
        System.out.println("Enter The Name of Car");
        String carName = scanner.next();
        System.out.println("Enter The Price of Car Per Day:");
        int carPrice = scanner.nextInt();
        String query = String.format("INSERT INTO car_booking (car_name,car_price) VALUES('%s',%d)", carName, carPrice);
        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if (rows > 0) {
                System.out.println("Car Added Successfully");
            } else {
                System.out.println("Failed To Add Car!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeCar(Connection connection, Scanner scanner) {
        System.out.println("Enter The Name of Car");
        String carName = scanner.next();
        String query = String.format("DELETE FROM car_booking WHERE car_name='%s'", carName);
        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if (rows > 0) {
                System.out.println("Car Deleted Successfully");
            } else {
                System.out.println("Car Does Not Exists!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void returnCar(Connection connection, Scanner scanner) {
        Customers c = new Customers();
        int custId = -1;
        try {
            custId = c.existingCustomer(connection, scanner);
            System.out.println("Enter the Name of Car:");
            String carName = scanner.next();
            String query = String.format("Select * from car_booking WHERE car_name='%s';", carName);
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    int customersId = resultSet.getInt("customersId");
                    if (customersId == custId) {
                        String query1 = String.format("UPDATE car_booking SET isReserved=%d,customersId=%d ,cust_name=NULL, reserved_time=NULL,days=%d WHERE car_name='%s';", 0, -1, 0, carName);
                        String query2 = String.format("UPDATE customer SET cust_car_id=%d ,car_name=NULL ,total_price=%d ,car_days=%d WHERE custId=%d;", -1, 0, 0, custId);
                        try {
                            int result = statement.executeUpdate(query1);
                            statement.executeUpdate(query2);
                            if (result > 0) {
                                System.out.println("Returned Successfully");
                            } else {
                                System.out.println("Returned Failed!");
                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Customer Does Not Have " + carName);
                    }
                } else {
                    System.out.println("Car Does Not Exists!");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (RuntimeException e) {
            return;
        }

    }

    @Override
    public void seeCarDetails(Connection connection, Scanner scanner) {
        String carAvailable = "Select * from car_booking;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(carAvailable);
            System.out.println("Car Details :");
            System.out.println("+----------------+-----------------+----------------+-------------------------+----------------------+--------+------------------+");
            System.out.println("|        ID       | Car Name       |  isReserved    | Reservation Date        | Customer Name        | Days   | Price Per Day    |");
            System.out.println("+----------------+-----------------+----------------+-------------------------+----------------------+--------+------------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("carId");
                String carName = resultSet.getString("car_name");
                int isReserved = resultSet.getInt("isReserved");
                String reservationDate;
                if (resultSet.getTimestamp("reserved_time") == null) {
                    reservationDate = "N/A";
                } else {
                    reservationDate = resultSet.getTimestamp("reserved_time").toString();
                }
                String custName = resultSet.getString("cust_name");
                int ppd = resultSet.getInt("car_price");
                int days = resultSet.getInt("days");
                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-14d | %-23s | %-20s | %-6d | %-16d |\n",
                        id, carName, isReserved, reservationDate, custName, days, ppd);
            }

            System.out.println("+----------------+-----------------+----------------+-------------------------+----------------------+--------+------------------+");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void seeCustomerDetails(Connection connection, Scanner scanner) {
        String carAvailable = "Select * from customer;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(carAvailable);
            System.out.println("Customer Details :");
            System.out.println("+----------------+-----------------+--------------------------+----------------+---------------------+--------+");
            System.out.println("|        ID       | Customer Name  | Email                    | Car Name       | Total Price         | Days   |");
            System.out.println("+----------------+-----------------+--------------------------+----------------+---------------------+--------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("custId");
                String cust_Name = resultSet.getString("custName");
                String email = resultSet.getString("email");
                String carName = resultSet.getString("car_name");
                int totalPrice = resultSet.getInt("total_price");
                int days = resultSet.getInt("car_days");
                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-24s | %-14s | %-19d | %-6d |\n",
                        id, cust_Name, email, carName, totalPrice, days);
            }
            System.out.println("+----------------+-----------------+--------------------------+----------------+---------------------+--------+");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
