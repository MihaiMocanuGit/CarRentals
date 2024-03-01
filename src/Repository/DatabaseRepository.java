package Repository;


import java.sql.*;
import Domain.Identifiable;
import org.sqlite.SQLiteDataSource;

import java.io.FileNotFoundException;

public abstract class DatabaseRepository<ID, T extends Identifiable<ID>> extends PersistentRepository<ID, T> {

    protected static Connection conn = null;
    public DatabaseRepository(String filename) throws FileNotFoundException {
        super(filename);
        openConnection();
    }
    /**
     * Gets a connection to the database.
     * If the underlying connection is closed, it creates a new connection. Otherwise, the current instance is returned.
     */
    public void openConnection() {
        try {
            // with DataSource
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(persistentFilePath);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Closes the underlying connection to the in-memory SQLite instance.
     */
    public void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFromFile() throws FileNotFoundException {
       /*
       try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * from Cars"); ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Car car = new Car(rs.getInt("c_id"), rs.getBoolean("isReserved"), rs.getString("brand"),
                            rs.getString("model"), rs.getInt("year"), rs.getFloat("mileage"),
                            rs.getFloat("cost"));
                    super.add(car);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }*/
    }

    @Override
    public void writeToFile() {

    }
}
