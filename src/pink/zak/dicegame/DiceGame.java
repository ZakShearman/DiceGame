package pink.zak.dicegame;

public class DiceGame {

    public static void main(String[] args) {
        // Creates a Controller.
        Controller controller = new Controller();
        // Stops us from running everything in static.
        controller.onEnable();
    }
}
