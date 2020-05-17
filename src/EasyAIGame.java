import java.util.Random;

public final class EasyAIGame extends AIGame {

    public EasyAIGame(int s) {
        super(s);
    }

    @Override
    public int[] position() {
        int[] pos = new int[2];
        Random r = new Random();
        char[][] matrix = super.getMatrix();
        int size = matrix.length;
        int x = 0;
        int y = 0;
        do {
            x = r.nextInt(size);
            y = r.nextInt(size);
        }while(matrix[x][y] != '-');
        pos[0] = x;
        pos[1] = y;
        return pos;
    }
}
