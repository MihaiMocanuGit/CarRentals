package Repository;

import Domain.Car;
import Domain.CarReservation;
import Domain.Person;
import Utils.ParsingHelper;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ReservationsRepoDatabase extends DatabaseRepository<Integer, CarReservation> {

    public ReservationsRepoDatabase(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public CarReservation add(CarReservation elem) {
        try {
            //store the old element with the same id and then delete it
            CarReservation oldElem = this.delete(elem.getId());

            //add the new element, after this it would look like the old element was overwritten
            try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO CarReservations VALUES (?, ?, ?, ?)")) {
                statement.setInt(1, elem.getId());
                statement.setString(2, elem.getRenter().getFamilyName());
                statement.setString(3, elem.getRenter().getFirstName());
                statement.setString(4, elem.getRenter().getId());
                statement.executeUpdate();
            }
            return oldElem;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CarReservation delete(Integer id) {
        CarReservation oldElem = this.findById(id);
        //delete the old element from the database
        try (PreparedStatement statement = conn.prepareStatement("DELETE FROM CarReservations WHERE cr_id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return oldElem;
    }

    @Override
    public CarReservation update(Integer id, CarReservation newElement) {
        try {
            CarReservation oldReservation = null;
            if (Objects.equals(id, newElement.getId())) {
                oldReservation = this.findById(id);
                try (PreparedStatement statement = conn.prepareStatement(
                        "UPDATE CarReservations " +
                                "SET familyName = ?, firstName = ?, personal_id = ?" +
                                "WHERE cr_id = ?;")) {
                    statement.setString(1, newElement.getRenter().getFamilyName());
                    statement.setString(2, newElement.getRenter().getFirstName());
                    statement.setString(3, newElement.getRenter().getId());
                    statement.setInt(4, newElement.getId());

                    statement.executeUpdate();
                }
            } else if (this.findById(newElement.getId()) == null) {
                oldReservation = this.delete(id);
                this.add(newElement);
            } else {
                throw new RuntimeException("Tried to update element with given id with another element having a different id " +
                        "that is already populated by another element");
            }

            return oldReservation;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CarReservation findById(Integer id) {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * from CarReservations where cr_id = ?");) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            return ParsingHelper.parseCarReservation(rs, conn, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<CarReservation> getAll() {
        ArrayList<CarReservation> reservations = new ArrayList<>();

        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * from CarReservations");
                 ResultSet rs = statement.executeQuery();)
            {
                while (rs.next()) {
                    CarReservation reservation = ParsingHelper.parseCarReservation(rs, conn, rs.getInt("cr_id"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservations;
    }

    @Override
    public int size() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) from CarReservations");
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