import java.util.HashMap;
import java.util.Random;
import java.util.Map;
import java.util.HashSet;

public final class HardAIGame extends EasyAIGame {

    public HardAIGame(int s) {
        super(s);
    }

    @Override
    public int[] position() {
        char[][] matrix = super.getMatrix();
        int[] attack = threat(matrix, 'O');
        if (attack != null) {
            return attack;
        }
        int[] defense = threat(matrix, 'X');
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
                        return new int[]{i, j};
                    }
                    matrix[i][j] = '-';
                }
            }
        }

        int turn = super.getTurn();
        if (turn == 0) {
            return randomCorner(matrix.length);
        }
        int mid = matrix.length / 2;

        if (turn == 1) {
            if (matrix[mid][mid] == '-') {
                return new int[]{mid, mid};
            }
            else {
                return randomCorner(matrix.length);
            }
        }

        else if (turn == 2 && matrix[mid][mid] == '-') {
            // Check the corners
            int lim = matrix.length - 1;
            HashSet<int[]> availableCorners = new HashSet<int[]>();
            HashMap<int[], Character> cornerStatus = new HashMap<int[], Character> ();
            cornerStatus.put(new int[]{0, 0}, matrix[0][0]);
            cornerStatus.put(new int[]{0, lim}, matrix[0][lim]);
            cornerStatus.put(new int[]{lim, 0}, matrix[lim][0]);
            cornerStatus.put(new int[]{lim, lim}, matrix[lim][lim]);
            int rowNum = 0;
            int colNum = 0;
            for (Map.Entry<int[], Character> entry : cornerStatus.entrySet()) {
                if (entry.getValue() == '-') {
                    availableCorners.add(entry.getKey());
                }
                if (entry.getValue() == 'O') {
                    int[] temp = entry.getKey();
                    rowNum = temp[0];
                    colNum = temp[1];
                }
            }
            if (availableCorners.size() == 2) {
                return availableCorners.iterator().next();
            }
            else {
                boolean empty = true;
                // Check row
                for (int j = 0; empty && j < matrix.length; ++j ) {
                    if(matrix[rowNum][j] == 'X') {
                        empty = false;
                    }
                }
                if (empty) {
                    for (int[] position : availableCorners ) {
                        if(position[0] == rowNum) {
                            return position;
                        }
                    }
                }
                // Check column
                empty = true;
                for (int i = 0; empty && i < matrix.length; ++i ) {
                    if(matrix[i][colNum] == 'X') {
                        empty = false;
                    }
                }
                if (empty) {
                    for (int[] position : availableCorners ) {
                        if(position[1] == colNum) {
                            return position;
                        }
                    }
                }
            }
        }
        HashSet<int[]> middles = new HashSet<int[]>();
        middles.add(new int[]{0, mid});
        middles.add(new int[]{mid, 0});
        middles.add(new int[]{mid, matrix.length - 1});
        middles.add(new int[]{matrix.length - 1, mid});

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == '-') {
                    matrix[i][j] = 'X';
                    if (doubleThreat(matrix, 'X')) {
                        matrix[i][j] = '-';
                        if (super.getDiagonal1() == matrix.length || super.getDiagonal2() == matrix.length) {
                            for (int[] pos: middles) {
                                if (matrix[pos[0]][pos[1]] == '-'){
                                    System.out.println("DOUBLE DEFENSE SPECIAL!!");
                                    return pos;
                                }
                            }
                        }
                        else {
                            System.out.println("DOUBLE DEFENSE NORMAL!!");
                            return new int[]{i, j};
                        }
                    }
                    matrix[i][j] = '-';
                }
            }
        }

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == '-') {
                    matrix[i][j] = 'O';
                    int[] threat = threat(matrix, 'O');
                    if (threat != null) {
                        matrix[i][j] = '-';
                        System.out.println("SINGLE ATTACK!!");
                        return threat;
                    }
                    matrix[i][j] = '-';
                }
            }
        }
        System.out.println("RANDOM !!");
        return super.position();
    }

    private static int[] randomCorner(int size) {
        Random r = new Random();
        int x =  r.nextBoolean() ? size - 1 : 0;
        int y = r.nextBoolean() ? size - 1 : 0;
        return new int[] {x, y};
    }

    private static boolean doubleThreat(char[][] matrix, char c) {
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

    private static int[] threat(char[][] matrix, char c) {
        int[] result = threatRows(matrix, c);
        if (result != null) {
            return result;
        }
        result = threatColumns(matrix, c);
        if (result != null) {
            return result;
        }
        return threatDiagonals(matrix, c);
    }

    private static int[] threatRows(char[][] matrix, char c) {
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
                return new int[] {i, dashPosition};
            }

        }
        return null;
    }

    private static int[] threatColumns(char[][] matrix, char c) {
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
                return new int[] {dashPosition, j};
            }

        }
        return null;
    }

    private static int[] threatDiagonals(char[][] matrix, char c) {
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
            return new int[] {dashPosition, dashPosition};
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
            return new int[] {dashPosition, matrix.length - dashPosition - 1};
        }
        return null;
    }
}