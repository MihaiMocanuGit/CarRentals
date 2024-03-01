package Repository;

import Domain.Identifiable;

import java.util.HashMap;
import java.util.Map;

public class MemoryRepository<ID,T extends Identifiable<ID>>
        implements IRepository<ID,T>{
    HashMap<ID, T> data = new HashMap<ID, T>();
    @Override
    public T add(T element) {

        return data.put(element.getId(), element);
    }

    @Override
    public T delete(ID id) {
        return data.remove(id);
    }

    @Override
    public T update(ID id, T newElement) {
        if (id == newElement.getId())
            return data.replace(id, newElement);
        else if (data.get(newElement.getId()) == null)
        {
            data.remove(id);
            data.put(newElement.getId(), newElement);
            return null;
        }
        else
        {
            throw new RuntimeException("Tried to update element with given id with another element having a different id " +
                    "that is already populated by another element");
        }
    }

    @Override
    public T findById(ID id) {

        return data.get(id);
    }

    @Override
    public Iterable<T> getAll() {

        return data.values();
    }

    @Override
    public int size()
    {
        return data.size();
    }
}
