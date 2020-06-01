import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class AIGame extends Game {
    // Protected variables for the AI game
    private int turn;
    private int diagonal1;
    private int diagonal2;
    private Set<Position> emptyPositions;
    private Set<Position> emptyCorners;

    public AIGame(int s) {
        super(s);
        turn = 0;
        diagonal1 = 0;
        diagonal2 = 0;
        emptyPositions = new HashSet<>();
        emptyCorners = new HashSet<>();
        for (int i = 0; i < s; ++i) {
            for (int j = 0; j < s; ++j) {
                emptyPositions.add(new Position(i, j));
            }
        }
        emptyCorners.add(new Position(0, 0));
        emptyCorners.add(new Position(0, s - 1));
        emptyCorners.add(new Position(s - 1, 0));
        emptyCorners.add(new Position(s - 1, s - 1));
    }

    @Override
    public boolean isAIGame() {
        return true;
    }

    @Override
    public void play(int x, int y) {
        char[][] matrix = super.getMatrix();
        super.play(x, y);
        ++turn;
        if (x == y) {
            ++diagonal1;
        }
        if (x + y == matrix.length - 1) {
            ++diagonal2;
        }
        emptyPositions.removeIf(pos -> pos.getX() == x && pos.getY() == y);
        emptyCorners.removeIf(pos -> pos.getX() == x && pos.getY() == y);
        notifyObservers();
    }

    protected int getTurn() { return turn; }

    protected int getDiagonal1() {
        return diagonal1;
    }

    protected int getDiagonal2() {
        return diagonal2;
    }

    protected Set<Position> getEmptyPositions() {
        return emptyPositions;
    }

    protected Set<Position> getEmptyCorners() {
        return emptyCorners;
    }

    protected Position randomPosition() {
        return randomFromSet(emptyPositions);
    }

    protected Position randomCorner() {
        return randomFromSet(emptyCorners);
    }

    private static Position randomFromSet(Set<Position> set){
        int size = set.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(Position pos : set) {
            if (i == item){
                return pos;
            }
            ++i;
        }
        throw new IllegalArgumentException("Empty set!");
    }

    public abstract Position position();

    protected static boolean doubleThreat(char[][] matrix, char c) {
        int count = 0;
        if (threatRows(matrix, c) != null) {
            ++count;
        }
        if (threatColumns(matrix, c) != null) {
            ++count;
        }
        if (threatDiagonals(matrix, c) != null) {
            ++count;
        }
        return count >= 2;
    }

    protected static Position threat(char[][] matrix, char c) {
        Position result = threatRows(matrix, c);
        if (result != null) {
            return result;
        }
        result = threatColumns(matrix, c);
        if (result != null) {
            return result;
        }
        return threatDiagonals(matrix, c);
    }

    private static Position threatRows(char[][] matrix, char c) {
        for (int i = 0; i < matrix.length; ++i) {
            int dashCount = 0;
            int dashPosition = 0;
            int charCount = 0;
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == c) {
                    ++charCount;
                }
                else if (matrix[i][j] == '-') {
                    ++dashCount;
                    dashPosition = j;
                }
            }
            if (dashCount ==  1 && charCount == matrix.length - 1) {
                return new Position(i, dashPosition);
            }
        }
        return null;
    }

    private static Position threatColumns(char[][] matrix, char c) {
        for (int j = 0; j < matrix[0].length; ++j) {
            int dashCount = 0;
            int charCount = 0;
            int dashPosition = 0;
            for (int i = 0; i < matrix.length; ++i) {
                if (matrix[i][j] == c) {
                    ++charCount;
                }
                else if (matrix[i][j] == '-') {
                    ++dashCount;
                    dashPosition = i;
                }
            }
            if (dashCount ==  1 && charCount == matrix.length - 1) {
                return new Position(dashPosition, j);
            }

        }
        return null;
    }

    private static Position threatDiagonals(char[][] matrix, char c) {
        int dashCount = 0;
        int charCount = 0;
        int dashPosition = 0;
        for (int i = 0; i < matrix.length; ++i) {
            if(matrix[i][i] == c) {
                ++charCount;
            }
            else if (matrix[i][i] == '-') {
                ++dashCount;
                dashPosition = i;
            }
        }

        if (dashCount ==  1 && charCount == matrix.length - 1) {
            return new Position(dashPosition, dashPosition);
        }

        dashCount = 0;
        charCount = 0;
        dashPosition = 0;
        for (int i = 0; i < matrix.length; ++i) {
            if(matrix[i][matrix.length - i - 1] == c) {
                ++charCount;
            }
            else if(matrix[i][matrix.length - i - 1] == '-') {
                ++dashCount;
                dashPosition = i;
            }
        }
        if (dashCount ==  1 && charCount == matrix.length - 1) {
            return new Position(dashPosition, matrix.length - dashPosition - 1);
        }
        return null;
    }

    public Position initialMove() {
        char[][] matrix = super.getMatrix();
        Position attack = threat(matrix, 'O');
        if (attack != null) {
            return attack;
        }
        Position defense = threat(matrix, 'X');
        if (defense != null) {
            System.out.println("DEFENSE!!");
            return defense;
        }

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == '-') {
                    matrix[i][j] = 'O';
                    if (doubleThreat(matrix, 'O')) {
                        matrix[i][j] = '-';
                        System.out.println("DOUBLE ATTACK!!");
                        return new Position(i, j);
                    }
                    matrix[i][j] = '-';
                }
            }
        }
        return null;
    }
}
