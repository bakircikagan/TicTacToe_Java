import java.util.Random;

public class EasyAIGame extends AIGame {

    public EasyAIGame(int s) {
        super(s);
    }

    @Override
    public int[] position() {
        Random r = new Random();
        char[][] matrix = super.getMatrix();
        int size = matrix.length;
        int x = 0;
        int y = 0;
        do {
            x = r.nextInt(size);
            y = r.nextInt(size);
        }while(matrix[x][y] != '-');
        return new int[]{x, y};
    }
}
