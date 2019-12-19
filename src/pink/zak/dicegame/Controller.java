package pink.zak.dicegame;

import pink.zak.dicegame.cache.UserCache;
import pink.zak.dicegame.objects.Game;
import pink.zak.dicegame.objects.User;
import pink.zak.dicegame.storage.Storage;

import java.util.Scanner;

public class Controller {
    private final UserCache userCache;
    private final Storage storage;
    private User playerOne;

    public Controller() {
        this.storage = new Storage();
        this.userCache = new UserCache(this.storage); // Create an instance of the UserCache.
    }

    public void onEnable() {
        this.print("Welcome to my dice game!");
        this.loginOrRegister(Player.PLAYER1); // Start the login sequence.
    }

    private void loginOrRegister(Player player) {
        this.print("[%s] Please enter 'login' to login or 'register' to register.", player.getName()); // player.getName# used for getting [Player 1] or [Player 2] using the Enum.
        Scanner scanner = new Scanner(System.in); // Creates the scanner, used later on.
        String nextLine = scanner.nextLine(); // Sets nextLine to the value of what is typed.
        if (nextLine.equalsIgnoreCase("login")) { // Login sequence
            this.print("[%s] Please enter your username:", player.getName());
            String username = scanner.nextLine();
            this.print("[%s] Please enter your password:", player.getName());
            String password = scanner.nextLine(); // Gather the username and password information.

            User user = this.userCache.authenticateUser(username, password); // Use the UserCache's authentication check.
            if (user == null) { // null meaning the user could not be authenticated.
                this.print("The username or password entered was incorrect. Starting again.");
                this.loginOrRegister(player); // Restart the login sequence.
            } else if (player == Player.PLAYER1) {
                this.playerOne = user;
                this.loginOrRegister(Player.PLAYER2); // Move the loginOrRegister sequence to the second player if it was the first player before.
            } else {
                Game game = new Game(this.playerOne, user, this.userCache); // Submit info into the game.
                game.initiate(); // Start the game.
            }
        } else if (nextLine.equalsIgnoreCase("register")) { // Register sequence
            this.print("[%s] Please enter your username:", player.getName());
            String username = scanner.nextLine();
            this.print("[%s] Please enter your password:", player.getName());
            String password = scanner.nextLine(); // Gather the username and password information.

            User user = this.userCache.registerUser(username, password); // Use the UserCache's registration method.
            if (user == null) {
                this.print("[%s] The username was already taken and the password did not match. Starting again.", player.getName());
                this.loginOrRegister(player);
            }
            if (player == Player.PLAYER1) {
                this.playerOne = user;
                this.loginOrRegister(Player.PLAYER2); // Move the loginOrRegister sequence to the second player if it was the first player before.
            } else {
                Game game = new Game(this.playerOne, user, this.userCache); // Submit info into the game.
                game.initiate(); // Start the game.
            }
        } else { // Erroneous input
            this.print("[%s] Your input could not be recognised. Starting again.", player.getName());
            this.print(" ");
            this.loginOrRegister(player); // Loops back to the start for the player.
        }
    }

    private void print(String string, Object... objects) {
        System.out.println(String.format(string, objects)); // Print and replace %s with another string.
    }

    enum Player { // Used for knowing what the current player is during the loginOrRegister sequence.
        PLAYER1("Player One"),
        PLAYER2("Player Two");

        private final String name;

        Player(String name) {
            this.name = name;
        } // Assigns a name in the constructor.

        public String getName() {
            return this.name;
        } // Returns the name assigned with the Enum variable.
    }
}
