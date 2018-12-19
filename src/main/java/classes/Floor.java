package classes;

import java.util.*;


/**
 * A Floor object represents a getFloor in a building.
 * Each Floor object has a Floor number and tracks how many people
 * are waiting for the Elevator in different directions.
 */
class Floor {

    private Building building;

    // collections for type of passengers/residents in this floor.
    // residents: non-waiting persons
    // upwardBound: waiting to go up
    // downwardBound: waiting to go down
    private List<Passenger> residents = new ArrayList<>();
    private Deque<Passenger> upwardBound = new ArrayDeque<>();
    private Deque<Passenger> downwardBound = new ArrayDeque<>();

    /**
     * Constructor for class Floor
     */
    Floor(Building building) {
        this.building = building;
    }

    /**
     * A person in residents List decides to wait for elevator. Steps:
     * - assigns destination floor with passenger method.
     * - resident gets assigned to either upward or downward queue.
     * - resident gets removed from residents List.
     */
    private void waitForElevator(Passenger passenger, int destinationFloor) {

        passenger.waitForElevator(destinationFloor);

        if (destinationFloor > passenger.getCurrentFloor()) upwardBound.add(passenger);
        else downwardBound.add(passenger);

        residents.remove(passenger);
    }

    boolean isResident(Passenger passenger) {
        return residents.contains(passenger);
    }

    void enterGroundFloor(Passenger passenger) {
        residents.add(passenger);
    }

    /**
     * Removes the first person in the upward/downward queue
     * when entering elevator.
     * @param direction direction of the elevator at loading moment
     */
    void decrementQueue(String direction) {

        if (direction.equals("up")) upwardBound.removeFirst();
        else downwardBound.removeFirst();
    }

    /**
     * Gets the first person of the upward/downward queue
     * when entering the elevator but won't remove the passenger.
     * @param direction direction of the elevator at loading moment
     */
    Passenger getFirstPassenger(String direction) {

        return direction.equals("up")
                ? upwardBound.getFirst()
                : downwardBound.getFirst();
    }

    Boolean queueIsEmpty(String direction) {

        return direction.equals("up")
                ? upwardBound.isEmpty()
                : downwardBound.isEmpty();
    }

    int getQueueSize(String direction) {

        return direction.equals("up")
                ? upwardBound.size()
                : downwardBound.size();
    }

    int getResidentsSize() {
        return residents.size();
    }

    /**
     * Randomizes the destination of the waiting residents in this floor.
     * It receives a Map with residents that want to board the elevator
     * and their destinations and calls the "waitForElevator" method
     * for each of them.
     */
    void randomizeDestinations() {

        for (Map.Entry<Passenger, Integer> entry :
                getResidentsReadyToBoard().entrySet()) {

            waitForElevator(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Helps to randomize destinations. A random number is generated,
     * If it is not 0 then the resident is put in a new Map as indication
     * of its desire to board the elevator. The number generated will be
     * its destination.
     */
    private Map<Passenger, Integer> getResidentsReadyToBoard() {

        Map<Passenger, Integer> toQueues = new HashMap<>();
        for (Passenger user : residents) {

            int rand = new Random().nextInt(Building.FLOORS);

            if (rand != 0 && rand != user.getCurrentFloor()) {
                toQueues.put(user, rand);
            }
        }
        return toQueues;
    }

    /**
     * Return the indicated Collection from this floor
     * @param flag String to indicate the required collection
     *             (residents, up, down)
     */
    List<Passenger> getListOf(String flag) {

        if (flag.equals("residents")) return residents;
        else if (flag.equals("up")) return new ArrayList<>(upwardBound);
        else return new ArrayList<>(downwardBound);
    }

}