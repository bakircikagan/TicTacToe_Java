import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class GameFrame extends JFrame implements Observer,ActionListener {
    //ENUM
    public enum GameType {HUMAN, EASY_AI, HARD_AI}

    //CONSTANTS
    private static final Font FONT_1 = new Font("Roman", Font.BOLD, 60);
    private static final Font FONT_2 = new Font("Roman", Font.BOLD, 40);
    private static final int WIDTH = 380;
    private static final int HEIGHT = 480;

    //PROPERTIES
    private JPanel mainPanel;
    private JLabel label;
    private JPanel northPanel;
    private JPanel centerPanel;
    private Pot[][] pots;
    private Game game;

    private static JRadioButton buttonX = new JRadioButton("X");
    private static JRadioButton buttonO = new JRadioButton("O");
    private static int scoreX = 0;
    private static int scoreO = 0;
    private GameType type;

    //CONSTRUCTORS
    public GameFrame(GameType type) {
        this(3, type);
    }

    public GameFrame(int size, GameType type) {
        super("Tic Tac Toe!");
        pots = new Pot[size][size];
        mainPanel = new JPanel();
        northPanel = new JPanel();
        centerPanel = new JPanel();
        label = new JLabel("X: " + scoreX +"   O: " + scoreO);

        this.type = type;
        game = type == GameType.HUMAN ? new Game(size) :
                (type == GameType.EASY_AI ?  new EasyAIGame(size)
                        : new HardAIGame(size));

        northPanel.add(label);
        northPanel.add(buttonX);
        northPanel.add(buttonO);

        buttonX.setEnabled(false);
        buttonO.setEnabled(false);
        buttonX.setSelected(Game.XPlays);
        buttonO.setSelected(!Game.XPlays);

        centerPanel.setLayout(new GridLayout(size, size));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel,BorderLayout.CENTER);

        //Creating the pots
        for (int i = 0; i < pots.length; ++i) {
            for (int j = 0; j < pots[0].length; ++j) {
                pots[i][j] = new Pot("", i, j);
                pots[i][j].setFont(FONT_1);
                pots[i][j].setBackground(Color.BLUE);
                pots[i][j].setForeground(Color.WHITE);
                pots[i][j].addActionListener(this);
                pots[i][j].setPreferredSize(new Dimension(120,120));
                JPanel p = new JPanel();

                p.add(pots[i][j]);
                centerPanel.add(p);
            }
        }

        label.setFont(FONT_2);
        northPanel.setBackground(Color.YELLOW);


        add(mainPanel);
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        //Adding the observers
        game.addObserver(this);

        if (!Game.XPlays) {
           game.notifyObservers();
        }

    }

    //METHODS
    @Override
    public void update(Subject s) {
        if (game.isOver()) {
            if (game.won('X')) {
                ++scoreX;
            }
            else if (game.won('O')) {
                ++scoreO;
            }
            dispose();
            new GameFrame(type);
        }
        else if (game.isAIGame() && !Game.XPlays) {
            int[] pos = ((AIGame) game).position();
            int x = pos[0];
            int y = pos[1];
            mark(pots[x][y]);
        }
    }

    private void mark(Pot p) {
        int x = p.getX();
        int y = p.getY();
        p.setEnabled(false);
        p.setText(game.next() + "");
        game.play(x, y);
        buttonX.setSelected(Game.XPlays);
        buttonO.setSelected(!Game.XPlays);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if(source instanceof Pot) {
            mark((Pot)source);
        }
    }

}