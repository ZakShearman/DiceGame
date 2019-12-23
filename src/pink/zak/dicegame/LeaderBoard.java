package pink.zak.dicegame;

import com.google.common.collect.Maps;
import pink.zak.dicegame.cache.UserCache;
import pink.zak.dicegame.objects.User;

import java.util.Collections;
import java.util.Map;

public class LeaderBoard {
    private final UserCache userCache;
    // The tree map will automatically sort the values. Reverses so that it goes from highest to lowest.
    private Map<String, Integer> leaderBoard = Maps.newTreeMap(Collections.reverseOrder());

    public LeaderBoard(UserCache userCache) { // accept the {@link UserCache} in the constructor to get all the users
        this.userCache = userCache;
    }

    public Map<String, Integer> getLeaderBoard() { // gets the leaderboard map, containing sorted ids bound to their scores
        return this.leaderBoard;
    }

    public void update() {
        // Iterates through all users in the cache.
        for (Map.Entry<String, User> entry : this.userCache.getUsers().entrySet()) {
            // Loops through every user's scores.
            for (int score : entry.getValue().getGameScores()) {
                // Puts the score of the user into the leaderBoard Map.
                this.leaderBoard.put(entry.getKey(), score);
            }
        }
    }
}