package classes;

import java.util.*;


/**
 * A Building object represents a Building. A building
 * has an Elevator objects as well as a number of FLOORS as defined
 * in a static int. A building knows the state of each Floor object as
 * well as it's Elevator.
 */
class Building {

    static int FLOORS;
    // reference to the elevator class
    private Elevator elevator;
    // container for the floor objects
    private HashMap<Integer, Floor> floorsMap = new HashMap<>();

    /**
     * Constructor for class Building
     */
    Building(int numFloors, int elevatorCapacity) {

        FLOORS = numFloors;
        this.elevator = new Elevator(this, elevatorCapacity);

        // to instantiate all floors
        for (int n = 1; n <= FLOORS; n++) {
            floorsMap.put(n, new Floor(this));
        }
    }

    /**
     * To enter the building in the first floor
     */
    void enter(Passenger passenger) {
        getFloor(1).enterGroundFloor(passenger);
    }

    /**
     * Randomizes the waiting queues in all floors if random number generated
     * is greater than 0.5. Residents in floors will be randomly assigned to
     * waiting queues with random destination floors.
     * @param flag if "enforce" is passed the function guarantees randomization
     *             by passing 0.99 rather than a random number.
     */
    void randomizeQueues(String flag) {

        double num = (flag.equals("enforce")) ? 0.99 : Math.random();
        if (num > 0.5) floorsMap.values().forEach(Floor::randomizeDestinations);
    }

    Elevator getElevator() {
        return elevator;
    }

    Floor getFloor(int floorNumber) {
        return floorsMap.get(floorNumber);
    }
}