import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

// Encapsulation: Private fields with getter methods
class Reservation {
    private static int idCounter = 1;
    private final int reservationId;
    private final String passengerName;
    private final int trainNumber;
    private final String destination;

    Reservation(String passengerName, int trainNumber, String destination) {
        this.reservationId = idCounter++;
        this.passengerName = passengerName;
        this.trainNumber = trainNumber;
        this.destination = destination;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + ", Passenger: " + passengerName +
               ", Train No: " + trainNumber + ", Destination: " + destination;
    }
}

// Abstract class enforcing core functionalities
abstract class TrainService {
    protected HashMap<Integer, Reservation> reservations = new HashMap<>();

    public abstract void addReservation(String passengerName, int trainNumber, String destination);
    public abstract void cancelReservation(int reservationId);
    
    public void displayReservations() {
        if (reservations.isEmpty()) {
            System.out.println("🚫 No current reservations.");
        } else {
            System.out.println("\n=== 🚆 Current Reservations ===");
            for (Reservation reservation : reservations.values()) {
                System.out.println(reservation);
            }
            System.out.println("Total Reservations: " + reservations.size());
        }
    }
}

// Railway reservation system implementation
class RailwayReservationSystem extends TrainService {

    @Override
    public void addReservation(String passengerName, int trainNumber, String destination) {
        if (!isValidName(passengerName) || !isValidName(destination) || trainNumber <= 0 || trainNumber > 9999) {
            System.out.println("❌ Invalid input! Please enter a valid name and train number (1-9999).");
            return;
        }

        // Prevent duplicate reservations
        for (Reservation res : reservations.values()) {
            if (res.getPassengerName().equalsIgnoreCase(passengerName) && res.getTrainNumber() == trainNumber) {
                System.out.println("⚠️ You already have a reservation for Train No. " + trainNumber);
                return;
            }
        }

        Reservation newReservation = new Reservation(passengerName, trainNumber, destination);
        reservations.put(newReservation.getReservationId(), newReservation);
        System.out.println("✅ Reservation confirmed! Your Reservation ID: " + newReservation.getReservationId());
    }

    @Override
    public void cancelReservation(int reservationId) {
        if (reservations.remove(reservationId) != null) {
            System.out.println("✅ Reservation ID " + reservationId + " cancelled successfully!");
        } else {
            System.out.println("❌ No reservation found with ID " + reservationId);
        }
    }

    // Helper method for name validation
    private boolean isValidName(String name) {
        return Pattern.matches("^[a-zA-Z\\s]+$", name);
    }
}

// Special class with extra benefits
class SpecialReservationSystem extends RailwayReservationSystem {
    @Override
    public void addReservation(String passengerName, int trainNumber, String destination) {
        super.addReservation(passengerName, trainNumber, destination);
        System.out.println("🎉 Special reservation confirmed with complimentary meal and preferred seating!");
    }
}

// Main class with user interaction
public class RailwayReservationApp {
    public static void main(String[] args) {
        RailwayReservationSystem system = new SpecialReservationSystem(); // Polymorphism at work!
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Railway Reservation System ===");
            System.out.println("1️⃣ Add Reservation");
            System.out.println("2️⃣ Cancel Reservation");
            System.out.println("3️⃣ Display Reservations");
            System.out.println("4️⃣ Exit");
            System.out.print("Enter your choice: ");
            
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next(); // Consume invalid input
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("👤 Enter passenger name: ");
                    String passengerName = scanner.nextLine().trim();
                    
                    System.out.print("🚆 Enter train number: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a valid train number.");
                        scanner.next();
                    }
                    int trainNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("📍 Enter destination: ");
                    String destination = scanner.nextLine().trim();
                    
                    system.addReservation(passengerName, trainNumber, destination);
                    break;

                case 2:
                    System.out.print("❌ Enter Reservation ID to cancel: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a valid reservation ID.");
                        scanner.next();
                    }
                    int reservationId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    system.cancelReservation(reservationId);
                    break;

                case 3:
                    system.displayReservations();
                    break;

                case 4:
                    System.out.println("🚪 Exiting the system. Thank you!");
                    break;

                default:
                    System.out.println("⚠️ Invalid choice. Please try again.");
            }
        } while (choice != 4);

        scanner.close();
    }
}
