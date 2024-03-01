package Repository;

import Domain.Identifiable;

import java.io.FileNotFoundException;

public abstract class PersistentRepository<ID, T extends Identifiable<ID>> extends MemoryRepository<ID, T>{
    protected String persistentFilePath;

    public PersistentRepository(String persistentFilePath) throws FileNotFoundException{
        this.persistentFilePath = persistentFilePath;
        readFromFile();
    }

    protected abstract void readFromFile() throws FileNotFoundException;

    //it must be public, because if a user modifies an element using Repo.findById(), the user needs the ability to write
    //to file after. Moreover, when the app closes, we must save to file.
    public abstract void writeToFile();
    @Override
    public T add(T elem) {
        T result = super.add(elem);
        writeToFile();

        return result;
    }
    @Override
    public T delete(ID id){
        T result = super.delete(id);
        writeToFile();

        return result;
    }
    @Override
    public T update(ID id, T elem){
        T result = super.update(id, elem);
        writeToFile();

        return result;
    }
}