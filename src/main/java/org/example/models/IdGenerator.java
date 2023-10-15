package org.example.models;


public class IdGenerator {

    private long currentId = 0;

    private static IdGenerator instance;


    private IdGenerator() { }


    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public synchronized long generateNewId() {
        return ++currentId;
    }
}

