package Domain;

import Utils.ParsingHelper;

import java.io.Serializable;

public class CarReservation implements Identifiable<Integer>, Serializable {
    private int id;
    private Car rentedCar;
    private Person renter;



    public CarReservation(int id, Car rentedCar, Person renter)
    {
        this.id = id;
        this.rentedCar = rentedCar;
        this.renter = renter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarReservation that = (CarReservation) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "CarReservation{" +
                "id=" + id +
                ", rentedCar=" + rentedCar +
                ", renter=" + renter +
                '}';
    }
    static public CarReservation parseReservation(String str)
    {
        if (str.indexOf("CarReservation{") != 0)
            return null;

        if (!ParsingHelper.bracketsAreValid(str))
            return null;

        int startSearchIndex = "CarReservation{".length();
        String idStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "id=", ", ");
        if (idStr == null)
            return null;
        startSearchIndex += "id=".length() + idStr.length();

        ParsingHelper.ScopeGroup bracketGroupCar = ParsingHelper.findClosestScopeGroup(str, '{', '}',
                                                                startSearchIndex, str.length());
        if (bracketGroupCar == null)
            return null;

        String rentedCarStr = str.substring(bracketGroupCar.startScopeIndex - "Car".length(),
                                            bracketGroupCar.endScopeIndex + 1);

        startSearchIndex =  bracketGroupCar.endScopeIndex + 1;

        ParsingHelper.ScopeGroup bracketGroupPerson = ParsingHelper.findClosestScopeGroup(str, '{',
                                                            '}', startSearchIndex, str.length());
        if (bracketGroupPerson == null)
            return null;

        String renterStr = str.substring(bracketGroupPerson.startScopeIndex - "Person".length(),
                                         bracketGroupPerson.endScopeIndex + 1);

        startSearchIndex = bracketGroupCar.endScopeIndex + 1;

        int id = Integer.parseInt(idStr);
        Car rentedCar = Car.parseCar(rentedCarStr);
        Person renter = Person.parsePerson(renterStr);

        if (rentedCar == null)
            throw new ParsingHelper.ParseError("Could not parse rentedCar from str: " + rentedCarStr);
        else if (renter == null)
            throw new ParsingHelper.ParseError("Could not parse renter from string: " + renterStr);

        return new CarReservation(id, rentedCar, renter);
    }

    /*
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    */
    @Override
    public Integer getId() {
        return id;
    }

    public Car getRentedCar() {
        return rentedCar;
    }

    public void setRentedCar(Car rentedCar) {
        this.rentedCar = rentedCar;
    }

    public Person getRenter() {
        return renter;
    }

    public void setRenter(Person renter) {
        this.renter = renter;
    }


}
