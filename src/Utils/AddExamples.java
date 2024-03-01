package Utils;

import Domain.Person;
import Service.Service;

public class AddExamples {
    static private void addPrimitiveCarToService(Primitives.PrimCar car, Service service)
    {
        service.addCar(car.brand, car.model, car.year, car.mileage, car.cost);
    }


    static private void addExamplesIntoCars(Service service) {

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

        addPrimitiveCarToService(car1, service);
        addPrimitiveCarToService(car2, service);
        addPrimitiveCarToService(car3, service);
        addPrimitiveCarToService(car4, service);
        addPrimitiveCarToService(car5, service);
        addPrimitiveCarToService(car6, service);
        addPrimitiveCarToService(car7, service);
        addPrimitiveCarToService(car8, service);
        addPrimitiveCarToService(car9, service);
        addPrimitiveCarToService(car10, service);
    }

    static public void addExamplesIntoRepos(Service service) {
        if (service.noOfCars() == 0 && service.noOfReservations() == 0) {
            addExamplesIntoCars(service);

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
    }
}
