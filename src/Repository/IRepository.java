package Repository;

import Domain.Identifiable;

public interface IRepository<ID,T extends Identifiable<ID>> {

    /**
     * Adds the given element to the repository
     * @param element Object extending from Identifiable
     * @return The previous value associated with key, or null if there was no mapping for key.
     */
    T add(T element);

    /**
     * Removes the given element from the repository
     * @param id - The id of the object inherited from the Identifiable interface
     * @return The previous value associated with id, or null if there was no mapping for id.
     */
    T delete(ID id);

    /**
     * Updates the element in the repository associated to the given id with the new given element
     * @param id
     * @param newElement
     * @return the previous value associated with the specified id, or null if there was no mapping for the id.
     */
    T update(ID id, T newElement) throws RuntimeException;

    /**
     * Gets the element having the given id in the repo
     * @param id
     * @return The value associated with the specified id, or null if there was no mapping for the id.
     */
    T findById(ID id);

    Iterable<T> getAll();

    int size();
}
