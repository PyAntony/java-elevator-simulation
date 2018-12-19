package classes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) {

        if (args.length != 5) {

            System.out.println("Usage: \"java -jar elevator-v1.jar" +
                    " <numPeople> <movingTime> <numFloors> <eCapacity> <simNum>\"\n\n" +
                    "numPeople: number of people to enter the building.\n" +
                    "movingTime: time to wait for elevator to move between floors in seconds.\n" +
                    "numFloors: number of floors in the building.\n" +
                    "eCapacity: elevator loading capacity.\n" +
                    "simNum: number of floors the elevator will travel.\n");
            return;
        }

        var numPeople = Integer.parseInt(args[0]);
        var movingTime = Integer.parseInt(args[1]);
        var numFloors = Integer.parseInt(args[2]);
        var elevatorCapacity = Integer.parseInt(args[3]);
        var simNum = Integer.parseInt(args[4]);

        Building building = new Building(numFloors, elevatorCapacity);
        Pprinter Pprinter = new Pprinter(building, movingTime);

        enterBuilding(building, numPeople);

        building.randomizeQueues("enforce");
        building.getElevator().getPassengersIn();
        Pprinter.displayEntireFloor("start");

        for (int i = 0; i <= simNum; i++) {
            building.getElevator().move();
            Pprinter.displayEntireFloor("standard");
            building.randomizeQueues("standard");
        }
    }

    private static void enterBuilding(Building building, int number) {

        List<String> names = readTxtFileInProject();

        for (int i = 0; i < number; i++) {
            building.enter(new Passenger(chooseRandom(names)));
        }
    }

    private static List<String> readTxtFileInProject() {

        InputStream resource = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("names.txt");

        return new BufferedReader(new InputStreamReader(resource))
                .lines()
                .collect(Collectors.toList());
    }

    private static String chooseRandom(List<String> names) {
        return names.get(new Random().nextInt(names.size()));
    }

}


