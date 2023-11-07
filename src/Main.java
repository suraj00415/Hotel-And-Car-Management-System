import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DbConnector hotel = new DbConnector();
        DbConnector car = new DbConnector();
        try {
            hotel.dbConnector("hotel_db");
            car.dbConnector("car_db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        while (true) {
            System.out.println("------------------------------Welcome To Booking Portal!------------------------------");
            System.out.println("Please Select any Choice");
            System.out.println("1. Room Booking System");
            System.out.println("2. Car Booking System");
            System.out.println("3. Exit");
            Scanner scanner = new Scanner(System.in);
            int choice1 = scanner.nextInt();
            if (choice1 == 1) {
                while (true) { // Start a loop for the room booking system submenu.
                    System.out.println("-----------------------------------------");
                    System.out.println("HOTEL MANAGEMENT SYSTEM");
                    System.out.println("-----------------------------------------");
                    System.out.println("1. View Available Rooms");
                    System.out.println("2. Book a room");
                    System.out.println("3. Add a Room");
                    System.out.println("4. Delete a Room");
                    System.out.println("5. Checkout");
                    System.out.println("6. See the Room Details");
                    System.out.println("7. See the Customer Details");
                    System.out.println("8. Return to Main Menu"); // Option to return to the main menu.
                    System.out.print("Choose an option: ");
                    // ... (user input for choice)
                    int choice=scanner.nextInt();
                    if (choice == 8) {
                        System.out.println("Returning to the main menu...");
                        break; // Exit the room booking system submenu loop and return to the main menu options.
                    }

                    Hotel_Reservation h = new Hotel_Reservation();
                    switch (choice) {
                        case 1 -> h.viewReservation(hotel.connection, scanner);
                        case 2 -> h.createReservation(hotel.connection, scanner);
                        case 3 -> h.addRoom(hotel.connection, scanner);
                        case 4 -> h.removeRoom(hotel.connection, scanner);
                        case 5 -> h.checkout(hotel.connection, scanner);
                        case 6 -> h.seeRoomDetails(hotel.connection, scanner);
                        case 7 -> h.seeCustomerDetails(hotel.connection, scanner);
                        default -> System.out.println("Invalid choice. Try again.");
                    }
                }
            }

            else if (choice1 == 2) {
                while (true) { // Start a loop for the car booking system submenu.
                    System.out.println("-----------------------------------------");
                    System.out.println("CAR BOOKING SYSTEM");
                    System.out.println("-----------------------------------------");
                    System.out.println("1. View Available Cars");
                    System.out.println("2. Book a Car");
                    System.out.println("3. Add a Car");
                    System.out.println("4. Delete a Car");
                    System.out.println("5. Return the Car");
                    System.out.println("6. See the Car Details");
                    System.out.println("7. See the Customer Details");
                    System.out.println("8. Return to Main Menu"); // Option to return to the main menu.
                    System.out.print("Choose an option: ");
                    // ... (user input for choice)
                    int choice=scanner.nextInt();
                    if (choice == 8) {
                        System.out.println("Returning to the main menu...");
                        break; // Exit the car booking system submenu loop and return to the main menu options.
                    }

                    Car_Rentals c = new Car_Rentals();
                    switch (choice) {
                        case 1 -> c.viewCar(car.connection, scanner);
                        case 2 -> c.bookCar(car.connection, scanner);
                        case 3 -> c.addCar(car.connection, scanner);
                        case 4 -> c.removeCar(car.connection, scanner);
                        case 5 -> c.returnCar(car.connection, scanner);
                        case 6 -> c.seeCarDetails(car.connection, scanner);
                        case 7 -> c.seeCustomerDetails(car.connection, scanner);
                        default -> System.out.println("Invalid choice. Try again.");
                    }
                }
            }

            else if (choice1 == 3) {
                System.out.println("Exiting...");
                return;
            }

            else {
                System.out.println("Choose Valid Options!");
            }
        }
    }
}