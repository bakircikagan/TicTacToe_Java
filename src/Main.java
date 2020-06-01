import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Main extends JFrame implements ActionListener {
    // CONSTANTS
    private static final int WIDTH = 500;
    private static final int HEIGHT = 200;
    private static final int GAP = 10;

    //PROPERTIES
    private JPanel mainPanel;
    private JButton human;
    private JButton easy;
    private JButton hard;
    private final int size;

    public Main(int size) {
        super("MENU");
        this.size = size;
        mainPanel = new JPanel();
        human = new JButton("1-VS-1");
        easy = new JButton("EASY");
        hard = new JButton("HARD");

        mainPanel.setLayout(new GridLayout(1,3, GAP, GAP));
        mainPanel.add(human);
        mainPanel.add(easy);
        mainPanel.add(hard);

        human.addActionListener(this);
        easy.addActionListener(this);
        hard.addActionListener(this);

        add(mainPanel);
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source instanceof JButton) {
            JButton button = (JButton) source;
            GameFrame.GameType type = button == easy ? GameFrame.GameType.EASY_AI :
                                      button == hard ? GameFrame.GameType.HARD_AI :
                                                       GameFrame.GameType.HUMAN;
            new GameFrame(size, type);
            dispose();
        }
    }

    public static void main(String[] args) {
        new Main(3);
    }

}
