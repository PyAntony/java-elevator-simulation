package classes;


/**
 * An ElevatorFullException is a custom Exception class that
 * extends Exception. When an Elevator object is already at Capacity and
 * another passenger is attempting to board this exception is called and
 * prints an error message. In the code that calls this exception aside from
 * the print message the code continues running and does not board the
 * passenger.
 */

class ElevatorFullException extends Exception {

    ElevatorFullException(String message) {
        super(message);
    }
}

