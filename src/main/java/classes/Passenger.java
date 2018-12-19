package classes;


/**
 * A Passenger object represents a person to ride the Elevator.
 * a passengers has a name, knows its current Floor and its destination Floor.
 */
class Passenger {

    // Resident has current floor and undefined destination number.
    // Passenger in elevator has destination number and undefined current floor.
    // Passenger waiting for elevator has current floor number and destination number.
    // "undefined" will be indicated with "0".

    private static final String UND_FLAG = "UNDEFINED_FLOOR -> 0";

    private int current_floor;
    private int destination_floor;
    private String name;

    /**
     * Constructor for class Passenger
     */
    Passenger(String name) {

        this.name = name;
        this.current_floor = 1;
        this.destination_floor = 0;
    }

    void waitForElevator(int newDestinationFloor) {
        destination_floor = newDestinationFloor;
    }

    void boardElevator() {
        current_floor = 0;
    }

    void arrive() {
        current_floor = destination_floor;
        destination_floor = 0;
    }

    public String toString() {

        return "current floor: " +
                ((current_floor == 0) ? UND_FLAG : current_floor) +
                "destination floor: " +
                ((destination_floor == 0) ? UND_FLAG : destination_floor);
    }

    String toString2() {
        return name + String.format("[c:%d, d:%d] ", current_floor, destination_floor);
    }

    int getCurrentFloor() {
        return current_floor;
    }

    int getDestinationFloor() {
        return destination_floor;
    }

    String getName() {
        return name;
    }
}
