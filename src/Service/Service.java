package Service;

import Domain.Car;
import Domain.CarReservation;
import Domain.Person;
import Repository.*;
import Utils.Primitives;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {

    //the id of a free car will be considered as the id of the reservation when that car is used
    private PersistentRepository<Integer, Car> cars;
    private int validCarId = 0;

    private PersistentRepository<Integer, CarReservation> reservations;

    private void findNextValidCarId()
    {
        int maxId = -1;
        for (Car car : cars.getAll())
        {
            if (car.getId() > maxId)
                maxId = car.getId();
        }

        for (CarReservation reservation : reservations.getAll())
        {
            if (reservation.getId() > maxId)
                maxId = reservation.getId();
        }

        this.validCarId = maxId + 1;
    }

    public Service(String filenameCarRepo, String filenameReservationRepo, String data) throws FileNotFoundException {
        if (filenameCarRepo.contains(".txt"))
            cars = new CarRepoText(filenameCarRepo);
        else if (filenameCarRepo.contains(".bin"))
            cars = new CarRepoBinary(filenameCarRepo);
        else if (data != null && !data.isEmpty()) //we are working with a database
            cars = new CarRepoDatabase(data);
        else
            throw new FileNotFoundException("File must be either .txt, .bin or a database table!");

        if (filenameReservationRepo.contains(".txt"))
            reservations = new ReservationsRepoText(filenameReservationRepo);
        else if (filenameReservationRepo.contains(".bin"))
            reservations = new ReservationsRepoBinary(filenameReservationRepo);
        else if (!filenameReservationRepo.contains(".")) //we are working with a database
            reservations = new ReservationsRepoDatabase(filenameReservationRepo);
        else
            throw new FileNotFoundException("File must be either .txt, .bin or a database table");

        findNextValidCarId();
    }

    public Iterable<CarReservation> reservations()
    {
        return reservations.getAll();
    }
    public CarReservation makeReservation(int carId, Person renter)
            throws NoSuchElementException, RuntimeException {

        //the id of a car will coincide with the id of a reservation using that car
        if (reservations.findById(carId) != null)
            throw new RuntimeException("A reservation with the same id already exists!");

        //might throw NoSuchElementException
        Car rentedCar = getCarById(carId);
        rentedCar.setIsReserved(true);
        CarReservation reservation = new CarReservation(rentedCar.getId(), rentedCar, renter);

        cars.update(rentedCar.getId(), rentedCar);
        reservations.add(reservation);

        return reservation;
    }

    public CarReservation removeReservation(int reservationId)
            throws NoSuchElementException
    {
        CarReservation reservation = reservations.findById(reservationId);

        if (reservation == null) {
            throw new NoSuchElementException("There is no reservation by that id!");
        }
        else {
            reservations.delete(reservationId);
            Car car = cars.findById(reservationId);
            car.setIsReserved(false);
            cars.update(reservationId, car);
            //freeCars.add(reservation.getRentedCar());
            return reservation;
        }
    }
    public CarReservation getReservation(int reservationId)
            throws NoSuchElementException
    {
        CarReservation reservation = reservations.findById(reservationId);
        if (reservation == null)
            throw new NoSuchElementException("There is no reservation by that id!");
        else
            return reservation;
    }

    public CarReservation updateReservation(int oldReservationId, int newCarId, Person newRenter)
    {
        CarReservation oldReservation = getReservation(oldReservationId);

        Car newCar = getCarById(newCarId);
        boolean mustUpdateCarRentedStatus = false;
        if (newCar.getIsReserved() && newCarId != oldReservationId)
            throw new RuntimeException("The given car is not available!");
        else if (newCarId != oldReservationId) {
            mustUpdateCarRentedStatus = true;
        }

        CarReservation newReservation = new CarReservation(newCarId, newCar, newRenter);
        CarReservation reservation = reservations.update(oldReservationId, newReservation);

        if (mustUpdateCarRentedStatus)
        {
            Car oldCar = cars.findById(oldReservationId);
            oldCar.setIsReserved(false);
            cars.update(oldReservationId, oldCar);

            newCar.setIsReserved(true);
            cars.update(newCarId, newCar);
        }
        return reservation;
    }

    public int noOfReservations()
    {
        return reservations.size();
    }

    public Iterable<Car> cars()
    {
        return cars.getAll();
    }
    public Car addCar(String brand, String model, int year, float mileage, float cost)
    {
        Car car = new Car(validCarId++, false,brand, model, year, mileage, cost);
        cars.add(car);
        return car;
    }

    public Car removeCar(int carId)
            throws NoSuchElementException
    {
        Car car = cars.findById(carId);
        reservations.delete(carId);
        if (car == null)
            throw new NoSuchElementException("There is no free car by that id!");
        else
            return cars.delete(carId);
    }

    public Car getCarById(int carId)
            throws NoSuchElementException
    {
        Car car = cars.findById(carId);
        if (car == null)
            throw new NoSuchElementException("There is no free car by that id!");
        else
            return car;
    }

    public Car updateCar(int oldCarId, Primitives.PrimCar newCarData)
    {
        Car newCar = new Car(oldCarId, newCarData.isReserved, newCarData.brand, newCarData.model, newCarData.year,
                        newCarData.mileage, newCarData.cost);
        return cars.update(oldCarId, newCar);
    }
    public int noOfCars()
    {
        return cars.size();
    }

    public MemoryRepository<Integer, Car> getAllAvailableCars()
    {
        MemoryRepository<Integer, Car>  availableCars = new MemoryRepository<>();
        cars.getAll().forEach(car ->
        {
            if (!car.getIsReserved())
                availableCars.add(car);
        });

        return availableCars;
    }

    public void writeCarsToFile()
    {
        cars.writeToFile();
    }

    public void writeReservationsToFile()
    {
        reservations.writeToFile();
    }

    public void writeAllToFile()
    {
        writeCarsToFile();
        writeReservationsToFile();
    }

    public void removeAll()
    {
        ArrayList<Integer> carId = new ArrayList<>();
        for (Car car : cars())
            carId.add(car.getId());

        for (Integer id : carId)
            removeCar(id);

        this.validCarId = 0;
    }

    public List<Car> getAllCarsByBrand(String brand)
    {
        return StreamSupport.stream(cars.getAll().spliterator(), false)
                .filter(car -> car.getBrand().contains(brand))
                .collect(Collectors.toList());
    }

    public List<Car> getAllRentedCars()
    {
        return StreamSupport.stream(cars.getAll().spliterator(), false)
                .filter(Car::getIsReserved)
                .collect(Collectors.toList());
    }

    public List<Car> getAllCarsByPriceRange(float minPrice, float maxPrice)
    {
        return StreamSupport.stream(cars.getAll().spliterator(), false)
                .filter(car -> minPrice <= car.getCost() && car.getCost() <= maxPrice)
                .collect(Collectors.toList());
    }
    public List<Car> getAllCarsByPriceRange(float maxPrice)
    {
        return getAllCarsByPriceRange(0, maxPrice);
    }
    public List<Car> getAllCarsByYearRange(float minYear, float maxYear)
    {
        return StreamSupport.stream(cars.getAll().spliterator(), false)
                .filter(car -> minYear <= car.getYear() && car.getCost() <= maxYear)
                .collect(Collectors.toList());
    }

    public List<Car> getAllCarsByYearRange(float maxYear)
    {
        return getAllCarsByYearRange(0, maxYear);
    }

    public List<CarReservation> getAllReservationsByPerson(String familyName, String firstName)
    {
        return StreamSupport.stream(reservations.getAll().spliterator(), false)
                .filter(reservation ->  reservation.getRenter().getFamilyName().contains(familyName) &&
                        reservation.getRenter().getFirstName().contains(firstName))
                .collect(Collectors.toList());
    }

    public List<CarReservation> getAllReservationsByFamilyName(String familyName)
    {
        return getAllReservationsByPerson(familyName, "");
    }

    public List<CarReservation> getAllReservationsByFirstName(String firstName)
    {
        return getAllReservationsByPerson("", firstName);
    }

    public List<Car> getAllActiveOrdersByPerson(String familyName, String firstName)
    {
        return StreamSupport.stream(reservations.getAll().spliterator(), false)
                .filter(reservation ->  reservation.getRenter().getFamilyName().contains(familyName) &&
                                        reservation.getRenter().getFirstName().contains(firstName))
                .map(CarReservation::getRentedCar)
                .collect(Collectors.toList());
    }

    public List<Car> getAllActiveOrdersByFamilyName(String familyName)
    {
        return getAllActiveOrdersByPerson(familyName, "");
    }

    public List<Car> getAllActiveOrdersByFirstName(String firstName)
    {
        return getAllActiveOrdersByPerson("", firstName);
    }
}
