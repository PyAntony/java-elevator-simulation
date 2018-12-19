package classes;

import java.util.*;
import java.util.stream.Collectors;


/**
 * An Elevator object represents an elevator in
 * a building.
 */
public class Elevator {

    private static int CAPACITY;
    private int currentFloor;
    private String direction;
    // to keep track of passengers in elevator
    private List<Passenger> passengerList = new ArrayList<>();
    private Building building;

    /**
     * Constructor for class Elevator.
     */
    Elevator(Building building, int elevatorCapacity) {

        CAPACITY = elevatorCapacity;
        this.building = building;
        this.currentFloor = 1;
        this.direction = "up";
    }

    /**
     * moves the elevator 1 floor. Steps:
     * - adjust the elevator direction if it is in edge floors.
     * - increment/decrement current floor.
     * - release passengers if current floor equals destination floor.
     * - board waiting passengers from appropriate queue.
     */
    void move() {

        direction = adjustDirection(direction);

        if (direction.equals("up")) ++currentFloor;
        else --currentFloor;

        for (Passenger user : getPassengersToRelease()) {
            releasePassenger(user);
        }

        getPassengersIn();
    }

    /**
     * Boards passenger if elevator capacity is not full.
     */
    private void boardPassenger(Passenger user) throws ElevatorFullException {

        if (passengerList.size() >= CAPACITY) {
            throw new ElevatorFullException("Elevator is full!.");
        }
        else passengerList.add(user);
    }

    /**
     * Performs full boarding sequence:
     * - board passenger.
     * - change passenger fields by calling its method "boardElevator".
     * - decrement the floor queue.
     */
    private void boardIfNotFull(Passenger user)
            throws ElevatorFullException {

        boardPassenger(user);
        user.boardElevator();
        building.getFloor(currentFloor)
                .decrementQueue(adjustDirection(direction));
    }

    /**
     * Calls the first person in queue without removing it. Person
     * can't board until elevator capacity is checked.
     */
    private Passenger callFirstInQueue() {
        return building.getFloor(currentFloor)
                .getFirstPassenger(adjustDirection(direction));
    }

    /**
     * Checks if somebody is waiting to board elevator in current floor.
     */
    private Boolean somebodyIsWaiting() {
        return !building.getFloor(currentFloor)
                .queueIsEmpty(adjustDirection(direction));
    }

    /**
     * Performs full boarding cycle by checking if somebody is waiting
     * and catching the full capacity exception.
     */
    void getPassengersIn() {

        while (somebodyIsWaiting()) {

            try {
                boardIfNotFull(callFirstInQueue());

            } catch (ElevatorFullException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    /**
     * Return a list of passengers to be released.
     */
    private List<Passenger> getPassengersToRelease() {

        return passengerList.stream()
                .filter(user -> user.getDestinationFloor() == currentFloor)
                .collect(Collectors.toList());
    }

    /**
     * Release passenger. Steps:
     * - Mark passenger arrival.
     * - Enter current floor as a resident.
     * - Removes passenger from elevator list.
     */
    private void releasePassenger(Passenger user) {

        user.arrive();
        building.getFloor(currentFloor).enterGroundFloor(user);
        passengerList.remove(user);
    }

    private String adjustDirection(String direction) {

        return (currentFloor == Building.FLOORS)
                ? "down"
                : ((currentFloor == 1) ? "up" : direction);
    }

    public String toString() {

        return "Floor " + currentFloor + ": " + passengerList.size() +
                ((passengerList.size() == 1) ? " passenger" : " passengers");
    }

    boolean goingUp() {
        return direction.equals("up");
    }

    boolean goingDown() {
        return direction.equals("down");
    }

    int getCurrentFloor() {
        return currentFloor;
    }

    List<Passenger> getPassengers() {
        return passengerList;
    }
}




