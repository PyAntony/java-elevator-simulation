# Elevator Simulation

## Task

Design a simulation for an elevator in a building and its occupants. There are multiple requirements that must be followed. In general, the building, the elevator, and the occupants must be able to update and register their own state after each movement, and this state must be displayed with each elevator arrival. There is also a component of randomization to change the state of the building occupants.

## Design

The simulation has 7 classes:

### Building:

A Building object has 1 elevator and multiple floors specified by the user. They are all initialized in the constructor. It has methods to put people in the building and get its Elevator and Floor objects. It also has a method to randomize the state of all its floors:
```java
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
```

### Elevator:

Represents the building Elevator. It has fields to indicate its capacity (user specified), current floor and direction (up or down). Among other methods the most complex are “move()” and “releasePassenger()” since they must accomplish a sequence in strict order: 
```java
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
 ```
```java
    /**
     * Release a passenger. Steps:
     * - Mark passenger arrival.
     * - Enter current floor as a resident.
     * - Removes passenger from elevator list.
     */
    private void releasePassenger(Passenger user) {

        user.arrive();
        building.getFloor(currentFloor).enterGroundFloor(user);
        passengerList.remove(user);
    }
 ```
The function “getPassengersIn()” is in turn composed of multiple steps. As an example:
```java
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
```
Some requirements for the elevator include the switching of direction, capacity checking, and boarding passengers only from the correct queue (passengers going up can’t be picked up by an elevator that is going down.)

### ElevatorFullException:

Simple class that extends the Exception object. This object if thrown and its message displayed when elevator is at full capacity. 

### Floor:

A Floor object represents a “getFloor()” in a building. Each Floor object has a Floor number and tracks how many people are waiting for the Elevator in different directions. Among many functions to adjust and retrieve its collections, it has 2 functions that execute multiple steps:
```java
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
```
This is the randomization method that belongs to Floor and the Building calls for each floor:
```java
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
```

### Passenger:

A Passenger object represents a person to ride the Elevator. It has a name (randomly generated), knows its current Floor and its destination Floor.
### Pprinter:

It is a helper class that prints the building state at the elevator current floor after each move. It displays the total number of persons for each list in floor: passengerList, residents, upwardBound, downwardBound. It also displays the names in each list, and the current floor and destination of each person as “Stephie[c:0, d:6]” where “c” is the current floor and “d” the destination floor. Floor 0 means “undefined” (passengers have undefined destination or current floor depending the list they are in).

### Main:

Class holding the main static method. It instantiates the building and its components (elevator and floors), put people in the building and run the simulation: move elevator, display the state, randomize the state, repeat.

## Instructions

Make sure you have Maven and JDK 11 installed. Steps:

- clone repository.
- “cd elevator”
- “mvn clean package”
- “java -jar target/elevator-v1.jar \<numPeople\> \<movingTime\> \<numFloors\> \<eCapacity\> \<simNum\>”

Required arguments **->**  
*numPeople*: number of people to enter the building.  
*movingTime*: time to wait for elevator to move between floors in seconds.  
*numFloors*: number of floors in the building.  
*eCapacity*: elevator loading capacity.  
*simNum*: number of floors the elevator will travel.  


## Output Example

![Screenshot](https://github.com/PyAntony/elevator/blob/master/images/Screenshot2.png)
