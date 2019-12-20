package pink.zak.dicegame.objects;

import pink.zak.dicegame.cache.UserCache;

public class Game {
    private final User player1;
    private final User player2;
    private final UserCache userCache;
    private int round;

    public Game(User player1, User player2, UserCache userCache) { // Input the players to the Game object.
        this.userCache = userCache;
        this.player1 = player1; // Assign player1 to the player1 variable
        this.player2 = player2; // Assign player2 to the player2 variable.
        this.round = 1;
    }

    public void initiate() { // Start the game.
        this.print("Proceeding into game...");
        while (this.round < 6) {
            this.print("");
            this.print("[Round Start] Round %s is starting!", this.round);
            this.print("");
            int playerOneRoundScore = this.roundForPlayer(this.player1);
            int playerTwoRoundScore = this.roundForPlayer(this.player2);

            this.print("[%s] You gained %s points this round.", player1.getUsername(), playerOneRoundScore);
            this.print("[%s] You now have a total score of %s", player1.getUsername(), this.player1.getScore().intValue());
            this.print(" ");
            this.print("[%s] You gained %s points this round.", player2.getUsername(), playerTwoRoundScore);
            this.print("[%s] You now have a total score of %s", player2.getUsername(), this.player2.getScore().intValue());

            this.print("");
            this.print("[Round End] Round %s has now ended!", this.round);
            this.round++;
        }

        int finalPlayerOneScore = this.player1.getScore().intValue();
        int finalPlayerTwoScore = this.player2.getScore().intValue();
        this.print("");
        if (this.player1.getScore().intValue() > this.player2.getScore().intValue()) {
            this.print("%s is the winner of the game with a score of %s.", player1.getUsername(), finalPlayerOneScore);
            this.print("%s lost with a score of %s.", player2.getUsername(), finalPlayerTwoScore);
            this.print("This means that %s lost by %s points.", player2.getUsername(), finalPlayerOneScore - finalPlayerTwoScore);
        } else {
            this.print("%s is the winner of the game with a score of %s.", player2.getUsername(), finalPlayerTwoScore);
            this.print("%s lost with a score of %s.", player1.getUsername(), finalPlayerOneScore);
            this.print("This means that %s lost by %s points.", player1.getUsername(), finalPlayerTwoScore - finalPlayerOneScore);
        }
        this.player1.addFinalGameScore(this.player1.getScore().intValue());
        this.player2.addFinalGameScore(this.player2.getScore().intValue());
        this.userCache.save();
    }

    public void terminate() {

    }

    private void print(String string, Object... objects) {
        System.out.println(String.format(string, objects)); // Print method with String formatting support.
    }

    public int roundForPlayer(User player) {
        DoubleDice playerOneDice = DoubleDice.roll(); // Roll the dice
        int roundScore;

        player.addToScore(playerOneDice.getTotal());
        if (playerOneDice.isTotalOdd()) {
            player.removeFromScore(5);
            roundScore = 0;
        } else {
            player.addToScore(10);
            roundScore = 10;
        }
        if (playerOneDice.areSame()) {
            DoubleDice diceRoll = DoubleDice.roll();
            player.addToScore(diceRoll.getTotal());
            roundScore = roundScore + diceRoll.getTotal();
        }
        return roundScore;
    }
}