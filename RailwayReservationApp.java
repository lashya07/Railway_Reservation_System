import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

// =======================
// Reservation Class
// =======================
class Reservation {
    private static int idCounter = 1;
    private final int reservationId;
    private final String passengerName;
    private final int trainNumber;
    private final String destination;

    public Reservation(String passengerName, int trainNumber, String destination) {
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
        return "Reservation ID: " + reservationId +
                ", Passenger: " + passengerName +
                ", Train No: " + trainNumber +
                ", Destination: " + destination;
    }
}

// =======================
// Abstract Service Class
// =======================
abstract class TrainService {
    protected HashMap<Integer, Reservation> reservations = new HashMap<>();

    public abstract boolean addReservation(String passengerName, int trainNumber, String destination);

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

// =======================
// Railway Reservation Implementation
// =======================
class RailwayReservationSystem extends TrainService {

    @Override
    public boolean addReservation(String passengerName, int trainNumber, String destination) {

        if (!isValidName(passengerName) ||
            !isValidName(destination) ||
            trainNumber <= 0 || trainNumber > 9999) {

            System.out.println("❌ Invalid input! Please enter valid details.");
            return false;
        }

        // Check for duplicate booking
        for (Reservation res : reservations.values()) {
            if (res.getPassengerName().equalsIgnoreCase(passengerName)
                    && res.getTrainNumber() == trainNumber) {

                System.out.println("⚠️ Duplicate reservation detected!");
                return false;
            }
        }

        Reservation newReservation =
                new Reservation(passengerName, trainNumber, destination);

        reservations.put(newReservation.getReservationId(), newReservation);

        System.out.println("✅ Reservation confirmed! Your Reservation ID: "
                + newReservation.getReservationId());

        return true;
    }

    @Override
    public void cancelReservation(int reservationId) {
        if (reservations.remove(reservationId) != null) {
            System.out.println("✅ Reservation ID " + reservationId + " cancelled successfully!");
        } else {
            System.out.println("❌ No reservation found with ID " + reservationId);
        }
    }

    private boolean isValidName(String name) {
        return Pattern.matches("^[a-zA-Z\\s]+$", name);
    }
}

// =======================
// Special Reservation (Polymorphism)
// =======================
class SpecialReservationSystem extends RailwayReservationSystem {

    @Override
    public boolean addReservation(String passengerName, int trainNumber, String destination) {
        boolean success = super.addReservation(passengerName, trainNumber, destination);

        if (success) {
            System.out.println("🎉 Special reservation confirmed with complimentary meal!");
        }

        return success;
    }
}

// =======================
// Main Application
// =======================
public class RailwayReservationApp {

    public static void main(String[] args) {

        TrainService system = new SpecialReservationSystem(); // Polymorphism
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            try {
                System.out.println("\n=== Railway Reservation System ===");
                System.out.println("1. Add Reservation");
                System.out.println("2. Cancel Reservation");
                System.out.println("3. Display Reservations");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("⚠️ Please enter a valid number.");
                    scanner.next();
                    continue;
                }

                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {

                    case 1:
                        System.out.print("Enter passenger name: ");
                        String name = scanner.nextLine().trim();

                        System.out.print("Enter train number: ");
                        if (!scanner.hasNextInt()) {
                            System.out.println("Invalid train number!");
                            scanner.next();
                            break;
                        }
                        int trainNumber = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter destination: ");
                        String destination = scanner.nextLine().trim();

                        system.addReservation(name, trainNumber, destination);
                        break;

                    case 2:
                        System.out.print("Enter Reservation ID to cancel: ");
                        if (!scanner.hasNextInt()) {
                            System.out.println("Invalid reservation ID!");
                            scanner.next();
                            break;
                        }
                        int id = scanner.nextInt();
                        scanner.nextLine();

                        system.cancelReservation(id);
                        break;

                    case 3:
                        system.displayReservations();
                        break;

                    case 4:
                        System.out.println("🚪 Exiting system. Thank you!");
                        break;

                    default:
                        System.out.println("⚠️ Invalid choice. Try again.");
                }

            } catch (Exception e) {
                System.out.println("⚠️ Unexpected error occurred.");
                scanner.nextLine(); // clear buffer
            }

        } while (choice != 4);

        scanner.close();
    }
}
