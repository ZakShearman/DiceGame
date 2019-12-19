package pink.zak.dicegame;

public class DiceGame {

    public static void main(String[] args) {
        Controller controller = new Controller(); // Creates a Controller.
        controller.onEnable(); // Stops us from running everything in static.
    }
}
