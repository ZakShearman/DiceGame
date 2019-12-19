package pink.zak.dicegame.cache;

import pink.zak.dicegame.objects.User;
import pink.zak.dicegame.storage.Storage;

import java.util.HashMap;
import java.util.Map;

public class UserCache {
    private final Storage storage;
    private Map<String, User> userMap = new HashMap<>(); // Used for storing the User object against their name used to identify them.

    public UserCache(Storage storage) {
        this.storage = storage;
    }

    public User authenticateUser(String username, String password) {
        if (this.userMap.containsKey(username)) { // Checks if the user is in the map, and if they are, proceed.
            User user = this.userMap.get(username); // Gets the user from the map.
            if (user.getPassword().equals(password)) { // Checks if the user's password matches what was inputted.
                return user; // Returns the User object.
            }
        }
        return null; // null is always returned in the event of an authentication failure.
    }

    public User registerUser(String username, String password) {
        if (this.userMap.containsKey(username)) {
            return this.authenticateUser(username, password); // Returns a user if they are in the map and can be authenticated. If they can't, returns null.
        }
        this.userMap.put(username, new User(username, password));
        return this.userMap.get(username); // Adds the user to the map if they are not in it, essentially registering them.
    }

    public void save() {
        for (User user : userMap.values()) {
            this.storage.saveUser(user);
        }
    }
}
