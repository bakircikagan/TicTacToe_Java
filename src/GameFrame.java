import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public final class GameFrame extends JFrame implements Observer,ActionListener {
    // ENUM
    public enum GameType {HUMAN, EASY_AI, HARD_AI}

    // CONSTANTS
    private static final Font FONT_1 = new Font("Roman", Font.BOLD, 60);
    private static final Font FONT_2 = new Font("Roman", Font.BOLD, 40);
    private static final int WIDTH = 380;
    private static final int HEIGHT = 480;
    private static final int DELAY = 1500;

    // PROPERTIES
    private JPanel mainPanel;
    private JLabel label;
    private JPanel northPanel;
    private JPanel centerPanel;
    private Pot[][] pots;
    private Game game;
    private GameType type;
    private Timer timer;
    private final int size;

    // Static Variables
    private static JRadioButton buttonX = new JRadioButton("X");
    private static JRadioButton buttonO = new JRadioButton("O");
    private static int scoreX = 0;
    private static int scoreO = 0;

    // CONSTRUCTORS
    public GameFrame(GameType type) {
        this(3, type);
    }

    public GameFrame(int size, GameType type) {
        super("Tic Tac Toe!");
        this.size = size;
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
                pots[i][j].setBackground(Color.CYAN);
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

        timer = new Timer(DELAY, this);

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

    // METHODS
    @Override
    public void update(Subject s) {
        boolean wonX = game.won('X');
        boolean wonO = game.won('O');
        boolean tie = game.tie();
        if (wonX || wonO || tie) {
            if (game.won('X')) {
                ++scoreX;
            }
            else if (game.won('O')) {
                ++scoreO;
            }
            if (!tie || (wonX || wonO)) {
                String str = wonX ? "X" : "O";
                for (int i = 0; i < pots.length; ++i) {
                    for (int j = 0; j < pots.length; ++j) {
                        pots[i][j].setEnabled(false);
                        if (pots[i][j].getText().equals(str)) {
                            pots[i][j].setBackground(Color.GREEN);
                        }
                    }
                }
            }
            timer.start();
        }
        else if (game.isAIGame() && !Game.XPlays) {
            Position pos = ((AIGame) game).position();
            mark(pots[pos.getX()][pos.getY()]);
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
        if (source == timer) {
            dispose();
            new GameFrame(size, type);
            timer.stop();
        }
        else if(source instanceof Pot) {
            mark((Pot)source);
        }
    }
}