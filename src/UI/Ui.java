package UI;

import Domain.Car;
import Domain.CarReservation;
import Domain.Person;
import Repository.MemoryRepository;
import Service.Service;
import Utils.ParsingHelper;
import Utils.Primitives;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Properties;

public class Ui {
    private Service service;
    final private String menuMessage = "Car Rentals:\n" +
                                "0) Exit\n" +
                                "1) View Free Cars.\n" +
                                "2) View Reservations.\n" +
                                "3) Add free car.\n" +
                                "4) Remove free car.\n" +
                                "5) Update free car.\n" +
                                "6) Add reservation.\n" +
                                "7) Remove reservation.\n" +
                                "8) Update reservation.\n" +
                                "9) Get cars by brand.\n" +
                                "10) Get rented cars.\n" +
                                "11) Get cars by price range.\n" +
                                "12) Get cars by year range.\n" +
                                "13) Get active orders of person.\n" +
                                "14) Clear everything.\n";

    public Ui() {
    }

    private void addPrimitiveCarToService(Primitives.PrimCar car)
    {
        service.addCar(car.brand, car.model, car.year, car.mileage, car.cost);
    }


    private void addExamplesIntoCars() {

        Primitives.PrimCar car1 = new Primitives.PrimCar(false, "Mazda", "Miata NA", 1998, 123400, 250);
        Primitives.PrimCar car2 = new Primitives.PrimCar(false, "Mazda", "Rx-7 FC Turbo", 1990, 84203, 550);
        Primitives.PrimCar car3 = new Primitives.PrimCar(false, "BMW", "E30", 1987, 941478, 300);
        Primitives.PrimCar car4 = new Primitives.PrimCar(false, "Toyota", "MR2", 1986, 441478, 400);
        Primitives.PrimCar car5 = new Primitives.PrimCar(false, "Toyota", "Trueno AE86", 1985, 1941478, 700);
        Primitives.PrimCar car6 = new Primitives.PrimCar(false, "Dacia", "1300", 1990, 1041786, 50);
        Primitives.PrimCar car7 = new Primitives.PrimCar(false, "Dacia", "Logan", 2008, 41786, 600);
        Primitives.PrimCar car8 = new Primitives.PrimCar(false, "Honda", "S200", 2004, 21786, 900);
        Primitives.PrimCar car9 = new Primitives.PrimCar(false, "Subaru", "Impreza WRX STI", 2004, 21786, 1000);
        Primitives.PrimCar car10 = new Primitives.PrimCar(false, "Mitsubishi", "Lancer Evo IX ", 2006, 2186, 1005);

        addPrimitiveCarToService(car1);
        addPrimitiveCarToService(car2);
        addPrimitiveCarToService(car3);
        addPrimitiveCarToService(car4);
        addPrimitiveCarToService(car5);
        addPrimitiveCarToService(car6);
        addPrimitiveCarToService(car7);
        addPrimitiveCarToService(car8);
        addPrimitiveCarToService(car9);
        addPrimitiveCarToService(car10);
    }

    private void addExamplesIntoRepos() {

        addExamplesIntoCars();

        Person person1 = new Person("Smith", "Rick", "012312");
        Person person2 = new Person("Johnsonn", "John", "123123");
        Person person3 = new Person("Notmarkinson", "Mark", "4323123");
        Person person4 = new Person("Le'decontare", "Transport Public", "4123");
        Person person5 = new Person("Tsuchiya", "Keiichi", "4124322423");
        Person person6 = new Person("Tsuchiya", "Keiichi", "4124322423");

        Person[] people = {person1, person2, person3, person4, person5, person6};

        for (int i = 0; i < people.length; i++) {
            service.makeReservation(i, people[i]);
        }
    }
    private static class RepoFilePaths
    {
        public String carsRepoFilePath;
        public String reservationsRepoFilePath;

        public RepoFilePaths(String carsRepoFilePath, String reservationsRepoFilePath) {
            this.carsRepoFilePath = carsRepoFilePath;
            this.reservationsRepoFilePath = reservationsRepoFilePath;
        }
    }
    private static RepoFilePaths getReposPath(String settingsPath)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(settingsPath))){
            String carRepoPath = "";
            String reservationRepoPath = "";

            String line;
            while ( (line = reader.readLine()) != null){
                if (line.contains("Cars ="))
                {
                    int startStringIndex = line.indexOf('\"');
                    int endStringIndex = line.lastIndexOf('\"');
                    if (startStringIndex == -1 || endStringIndex == -1)
                        throw new ParsingHelper.ParseError("Could not parse car repo file path: " + line);

                    carRepoPath = line.substring(startStringIndex + 1, endStringIndex);
                }
                else if (line.contains("Reservations ="))
                {
                    int startStringIndex = line.indexOf('\"');
                    int endStringIndex = line.lastIndexOf('\"');
                    if (startStringIndex == -1 || endStringIndex == -1)
                        throw new ParsingHelper.ParseError("Could not parse reservation repo file path: " + line);

                    reservationRepoPath = line.substring(startStringIndex + 1, endStringIndex);
                }
            }

            if (carRepoPath.isEmpty() || reservationRepoPath.isEmpty()) {
                throw new ParsingHelper.ParseError("Could not correctly find both repos' paths");
            }
            else{
                return new RepoFilePaths(carRepoPath, reservationRepoPath);
            }

        } catch (FileNotFoundException f){
            throw new Error("Could not open file at: " + settingsPath);
        }catch (IOException e) {
            throw new ParsingHelper.ParseError("Could not read line from text file:\n" +
                    "\tFilepath: \""+ settingsPath + "\"");
        }
    }

    private Car askUserForCar()  throws IOException
    {
        if (service.noOfCars() == 0) {
            throw new RuntimeException("There are no cars!");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Car car = null;
        boolean failed;
        do
        {
            try {
                System.out.print("Enter car id:\n>>\t");
                int carId = Integer.parseInt(br.readLine());

                car = service.getCarById(carId);
                if (car == null)
                {
                    System.out.println("There's no car with that id.");
                    failed = true;
                }
                else
                    failed = false;
            }
            catch (NumberFormatException exception)
            {
                System.out.println("Could not convert input into integer.");
                failed = true;
            }

        }while (failed);

        return car;
    }
    private Car askUserForAvailableCar() throws IOException, RuntimeException
    {
        MemoryRepository<Integer, Car> availableCars = service.getAllAvailableCars();
        if (availableCars.size() == 0) {
            throw new RuntimeException("No available cars left!");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Car car = null;
        boolean failed;
        do
        {
            try {
                System.out.print("Enter car id:\n>>\t");
                int carId = Integer.parseInt(br.readLine());

                car = availableCars.findById(carId);
                if (car == null)
                {
                    System.out.println("There's no available car with that id.");
                    failed = true;
                }
                else
                    failed = false;
            }
            catch (NumberFormatException exception)
            {
                System.out.println("Could not convert input into integer.");
                failed = true;
            }

        }while (failed);

        return car;
    }

    private Primitives.PrimCar askUserForNewCarData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Primitives.PrimCar car = new Primitives.PrimCar();

        System.out.print("Enter car brand:\n>>\t");
        car.brand = br.readLine();

        System.out.print("Enter car model:\n>>\t");
        car.model = br.readLine();

        boolean failed;
        do {
            try {
                System.out.print("Enter car year:\n>>\t");
                car.year = Integer.parseInt(br.readLine());

                failed = false;
            } catch (NumberFormatException exception) {
                System.out.println("Could not convert input into integer.");
                failed = true;
            }
        } while (failed);


        do {
            try {
                System.out.print("Enter car mileage:\n>>\t");
                car.mileage = Float.parseFloat(br.readLine());

                failed = false;
            } catch (NumberFormatException exception) {
                System.out.println("Could not convert input into float.");
                failed = true;
            }
        } while (failed);

        do {
            try {
                System.out.print("Enter car cost:\n>>\t");
                car.cost = Float.parseFloat(br.readLine());

                failed = false;
            } catch (NumberFormatException exception) {
                System.out.println("Could not convert input into float.");
                failed = true;
            }
        } while (failed);

        return car;
    }

    private CarReservation askUserForReservation() throws IOException {
        if (service.noOfReservations() == 0) {
            throw new RuntimeException("There are no reservations!");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        CarReservation reservation = null;
        boolean failed;
        do {
            try {
                System.out.print("Enter reservation id:\n>>\t");
                int reservationId = Integer.parseInt(br.readLine());

                reservation= service.getReservation(reservationId);
                failed = false;

            } catch (NumberFormatException exception) {
                System.out.println("Could not convert input into integer.");
                failed = true;
            } catch (NoSuchElementException exception)
            {
                System.out.println("Could not find a reservation with the given id.");
                failed = true;
            }
        } while (failed);

        return reservation;
    }
    private Primitives.PrimReservation askUserForNewReservationData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Car car = null;
        try {
            car = askUserForAvailableCar();
        } catch (RuntimeException exception)
        {
            //System.out.print("No free cars!");
            throw exception;
        }

        System.out.print("Enter renter's family name:\n>>\t");
        String familyName = br.readLine();

        System.out.print("Enter renter's first name:\n>>\t");
        String firstName = br.readLine();

        System.out.print("Enter renter's personal id:\n>>\t");
        String id = br.readLine();

        Person person = new Person(familyName, firstName, id);

        return new Primitives.PrimReservation(car, person);
    }
    private String viewReservations()
    {
        StringBuilder result = new StringBuilder();

        for (CarReservation reservation : service.reservations())
            result.append(reservation.toString()).append(";\n");

        if (result.isEmpty())
            return "There are no reservations!";
        else
            return result.toString();
    }
    private String addNewReservation() throws IOException {
        try {
            Primitives.PrimReservation primitiveReservation = askUserForNewReservationData();

            CarReservation reservation = service.makeReservation(primitiveReservation.rentedCar.getId(),
                    primitiveReservation.renter);

            return "Made reservation:\n\t" + reservation.toString() + ";";

        } catch (Exception exception)
        {
            //most probably askUserForNewReservationData re-threw an exception from askForFreeCar that happened
            //because freeCars is empty
            return exception.getMessage();
        }

    }
    private String removeReservation() throws IOException {

        try {
            CarReservation reservation = askUserForReservation();

            service.removeReservation(reservation.getId());

            return "Removed " + reservation.toString() + ";";
        } catch (RuntimeException exception)
        {
            return exception.getMessage();
        }

    }

    private String updateReservation() throws IOException {
        try {
            CarReservation reservation = askUserForReservation();
            service.removeReservation(reservation.getId());
            String oldReservationStr = reservation.toString();

            service.writeReservationsToFile();
            return addNewReservation() + "\n Old reservation was: " + oldReservationStr + ";";

        } catch (Exception exception)
        {
            //most probably askUserForReservation threw an exception that happened because there are no reservations;
            return exception.getMessage();
        }
    }

    private String viewCars() throws IOException{
        StringBuilder result = new StringBuilder();

        for (Car car : service.cars())
            result.append(car.toString()).append(";\n");

        if (result.isEmpty())
            return  "There are no free cars!";
        else
            return result.toString();
    }
    private String addCar() throws IOException{
        Primitives.PrimCar primitiveCar = askUserForNewCarData();
        Car car = service.addCar(primitiveCar.brand, primitiveCar.model, primitiveCar.year,
                                        primitiveCar.mileage, primitiveCar.cost);

        return "Added " + car.toString() + ";";
    }

    private String removeCar(){
        try {
            Car chosenCar = askUserForAvailableCar();

            service.removeCar(chosenCar.getId());
            return "Removed " + chosenCar.toString();
        } catch (Exception exception)
        {
            return exception.getMessage();
        }

    }

    private String updateCar() throws IOException{
        try {
            Car chosenCar = askUserForCar();
            String oldCarStr = chosenCar.toString();

            Primitives.PrimCar updatedCar = askUserForNewCarData();
            updatedCar.isReserved = chosenCar.getIsReserved();
            /*
            chosenCar.setBrand(updatedCar.brand);
            chosenCar.setModel(updatedCar.model);
            chosenCar.setYear(updatedCar.year);
            chosenCar.setMileage(updatedCar.mileage);
            chosenCar.setCost(updatedCar.cost);
            */
            service.updateCar(chosenCar.getId(), updatedCar);


            service.writeCarsToFile();
            return "Updated " + oldCarStr + " to\n " + service.getCarById(chosenCar.getId()).toString();
        } catch (Exception exception)
        {
            return exception.getMessage();
        }
    }

    public String getAllCarsByBrand() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter car brand:\n>>\t");
        String brand = br.readLine();

        return service.getAllCarsByBrand(brand).toString();
    }

    public String getAllRentedCars() throws IOException{
        return service.getAllRentedCars().toString();
    }

    public String getAllCarsByPriceRange() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean failed;
        float minPrice = 0, maxPrice = Float.MAX_VALUE;
        do {
            try {
                System.out.print("Enter minimum car price:\n>>\t");
                minPrice = Float.parseFloat(br.readLine());

                System.out.print("Enter maximum car price:\n>>\t");
                maxPrice = Float.parseFloat(br.readLine());

                failed = false;
            } catch (NumberFormatException exception) {
                System.out.println("Could not convert input into float.");
                failed = true;
            }
        } while (failed);

        return service.getAllCarsByPriceRange(minPrice, maxPrice).toString();
    }

    public String getAllCarsByYearRange() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean failed;
        int minYear = 0, maxYear = Integer.MAX_VALUE;
        do {
            try {
                System.out.print("Enter minimum car year:\n>>\t");
                minYear = Integer.parseInt(br.readLine());

                System.out.print("Enter maximum car price:\n>>\t");
                maxYear = Integer.parseInt(br.readLine());

                failed = false;
            } catch (NumberFormatException exception) {
                System.out.println("Could not convert input into int.");
                failed = true;
            }
        } while (failed);

        return service.getAllCarsByYearRange(minYear, maxYear).toString();
    }

    public String getAllActiveOrdersByPerson() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter renter's family name:\n>>\t");
        String familyName = br.readLine();

        System.out.print("Enter renter's first name:\n>>\t");
        String firstName = br.readLine();

        return service.getAllActiveOrdersByPerson(familyName, firstName).toString();
    }
    public String clearEverything()
    {
        int reservationsNo = service.noOfReservations();
        int carsNo = service.noOfCars();
        service.removeAll();

        return "Removed " +  reservationsNo + " reservations and "
                + carsNo + " cars.";
    }

    private void startLoop() throws IOException {
        boolean exit = false;

        String resultMessage = "";
        while(!exit)
        {

            System.out.print(menuMessage);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            if (!resultMessage.isEmpty())
                System.out.println("Result:\n" + resultMessage);

            System.out.print("Enter your option:\n>>\t");
            String option = br.readLine();

            switch (option)
            {
                case "0":
                    exit = true;
                    resultMessage = "Goodbye!";
                    break;
                case "1":
                    resultMessage = viewCars();
                    break;
                case "2":
                    resultMessage = viewReservations();
                    break;
                case "3":
                    resultMessage = addCar();
                    break;
                case "4":
                    resultMessage = removeCar();
                    break;
                case "5":
                    resultMessage = updateCar();
                    break;
                case "6":
                    resultMessage = addNewReservation();
                    break;
                case "7":
                    resultMessage = removeReservation();
                    break;
                case "8":
                    resultMessage = updateReservation();
                    break;
                case "9":
                    resultMessage = getAllCarsByBrand();
                    break;
                case "10":
                    resultMessage = getAllRentedCars();
                    break;
                case "11":
                    resultMessage = getAllCarsByPriceRange();
                    break;
                case "12":
                    resultMessage = getAllCarsByYearRange();
                    break;
                case "13":
                    resultMessage = getAllActiveOrdersByPerson();
                    break;
                case "14":
                    resultMessage = clearEverything();
                    break;
                default:
                    resultMessage = "Wrong input, try again!";
            }
        }
        service.writeAllToFile();
    }

    public void startUi() throws IOException {

        try (InputStream input = new FileInputStream("settings.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            String data = prop.getProperty("data");
            String carRepoFilepath = prop.getProperty("Cars");
            String reservationRepoFilepath = prop.getProperty("Reservations");

            service =  new Service(carRepoFilepath, reservationRepoFilepath, data);

            System.out.print("Want to clear old data and add new examples? [y/N] \n>>\t");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String result = br.readLine();
            if (Objects.equals(result.toLowerCase(), "y")) {
                System.out.println("Clearing old data...");
                clearEverything();
                System.out.println("Inserting examples...");
                addExamplesIntoRepos();
            }
            else {
                System.out.println("Skipping examples");
            }

            startLoop();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw new Error("Repo could not read from file");
        }

    }

}
