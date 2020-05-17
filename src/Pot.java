import javax.swing.JButton;

public final class Pot extends JButton{
    private final int x;
    private final int y;

    public Pot(String text, int x, int y) {
        super(text);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}