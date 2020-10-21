package com.dustinredmond.apifx.persistence;

import java.util.List;

/**
 * An interface indicating that a Class is used for retrieving
 * and manipulating data from the SQLite database
 * @param <T>
 */
public interface DAO<T> {

    /**
     * Updates an object {@code T} in the SQLite database.
     * @param object Object to update
     * @return true, if and only if the operation was successful.
     */
    boolean update(T object);

    /**
     * Removes an object {@code T} from the SQLite database.
     * @param object Object to remove
     * @return true, if and only if the operation was successful.
     */
    boolean remove(T object);

    /**
     * Creates an object of type {@code T} in the SQLite database.
     * @param object Object ot create
     * @return true, if and only if the operation was successful.
     */
    boolean create(T object);

    /**
     * Returns a list of all objects of type {@code T} from the SQLite database.
     * @return List of all objects, otherwise, if none are found an empty list.
     */
    List<T> findAll();

}