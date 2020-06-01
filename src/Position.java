public final class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        if (x < 0 || y < 0){
            throw new IllegalArgumentException("Invalid position " + x + ", " + y);
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return  x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof  Position){
            Position p = (Position) other;
            return x == p.x && y == p.y;
        }
        return false;
    }
}
