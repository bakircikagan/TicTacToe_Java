public abstract class AIGame extends Game {
    public AIGame(int s) {
        super(s);
    }

    @Override
    public boolean isAIGame() {
        return true;
    }

    public abstract int[] position();
}
