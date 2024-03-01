package Repository;

import Domain.Car;
import Utils.ParsingHelper;

import java.io.*;

public class CarRepoText extends PersistentRepository<Integer, Car> {

    public CarRepoText(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public void readFromFile() throws FileNotFoundException {
        //if the file does not exist, create a new one
        try {
            File file = new File(persistentFilePath);
            file.createNewFile();
        } catch (IOException e) {
            throw new Error("Could not create new empty binary file!");
        }


        try (BufferedReader reader = new BufferedReader(new FileReader(this.persistentFilePath))){
            super.data.clear();

            String line;
            while ( (line = reader.readLine()) != null){
                Car car = Car.parseCar(line);

                if (car != null)
                    super.add(car);
                else
                    throw new  ParsingHelper.ParseError("Could not parse text file, it might be corrupted:\n" +
                                                        "\tInvalid string: \""+ line + "\"");
            }
        }
        catch (FileNotFoundException f){
            throw new FileNotFoundException("File not found (" + persistentFilePath + ")!");
        } catch (IOException e) {
            throw new ParsingHelper.ParseError("Could not read line from text file:\n" +
                                                "\tFilepath: \""+ persistentFilePath + "\"");
        }
    }
    @Override
    public void writeToFile(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.persistentFilePath))){

            Iterable<Car> cars = this.getAll();
            for (Car car : cars)
                writer.write(car.toString() + ";\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
