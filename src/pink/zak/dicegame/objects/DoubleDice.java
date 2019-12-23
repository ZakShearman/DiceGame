package pink.zak.dicegame.objects;

import java.util.concurrent.ThreadLocalRandom;

public class DoubleDice {
    private final int valueOne;
    private final int valueTwo;

    public DoubleDice(int valueOne, int valueTwo) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }

    public int getTotal() {
        return this.valueOne + this.valueTwo;
    }

    public boolean isTotalOdd() {
        return this.getTotal() % 2 == 1;
    }

    public boolean areSame() {
        return this.valueTwo == this.valueTwo;
    }

    public static DoubleDice roll() {
        return new DoubleDice(ThreadLocalRandom.current().nextInt(1, 7),
                ThreadLocalRandom.current().nextInt(1, 7));
    }
}
