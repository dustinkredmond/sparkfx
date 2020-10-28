package com.dustinredmond.sparkfx.persistence;

/*
 *  Copyright 2020  Dustin K. Redmond
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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