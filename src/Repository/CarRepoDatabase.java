package Repository;

import Domain.Car;
import Domain.Identifiable;
import Utils.ParsingHelper;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class CarRepoDatabase extends DatabaseRepository<Integer, Car> {
    public CarRepoDatabase(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public Car add(Car elem) {
        try {
            //store the old element with the same id and then delete it
            Car oldElem = this.delete(elem.getId());

            //add the new element, after this it would look like the old element was overwritten
            try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Cars VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                statement.setInt(1, elem.getId());
                statement.setBoolean(2, elem.getIsReserved());
                statement.setString(3, elem.getBrand());
                statement.setString(4, elem.getModel());
                statement.setInt(5, elem.getYear());
                statement.setFloat(6, elem.getMileage());
                statement.setFloat(7, elem.getCost());
                statement.executeUpdate();
            }
            return oldElem;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Car delete(Integer id) {
        Car oldElem = this.findById(id);
        //delete the old element from the database
        try (PreparedStatement statement = conn.prepareStatement("DELETE FROM Cars WHERE c_id=?")) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return oldElem;
    }

    @Override
    public Car update(Integer id, Car newElement) {
        try {
            Car oldCar = null;
            if (Objects.equals(id, newElement.getId())) {
                oldCar = this.findById(id);
                try (PreparedStatement statement = conn.prepareStatement(
                        "UPDATE Cars " +
                                "SET isReserved = ?, brand = ?, model = ?, year = ?, mileage = ?, cost = ?  " +
                                "WHERE c_id = ?;"))
                {
                    statement.setBoolean(1, newElement.getIsReserved());
                    statement.setString(2, newElement.getBrand());
                    statement.setString(3, newElement.getModel());
                    statement.setInt(4, newElement.getYear());
                    statement.setFloat(5, newElement.getMileage());
                    statement.setFloat(6, newElement.getCost());
                    statement.setInt(7, newElement.getId());

                    statement.executeUpdate();
                }
            } else if (this.findById(newElement.getId()) == null) {
                oldCar = this.delete(id);
                this.add(newElement);
            } else {
                throw new RuntimeException("Tried to update element with given id with another element having a different id " +
                        "that is already populated by another element");
            }

            return oldCar;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car findById(Integer id) {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * from Cars where c_id = ?");){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            return ParsingHelper.parseCar(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Iterable<Car> getAll() {
        ArrayList<Car> cars = new ArrayList<>();

        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * from Cars"); ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Car car = ParsingHelper.parseCar(rs);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cars;
    }

    @Override
    public int size() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) from Cars");
             ResultSet rs = statement.executeQuery();)
        {
            return rs.getInt(1);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

}
