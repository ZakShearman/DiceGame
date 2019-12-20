package pink.zak.dicegame;

import com.google.common.collect.Maps;
import pink.zak.dicegame.cache.UserCache;
import pink.zak.dicegame.objects.User;

import java.util.Collections;
import java.util.Map;

public class Leaderboard {
    private final UserCache userCache;
    private Map<String, Integer> leaderboard = Maps.newTreeMap(Collections.reverseOrder()); // tree map will sort the values for us. Reverse it so it sorts from highest to lowest.

    public Leaderboard(UserCache userCache) { // accept the {@link UserCache} in the constructor to get all the users
        this.userCache = userCache;
    }

    public Map<String, Integer> getLeaderboard() { // gets the leaderboard map, containing sorted ids bound to their scores
        return this.leaderboard;
    }

    public void update() { // iterate through the cache to and put them in the leaderboard, replacing previous scores if any.
        for (Map.Entry<String, User> entry : this.userCache.getUsers().entrySet()) {
            this.leaderboard.put(entry.getKey(), entry.getValue().getScore().get());
        }
    }
}