import java.util.Random;

public class EasyAIGame extends AIGame {

    public EasyAIGame(int s) {
        super(s);
    }

    @Override
    public Position position() {
        Position initial = initialMove();
        if (initial != null) {
            return initial;
        }
        System.out.println("RANDOM!!");
        return super.randomPosition();
    }
}
