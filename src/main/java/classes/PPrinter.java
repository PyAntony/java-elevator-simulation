package classes;


/**
 * A PPrinter class prints the building state at
 * the Elevator current floor after each move.
 */
class PPrinter {

    private Building building;
    private int time;

    /**
     * Constructor for class PPrinter
     */
    PPrinter(Building building, int time){

        this.building = building;
        this.time = time;
    }

    /**
     * Displays entire floor state:
     * passengers in elevator, residents, upward queue, and downward queue.
     * @param flag if "start" is passed don't display elevator actions. Used
     *             for elevator first action in first floor.
     */
    void displayEntireFloor(String flag) {

        int floor = building.getElevator().getCurrentFloor();

        if (!flag.equals("start")) displayWaitingTime(time, floor);
        System.out.println();

        printElevator(floor);
        printResidents(floor);
        printQueueGoingUp(floor);
        printQueueGoingDown(floor);
    }

    /**
     * Displays passengers in elevator.
     */
    private void printElevator(int floor) {

        int size = building.getElevator().getPassengers().size();
        System.out.println(" - PASSENGERS IN ELEVATOR: " + size);

        building.getElevator().getPassengers().stream()
                .map(Passenger::toString2)
                .forEach(System.out::print);

        System.out.println();
    }

    /**
     * Displays residents.
     */
    private void printResidents(int floor) {

        int size = building.getFloor(floor).getListOf("residents").size();
        System.out.println(" - RESIDENTS: " + size);

        building.getFloor(floor).getListOf("residents").stream()
                .map(Passenger::toString2)
                .forEach(System.out::print);

        System.out.println();
    }

    /**
     * Displays people waiting in upward queue.
     */
    private void printQueueGoingUp(int floor) {

        int size = building.getFloor(floor).getListOf("up").size();
        System.out.println(" - IN QUEUE TO GO UP: " + size);

        building.getFloor(floor).getListOf("up").stream()
                .map(Passenger::toString2)
                .forEach(System.out::print);

        System.out.println();

    }

    /**
     * Displays people waiting in downward queue.
     */
    private void printQueueGoingDown(int floor) {

        int size = building.getFloor(floor).getListOf("down").size();
        System.out.println(" - IN QUEUE TO GO DOWN: " + size);

        building.getFloor(floor).getListOf("down").stream()
                .map(Passenger::toString2)
                .forEach(System.out::print);

        System.out.println();
    }

    /**
     * Displays elevator message moving from floor to floor.
     * Waiting time is indicated by the user in seconds.
     */
    private void displayWaitingTime(int time, int floor) {

        try {
            System.out.println("----------------------------------------------");
            System.out.println("\nElevator is moving to FLOOR " + floor + "...");
            waiting(time);
            System.out.println("\nUnloading/boarding passengers in FLOOR " + floor + "...");
            waiting(time);

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void waiting(int time)
            throws InterruptedException  {

        for (int i = 0; i < time; i++) {

            System.out.print("#");
            Thread.sleep(1000);
        }
    }

}
