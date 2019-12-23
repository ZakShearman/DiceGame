package pink.zak.dicegame.objects;

import pink.zak.dicegame.cache.UserCache;

public class Game {
    private final User player1;
    private final User player2;
    private final UserCache userCache;
    private int round;

    public Game(User player1, User player2, UserCache userCache) { // Input the players to the Game object.
        this.userCache = userCache;
        this.player1 = player1; // Assign player1 to the player1 variable - passed in from Controller.
        this.player2 = player2; // Assign player2 to the player2 variable - passed in from Controller.
        this.round = 1;
    }

    public void initiate() { // Start the game.
        this.print("Proceeding into game...");
        while (this.round <= 5) { // Loops through all of the rounds in the game. Prevents more than 5 rounds.
            this.print("");
            this.print("[Round Start] Round %s is starting!", this.round);
            this.print("");
            int playerOneRoundScore = this.roundForPlayer(this.player1); // Performs all of the game logic for player one and player two, returning the total score.
            int playerTwoRoundScore = this.roundForPlayer(this.player2);

            // Prints statistics for the player's rounds.
            this.print("[%s] You gained %s points this round.", this.player1.getUsername(), playerOneRoundScore);
            this.print("[%s] You now have a total score of %s", this.player1.getUsername(), this.player1.getScore().intValue());
            this.print(" ");
            this.print("[%s] You gained %s points this round.", this.player2.getUsername(), playerTwoRoundScore);
            this.print("[%s] You now have a total score of %s", this.player2.getUsername(), this.player2.getScore().intValue());

            this.print("");
            this.print("[Round End] Round %s has now ended!", this.round);
            this.round++; // Adds a number to the round, making sure it is only run 5 times.
        }

        int finalPlayerOneScore = this.player1.getScore().intValue();
        int finalPlayerTwoScore = this.player2.getScore().intValue();
        this.print("");
        if (this.player1.getScore().intValue() > this.player2.getScore().intValue()) { // If player one has the higher score.
            // Print out winning and stats.
            this.print("%s is the winner of the game with a score of %s.", this.player1.getUsername(), finalPlayerOneScore);
            this.print("%s lost with a score of %s.", this.player2.getUsername(), finalPlayerTwoScore);
            this.print("This means that %s lost by %s points.", this.player2.getUsername(), finalPlayerOneScore - finalPlayerTwoScore);
        } else if (this.player1.getScore().intValue() < this.player2.getScore().intValue()) { // If player two has the higher score.
            this.print("%s is the winner of the game with a score of %s.", this.player2.getUsername(), finalPlayerTwoScore);
            this.print("%s lost with a score of %s.", this.player1.getUsername(), finalPlayerOneScore);
            this.print("This means that %s lost by %s points.", this.player1.getUsername(), finalPlayerTwoScore - finalPlayerOneScore);
        } else {
            this.print("The game resulted in a draw!");
            this.print("Both %s and %s had %s points!", this.player1.getUsername(), this.player2.getUsername(), this.player1.getScore());
        }
        this.player1.addFinalGameScore(this.player1.getScore().intValue()); // Adds game score for use in the leader board.
        this.player2.addFinalGameScore(this.player2.getScore().intValue());
        this.userCache.save(); // Saves the user.
    }

    private void print(String string, Object... objects) {
        System.out.println(String.format(string, objects)); // Print method with String formatting support.
    }

    public int roundForPlayer(User player) {
        DoubleDice playerOneDice = DoubleDice.roll(); // Roll the dice
        int roundScore; // Initializes an integer for storing of the player's round score.

        player.addToScore(playerOneDice.getTotal());
        if (playerOneDice.isTotalOdd()) { // If the total is odd, removes 5 from the player's score and returns -5 as the round score.
            player.removeFromScore(5);
            roundScore = -5;
        } else { // If the total is even, adds 10 to the player's score and returns 10 as the round score.
            player.addToScore(10);
            roundScore = 10;
        }
        if (playerOneDice.areSame()) { // Re rolls the dice for the player if they are the same (e.g 5 and 5 or 2 and 2)
            DoubleDice diceRoll = DoubleDice.roll();
            player.addToScore(diceRoll.getTotal());
            roundScore = roundScore + diceRoll.getTotal();
        }
        return roundScore; // Returns the total amount that the player has gained in the round. For use with displaying stats.
    }
}
