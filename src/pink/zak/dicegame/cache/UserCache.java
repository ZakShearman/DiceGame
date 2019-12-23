package pink.zak.dicegame.cache;

import pink.zak.dicegame.objects.User;
import pink.zak.dicegame.storage.Storage;

import java.util.HashMap;
import java.util.Map;

public class UserCache {
    private final Storage storage;
    // Used for storing the User object against their name used to identify them.
    private Map<String, User> userMap = new HashMap<>();

    public UserCache(Storage storage) {
        this.storage = storage;
    }

    public Map<String, User> getUsers() {
        return this.userMap;
    }

    public User authenticateUser(String username, String password) {
        String usernameLower = username.toLowerCase();
        // Checks if the user is in the map, and if they are, proceed.
        if (this.userMap.containsKey(usernameLower)) {
            // Gets the user from the map.
            User user = this.userMap.get(usernameLower);
            // Checks if the user's password matches what was inputted.
            if (user.getPassword().equals(password)) {
                return user; // Returns the User object.
            }
        }
        return null; // null is always returned in the event of an authentication failure.
    }

    public User registerUser(String username, String password) {
        String usernameLower = username.toLowerCase();
        if (this.userMap.containsKey(usernameLower)) {
            // Returns a user if they are in the map and can be authenticated. If they can't, returns null.
            return this.authenticateUser(usernameLower, password);
        }
        this.userMap.put(usernameLower, new User(usernameLower, password));
        // Adds the user to the map if they are not in it, essentially registering them.
        return this.userMap.get(usernameLower);
    }

    public void cachedUser(User user) {
        if (!this.userMap.containsValue(user)) {
            this.userMap.put(user.getUsername(), user);
        }
    }

    public void save() {
        for (User user : userMap.values()) {
            this.storage.saveUser(user);
        }
    }
}
