package com.example.ex4;


import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Bean ActiveUsers - a DS containing all the active users a timestamp when they were last active
 */
@Component
public class ActiveUsers {

    /**
     * the map of users-->time_active
     */
    private HashMap<String, Date> users;

    /**
     * Constructor
     */
    ActiveUsers() {
        users = new HashMap<>();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            /**
             * Will every 9 seconds update the users based on who was active in the last 10 seconds
             */
            @Override
            public synchronized void run() {
                HashMap<String, Date> updatedUsers = new HashMap<>();
                for (Map.Entry<String, Date> entry : users.entrySet()) {
                    if(new Date().getTime() - entry.getValue().getTime() < 10000)
                        updatedUsers.put(entry.getKey(), entry.getValue());
                }
                users = updatedUsers;
            }
        }, 0, 9000);
    }

    /**
     * updates the users timestamp to NOW
     * @param user to activate
     */
    public synchronized void activateUser(String user) {
        users.put(user, new Date());
    }

    /**
     * returns a list of just the active users usernames
     * @return said list
     */
    public synchronized ArrayList<String> calcActiveUsers() {
        ArrayList<String> activeUsers = new ArrayList<>();

        for (Map.Entry<String, Date> entry : users.entrySet()) {
            activeUsers.add(entry.getKey());
        }
        return activeUsers;
    }

    /**
     * removes user from list of active users
     * @param username to remove
     */
    public synchronized void deactivateUser(String username) {
        users.remove(username);
    }

    /**
     * checks if user is active
     * @param username to check
     * @return true if active, else false
     */
    public synchronized boolean checkForUser(String username) {
        return users.containsKey(username);
    }
}
