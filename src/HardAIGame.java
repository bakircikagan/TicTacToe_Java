import java.util.ArrayList;
import java.util.List;

public final class HardAIGame extends AIGame 
{
    public HardAIGame(int s) 
    {
        super(s);
    }

    @Override
    public int[] position()
    {
    	int[] pos = new int[2];
        char[][] matrix = super.getMatrix();
        int size = super.size();
        int depth = 6;
        int p_inf = Integer.MAX_VALUE;
    	int n_inf = Integer.MIN_VALUE;
        int[] arr = max_value(matrix, size, n_inf, p_inf, depth);
        int score = arr[0];
        pos[0] = arr[1];
        pos[1] = arr[2];
        System.out.println("Score-> " + arr[0]);
        System.out.println("Position-> " + pos[0] + " " + pos[1]);
        return pos;
    }
    
    
    private int[] max_value(char[][] board, int size, int alpha, int beta, int depth)
    {
    	System.out.println("Checking depth " + depth);
    	int p_inf = Integer.MAX_VALUE;
    	int n_inf = Integer.MIN_VALUE;
    	int row = -1;
    	int col = -1;
		char[][] temp = copy(board, size);
    	
    	int[] val = new int[3];
    	if (depth == 0 || super.isOver())//(evaluation_function(board, size) >= 90 || evaluation_function(board, size) <= -90 || depth == 0)
    	{
    		int scr = evaluation_function(board, size);
    		val[0] = scr;
    		val[1] = row;
    		val[2] = col;
    		return val;
    		//return evaluation_function(board, size)
    	}
    	
    	int value = n_inf;
    	
    	List<int[]> action_list = possible_actions(board, size);
    	printActions(action_list);
    	int max_score = 0;
    	
    	
    	int len = action_list.size();
    	for(int i = 0; i < len; i++)
    	{
        	int temp_row = action_list.get(i)[0];
        	int temp_col = action_list.get(i)[1];
        	temp[temp_row][temp_col] = 'O';
        	printMatrix(temp, size);
    		value = Math.max(value, min_value(temp, size, alpha, beta, depth - 1));
    		if(value > max_score)
    		{
    			max_score = value;
    			row = action_list.get(i)[0];
    			col = action_list.get(i)[1];
    		}
    		if(value >= beta)
    		{
    			val[0] = value;
    			val[1] = row;
    			val[2] = col;
    			return val;
    		}
    		alpha = Math.max(alpha, value);
    	}
		val[0] = value;
		val[1] = row;
		val[2] = col;
		return val;
    }
    
    private int min_value(char[][] board, int size, int alpha, int beta, int depth)
    {
    	System.out.println("Checking depth " + depth);
    	int p_inf = Integer.MAX_VALUE;
    	int n_inf = Integer.MIN_VALUE;
    	
    	if (depth == 0 || super.isOver())//(evaluation_function(board, size) >= 90 || evaluation_function(board, size) <= -90 ||depth == 0)
    	{
    		return evaluation_function(board, size);
    	}
    	
    	int value = p_inf;
    	
    	List<int[]> action_list = possible_actions(board, size);
    	int count = 0;
    	printActions(action_list);
    	
    	int len = action_list.size();
    	for(int i = 0; i < len; i++)
    	{
    		char[][] temp = copy(board, size);
        	int temp_row = action_list.get(i)[0];
        	int temp_col = action_list.get(i)[1];
        	temp[temp_row][temp_col] = 'X';
        	printMatrix(temp, size);
    		value = Math.min(value, max_value(temp, size, alpha, beta, depth - 1)[0]);
    		if(value <= alpha)
    		{
    			return value;
    		}
    		beta = Math.min(beta, value);
    		count++;
    	}
    	return value;
    }
    
    private List<int[]> possible_actions(char[][] board, int size)
    {
    	List<int[]> action = new ArrayList<int[]>();
    	
    	for(int i = 0; i < size; i++)
    	{
    		for(int j = 0; j < size; j++)
    		{
    			if(board[i][j] == '-')// && !super.isOver())
    			{
    				action.add(new int[] {i, j});
    			}
    		}
    	}
    	return action;
    }
    
    private int evaluation_function(char[][] board, int size)
    {
    	char[][] trans = transpose(board, size);
    	int total = 0;
    	total += check_horizontal(board, size) + check_horizontal(trans, size);
    	total += check_first_diagonal(board, size);
    	total += check_second_diagonal(board, size);
    	return total;
    }
    
    private int check_horizontal(char[][] board, int size)
    {
    	int total = 0;
    	for (int i = 0; i < size; i++)
    	{
    		int x_count = 0;
    		int o_count = 0;
    		for(int j = 0; j < size; j++)
    		{
    			if(board[i][j] == 'X')
    			{
    				x_count++;
    			}
    			else if(board[i][j] == 'O')
    			{
    				o_count++;
    			}
    		}
    		total += (int)(Math.pow(10, o_count - 1) - Math.pow(10, x_count - 1));
    		//System.out.println("x_count-> " + x_count);
    		//System.out.println("o_count-> " + o_count);
    		//System.out.println("total-> " + total);
    	}
    	//System.out.println("FINAL TOTAL: " + total);
    	return total;
    }
    
    private int check_first_diagonal(char[][] board, int size)
    {
    	int total = 0;
    	for (int i = 0; i < size; i++)
    	{
    		int x_count = 0;
    		int o_count = 0;
    		for(int j = 0; j < size; j++)
    		{
    			if(i == j)
    			{
	    			if(board[i][j] == 'X')
	    			{
	    				x_count++;
	    			}
	    			else if(board[i][j] == 'O')
	    			{
	    				o_count++;
	    			}
    			}
    		}
    		total += (int)(Math.pow(10, o_count - 1) - Math.pow(10, x_count - 1));
    	}
    	return total;
    }
    
    private int check_second_diagonal(char[][] board, int size)
    {
    	int total = 0;
    	for (int i = 0; i < size; i++)
    	{
    		int x_count = 0;
    		int o_count = 0;
    		for(int j = 0; j < size; j++)
    		{
    			if(i + j == (size - 1))
    			{
	    			if(board[i][j] == 'X')
	    			{
	    				x_count++;
	    			}
	    			else if(board[i][j] == 'O')
	    			{
	    				o_count++;
	    			}
    			}
    		}
    		total += (int)(Math.pow(10, o_count - 1) - Math.pow(10, x_count - 1));
    	}
    	return total;
    }
    
    private char[][] transpose(char[][] board, int size)
    {
    	char transpose[][] = new char[size][size];
    	for(int i = 0; i < size; i++)
    	{    
    		for(int j = 0; j < size; j++)
    		{    
    			transpose[i][j] = board[j][i];  
    		}    
    	}
    	return transpose;
    }
    
    private void printActions(List<int[]> list)
    {
    	int len = list.size();
    	for(int i = 0; i < len; i++)
    	{
    		System.out.print("(" + list.get(i)[0] + "," + list.get(i)[1] + ") ");
    	}
    	System.out.println(" ");
    }
    
    private char[][] copy(char[][] board, int size)
    {
    	char[][] copy = new char[size][size];
    	for(int i = 0; i < size; i++)
    	{
    		for(int j = 0; j < size; j++)
    		{
    			copy[i][j] = board[i][j];
    		}
    	}
    	return copy;
    }
    
    private void printMatrix(char[][] matrix, int size)
    {
    	for(int i = 0; i < size; i++)
    	{
    		for(int j = 0; j < size; j++)
    		{
    			System.out.print(matrix[i][j] + " ");
    		}
    		System.out.println();
    	}
    	System.out.println(" ");
    }
}