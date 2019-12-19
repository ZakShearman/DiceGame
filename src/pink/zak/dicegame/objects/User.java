package pink.zak.dicegame.objects;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private final String username;
    private final String password;
    private AtomicInteger score;
    private Multiset<Integer> gameScores = HashMultiset.create();

    public User(String username, String password, Multiset<Integer> gameScores) { // Take in the username or password of a user.
        this.username = username; // Assign username to variable
        this.password = password; // Assign password to variable.
        this.gameScores = gameScores;
        this.score = new AtomicInteger(0); // An AtomicInteger must be initiated with a value.
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = new AtomicInteger(0);
    }

    // Getters
    public AtomicInteger getScore() {
        return this.score;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void addFinalGameScore(int score) {
        this.gameScores.add(score);
    }

    public Multiset<Integer> getGameScores() {
        return this.gameScores;
    }

    // Setters
    public void setScore(AtomicInteger score) {
        this.score = score;
        if (this.score.intValue() <= 0) {
            this.score.set(0);
        }
    }

    // Other
    public void addToScore(int amount) {
        this.score.set(this.score.intValue() + amount);
    }

    public void removeFromScore(int amount) {
        this.score.set(this.score.intValue() - amount);
        if (this.score.intValue() <= 0) {
            this.score.set(0);
        }
    }
}
