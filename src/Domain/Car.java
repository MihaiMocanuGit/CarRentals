package Domain;

import Utils.ParsingHelper;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Car implements Identifiable<Integer>, Serializable {
    private int id;
    private boolean isReserved;
    private String brand;
    private String model;
    private int year;
    private float mileage;
    private float cost;

    public Car(Integer id, boolean isReserved, String brand, String model, int year, float mileage, float cost)
    {
        this.id = id;
        this.isReserved = isReserved;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        return id == car.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", isReserved=" + isReserved +
                ", brand=" + brand +
                ", model=" + model +
                ", year=" + year +
                ", mileage=" + mileage +
                ", cost=" + cost +
                '}';
    }

    public static Car parseCar(String str)
    {

        if (str.indexOf("Car{") != 0)
            return null;

        if (!ParsingHelper.bracketsAreValid(str))
            return null;

        int startSearchIndex = "Car{".length();
        String idStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "id=", ", ");
        if (idStr == null)
            return null;
        startSearchIndex += "id=".length() + idStr.length();

        String isReservedStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "isReserved=", ", ");
        if (isReservedStr == null)
            return null;
        startSearchIndex += "isReserved=".length() + idStr.length();

        String brandStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "brand=", ", ");
        if (brandStr == null)
            return null;
        startSearchIndex += "brand=".length() + brandStr.length();

        String modelStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "model=", ", ");
        if (modelStr == null)
            return null;
        startSearchIndex += "model=".length() + modelStr.length();

        String yearStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "year=", ", ");
        if (yearStr == null)
            return null;
        startSearchIndex += "year=".length() +  yearStr.length();

        String mileageStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "mileage=", ", ");
        if (mileageStr == null)
            return null;
        startSearchIndex += "mileage=".length() + mileageStr.length();

        String costStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "cost=", "}");
        if (costStr == null)
            return null;
        startSearchIndex += "cost=".length() + mileageStr.length();

        int id = Integer.parseInt(idStr);
        boolean isReserved = Boolean.parseBoolean(isReservedStr);
        String brand = brandStr;
        String model = modelStr;
        int year = Integer.parseInt(yearStr);
        float mileage = Float.parseFloat(mileageStr);
        float cost = Float.parseFloat(costStr);

        return new Car(id, isReserved, brand, model, year, mileage, cost);
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

    public boolean getIsReserved()
    {
        return isReserved;
    }

    public void setIsReserved(boolean isReserved)
    {
        this.isReserved = isReserved;
    }
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getMileage() {
        return mileage;
    }

    public void setMileage(float mileage) {
        this.mileage = mileage;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
