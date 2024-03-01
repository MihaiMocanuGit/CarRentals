package Utils;

import Domain.Car;
import Domain.Person;

public class Primitives {
    public static class PrimCar
    {
        public boolean isReserved;
        public String brand, model;
        public int year;
        public float mileage, cost;

        public PrimCar() {}
        public PrimCar(boolean isReserved, String brand, String model, int year, float mileage, float cost) {
            this.isReserved = isReserved;
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.mileage = mileage;
            this.cost = cost;
        }

    }

    public static class PrimReservation
    {
        public Car rentedCar;
        public Person renter;

        public PrimReservation(Car rentedCar, Person renter){
            this.rentedCar = rentedCar;
            this.renter = renter;
        }
    }
}
