package Repository;

import Domain.CarReservation;

import java.io.*;
import java.util.HashMap;

public class ReservationsRepoBinary extends PersistentRepository<Integer, CarReservation> {

    public ReservationsRepoBinary(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public void readFromFile() {


        //if the file does not exist
        File file = new File(persistentFilePath);
        if (! file.exists())
        {
            try {
                HashMap<Integer, CarReservation> emptyMap = new HashMap<>();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(persistentFilePath));
                oos.writeObject(emptyMap);
            } catch (IOException e) {
                throw new Error("Could not create new empty binary file!");
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(persistentFilePath)))
        {
            data = (HashMap<Integer, CarReservation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new Error("Could not read binary file!");
        }
    }
    @Override
    public void writeToFile()
    {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(persistentFilePath));
            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            throw new Error("Could not write to binary file!");
        }
    }
}
