package Repository;

import Domain.Car;

import java.io.*;
import java.util.HashMap;

public class CarRepoBinary extends PersistentRepository<Integer, Car> {

    public CarRepoBinary(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public void readFromFile() {


        //if the file does not exist
        File file = new File(persistentFilePath);
        if (! file.exists())
        {
            try {
                HashMap<Integer, Car> emptyMap = new HashMap<>();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(persistentFilePath));
                oos.writeObject(emptyMap);
            } catch (IOException e) {
                throw new Error("Could not create new empty binary file!");
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(persistentFilePath)))
        {
            data = (HashMap<Integer, Car>) ois.readObject();
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
