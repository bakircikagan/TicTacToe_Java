import java.util.HashSet;
import java.util.Set;

public class Game implements Subject {
    // PROPERTIES
    private Set<Observer> observers;
    private int size;
    public static boolean XPlays = true;

    // Protected variables for the AI game
    private int turn;
    private char[][] matrix;
    private int diagonal1;
    private int diagonal2;

    // CONSTRUCTORS
    public Game() {
        this(3);
    }

    public Game(int s) {
        size = s;
        matrix = new char[size][size];
        observers = new HashSet<>();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                matrix[i][j] = '-';
            }
        }
        turn = 0;
        diagonal1 = 0;
        diagonal2 = 0;
    }

    // METHODS
    public boolean isAIGame() {
        return false;
    }

    public void play( int x, int y) {
        if (matrix[x][y] != '-') {
            throw new IllegalArgumentException("Playing to non empty space, position: " + x + ", " + y);
        }
        char c = next();
        matrix[x][y] = c;
        XPlays = !XPlays;
        ++turn;
        if (x == y) {
            ++diagonal1;
        }
        if (x + y == size - 1) {
            ++diagonal2;
        }
        notifyObservers();
    }

    public char next() {
        return XPlays ? 'X' :'O';
    }

    public boolean isOver() {
        return won('X') || won('O') || tie();
    }

    public boolean tie() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if(matrix[i][j] == '-')
                    return false;
            }
        }
        return true;
    }

    public boolean won(char c) {
        return checkColumns(matrix, c) || checkRows(matrix, c) || checkDiagonals(matrix, c);
    }

    private static boolean checkColumns(char[][] matrix, char c) {
        boolean allCorrect = true;
        for (int j = 0; j < matrix[0].length; ++j) {
            for (int i = 0; allCorrect && i <  matrix.length; ++i) {
                if(matrix[i][j] != c) {
                    allCorrect = false;
                }
            }
            if(allCorrect)
                return true;
            allCorrect = true;
        }
        return false;
    }

    private static boolean checkRows(char[][] matrix, char c) {
        boolean allCorrect = true;
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; allCorrect && j < matrix[0].length; ++j) {
                if(matrix[i][j] != c) {
                    allCorrect = false;
                }
            }
            if(allCorrect)
                return true;
            allCorrect = true;
        }
        return false;
    }

    private static boolean checkDiagonals(char[][] matrix, char c) {
        boolean diagonal1 = true;
        boolean diagonal2 = true;
        for (int i = 0; i < matrix.length; ++i) {
            if(matrix[i][i] != c) {
                diagonal1 = false;
                break;
            }
        }

        for (int i = 0; i < matrix.length; ++i) {
            if(matrix[i][matrix.length - i - 1] != c) {
                diagonal2 = false;
                break;
            }
        }
        return diagonal1 || diagonal2;

    }

    public int size() {
        return size;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(x -> x.update(this));
    }

    protected char[][] getMatrix() {
        return matrix;
    }
    protected int getTurn() { return turn; }

    protected int getDiagonal1() {
        return diagonal1;
    }

    protected int getDiagonal2() {
        return diagonal2;
    }

}