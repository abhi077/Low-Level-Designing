import java.util.*;

public class VehicleRentalApplication {

    enum VehicleType { CAR, TRUCK, VAN, MOTORCYCLE }
    enum ReservationStatus { BOOKED, WAITING }
    enum VehicleCondition { SERVICING, ACCIDENT, OIL_CHANGE, OTHER }
    enum PaymentStatus { UNPAID, PENDING, PAID, CANCELLED, FAILED }
    enum AccountStatus { ACTIVE, INACTIVE }
    enum RentalInsurance { MONTH, WEEK, YEAR }
    enum Equipments { CHILD_SEAT, NAVIGATION, SKI_RACK }

    class Address {
        String street, state, country;
        int zipcode;
    }

    abstract class Person {
        String firstName, lastName, email, phoneNo;
        Address address;
    }

    abstract class Account {
        int id;
        String userName;
        AccountStatus accountStatus;
        Person person;
        public abstract boolean resetPassword();
    }

    class Member extends Account {
        int totalBookings;
        Search searchTheVehicle = new VehicleInventory();
        List<VehicleReservation> reservations = new ArrayList<>();
        
        public boolean resetPassword() { return true; }
        public void addReservation(VehicleReservation reservation) {
            reservations.add(reservation);
        }
    }

    class Vehicle {
        String nameOfVehicle, vehicleNumber, barCode;
        VehicleCondition vehicleStatus;
        VehicleType vehicleType;
        
        Vehicle(String name, String number, VehicleType type) {
            this.nameOfVehicle = name;
            this.vehicleNumber = number;
            this.vehicleType = type;
        }
    }

    class VehicleReservation {
        int reservationId;
        Member bookedBy;
        ReservationStatus reservationStatus;
        Date dateOfBooking, dateOfReturning;
        Vehicle vehicle;
        
        VehicleReservation(Member member, Vehicle vehicle) {
            this.reservationId = new Random().nextInt(1000);
            this.bookedBy = member;
            this.vehicle = vehicle;
            this.reservationStatus = ReservationStatus.BOOKED;
            this.dateOfBooking = new Date();
        }
    }

    interface Search {
        List<Vehicle> searchByType(VehicleType type);
    }

    class VehicleInventory implements Search {
        private final HashMap<VehicleType, List<Vehicle>> vehicleTypes = new HashMap<>();

        public VehicleInventory() {
            for (VehicleType type : VehicleType.values()) {
                vehicleTypes.put(type, new ArrayList<>());
            }
        }

        public void addVehicle(Vehicle vehicle) {
            vehicleTypes.get(vehicle.vehicleType).add(vehicle);
        }

        public List<Vehicle> searchByType(VehicleType type) {
            return vehicleTypes.getOrDefault(type, new ArrayList<>());
        }
    }

    public static void main(String[] args) {
        VehicleRentalApplication app = new VehicleRentalApplication();
        VehicleInventory inventory = app.new VehicleInventory();
        
        Vehicle car1 = app.new Vehicle("Toyota Camry", "1234", VehicleType.CAR);
        Vehicle car2 = app.new Vehicle("Honda Accord", "5678", VehicleType.CAR);
        inventory.addVehicle(car1);
        inventory.addVehicle(car2);

        Member member = app.new Member();
        member.userName = "john_doe";

        List<Vehicle> availableCars = inventory.searchByType(VehicleType.CAR);
        System.out.println("Available Cars: ");
        for (Vehicle v : availableCars) {
            System.out.println(v.nameOfVehicle);
        }

        if (!availableCars.isEmpty()) {
            VehicleReservation reservation = app.new VehicleReservation(member, availableCars.get(0));
            member.addReservation(reservation);
            System.out.println("Vehicle " + availableCars.get(0).nameOfVehicle + " booked for " + member.userName);
        }
    }
}
