import java.util.*;

public final class HardAIGame extends AIGame {

    public HardAIGame(int s) {
        super(s);
    }

    @Override
    public Position position() {
        Position initial = initialMove();
        if (initial != null) {
            return initial;
        }
        char[][] matrix = super.getMatrix();
        int turn = super.getTurn();
        if (turn == 0) {
            return super.randomCorner();
        }
        int mid = matrix.length / 2;
        boolean middleEmpty = matrix[mid][mid] == '-';
        if (turn == 1) {
            return middleEmpty ? new Position(mid, mid) : super.randomCorner();
        }

        else if (turn == 2 && middleEmpty) {
            // Check the corners
            int lim = matrix.length - 1;
            HashMap<Position, Character> cornerStatus = new HashMap<Position, Character> ();
            cornerStatus.put(new Position(0, 0), matrix[0][0]);
            cornerStatus.put(new Position(0, lim), matrix[0][lim]);
            cornerStatus.put(new Position(lim, 0), matrix[lim][0]);
            cornerStatus.put(new Position(lim, lim), matrix[lim][lim]);
            int rowNum = 0;
            int colNum = 0;
            for (Map.Entry<Position, Character> entry: cornerStatus.entrySet()) {
                if (entry.getValue() == 'O') {
                    Position temp = entry.getKey();
                    rowNum = temp.getX();
                    colNum = temp.getY();
                }
            }
            Set<Position> emptyCorners = super.getEmptyCorners();
            if (emptyCorners.size() == 2) {
                return emptyCorners.iterator().next();
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
                    for (Position position : emptyCorners ) {
                        if(position.getX() == rowNum) {
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
                    for (Position position : emptyCorners ) {
                        if(position.getY() == colNum) {
                            return position;
                        }
                    }
                }
            }
        }
        HashSet<Position> middles = new HashSet<Position>();
        middles.add(new Position(0, mid));
        middles.add(new Position(mid, 0));
        middles.add(new Position(mid, matrix.length - 1));
        middles.add(new Position(matrix.length - 1, mid));

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == '-') {
                    matrix[i][j] = 'X';
                    if (doubleThreat(matrix, 'X')) {
                        matrix[i][j] = '-';
                        if ((super.getDiagonal1() == matrix.length || super.getDiagonal2() == matrix.length)
                            && matrix[mid][mid] == 'O') {
                            for (Position pos: middles) {
                                if (matrix[pos.getX()][pos.getY()] == '-'){
                                    System.out.println("DOUBLE DEFENSE SPECIAL!!");
                                    return pos;
                                }
                            }
                        }
                        else {
                            System.out.println("DOUBLE DEFENSE NORMAL!!");
                            return new Position(i, j);
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
                    Position threat = threat(matrix, 'O');
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
        return super.randomPosition();
    }
}