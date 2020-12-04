package SnakeGame;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import SnakeGame.SnakeGame.Difficulty;
import SnakeGame.SnakeGame.Direction;

/**
 * Gui driver for the snake game
 */
public class SnakeDriver {

    private Dimension dim    = Toolkit.getDefaultToolkit().getScreenSize();
    private Dimension gameSize = new Dimension(950, 750);
    private Dimension startSize = new Dimension(350, 400);
    private final Font font2 = new Font("Calibri", Font.PLAIN, 25);

    private JFrame gameFrame, startFrame;
    private JRadioButton[] diffLevel;
    private JButton start, newGame, runStop;

    private Difficulty difficulty;
    private Direction direction;

    private static JTextField scoreField;

    private Thread t;
    private SnakeGame snakeGame;

    /**
     * No-Arg constructor
     */
    public SnakeDriver() {
        startFrame = makeStartFrame();
        startFrame.setTitle("Snake Game");
        startFrame.setVisible(true);
        startFrame.setSize(startSize);
        startFrame.setLocation((dim.width-startSize.width)/2, (dim.height-startSize.height)/2);
        startFrame.setResizable(false);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        gameFrame = makeGameFrame();
        gameFrame.setTitle("Snake Game");
        gameFrame.setSize(gameSize);
        gameFrame.setLocation((dim.width-gameSize.width)/2, (dim.height-gameSize.height)/2);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.addKeyListener(new KeyHandler());
        gameFrame.setVisible(false);

    }

    /**
     * Make startFrame
     * @return JFrame
     */
    public JFrame makeStartFrame() {
        JFrame frame = new JFrame();
        Container app = frame.getContentPane();

        JPanel temp = new JPanel();
        JPanel radioHolder = new JPanel();
        //BoxLayout box = new BoxLayout(radioHolder, BoxLayout.PAGE_AXIS);
        radioHolder.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioHolder.setLayout(new GridLayout(4,1));
        radioHolder.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(20,20,20,20)));

        ButtonGroup group = new ButtonGroup();
        diffLevel = new JRadioButton[4];
        diffLevel[0] = new JRadioButton("Novice");
        diffLevel[1] = new JRadioButton("Intermediate");
        diffLevel[2] = new JRadioButton("Expert");
        diffLevel[3] = new JRadioButton("Impossible");

        for (int i = 0; i < 4; i++) {
            JPanel holder = new JPanel();
            diffLevel[i].setFont(font2);
            diffLevel[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            group.add(diffLevel[i]);
            radioHolder.add(diffLevel[i]);
        }
        diffLevel[0].setSelected(true);
        temp.add(radioHolder);
        app.add(temp);

        JPanel buttonHolder = new JPanel();

        start = new JButton("Start Game");
        start.setFont(font2);
        start.addActionListener(new ButtonHandler());

        buttonHolder.add(start);
        buttonHolder.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        app.add(buttonHolder, BorderLayout.SOUTH);

        return frame;

    }

    /**
     * Make gameFrame
     * @return JFrame
     */
    public JFrame makeGameFrame() {
        snakeGame = new SnakeGame();

        JFrame frame = new JFrame();
        Container app = frame.getContentPane();

        JPanel topPanel = new JPanel();
        JLabel scoreLable = new JLabel("Score: ");
        scoreLable.setFont(font2);


        topPanel.add(scoreLable);

        scoreField = new JTextField(10);
        scoreField.setEditable(false);
        scoreField.setFocusable(false);
        scoreField.setFont(font2);

        topPanel.add(scoreField);
        app.add(topPanel, BorderLayout.NORTH);

        JPanel gamePanel = snakeGame.getGamePanel();
        gamePanel.setBackground(Color.LIGHT_GRAY);

        JPanel holder = new JPanel();
        holder.setLayout(new BoxLayout(holder, BoxLayout.PAGE_AXIS));
        holder.add(gamePanel);
        app.add(holder);

        JPanel bottomPanel = new JPanel();

        runStop = new JButton("Start");
        runStop.setFont(font2);
        runStop.setFocusable(false);
        runStop.addActionListener(new ButtonHandler());
        bottomPanel.add(runStop);

        newGame = new JButton("New Game");
        newGame.setFont(font2);
        newGame.setFocusable(false);
        newGame.addActionListener(new ButtonHandler());
        bottomPanel.add(newGame);

        app.add(bottomPanel, BorderLayout.SOUTH);

        return frame;
    }

    /**
     * Updates scorefield
     * @param score
     */
    public static void setScoreField(int score){
        scoreField.setText(String.valueOf(score));
    }

    /**
     * Make gameFrame visible
     */
    public void showGameFrame(){
        startFrame.setVisible(false);
        gameFrame.setVisible(true);

        snakeGame.run();
    }

    /**
     * Make startFrame visible
     */
    public void showStartFrame(){
        startFrame.setVisible(true);
        gameFrame.setVisible(false);
    }

    /**
     * ActionListener for buttons
     */
    public class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == start){
                snakeGame.reset();
                showGameFrame();
                if (diffLevel[0].isSelected()){
                    snakeGame.setDifficulty(Difficulty.Novice);
                } else if (diffLevel[1].isSelected()) {
                    snakeGame.setDifficulty(Difficulty.Intemediate);
                } else if (diffLevel[2].isSelected()) {
                    snakeGame.setDifficulty(Difficulty.Expert);
                } else if (diffLevel[3].isSelected()) {
                    snakeGame.setDifficulty(Difficulty.Impossible);
                }
            } else if (event.getSource() == newGame) {
                showStartFrame();
                runStop.setText("Start");
            } else if (event.getSource() == runStop) {
                if (snakeGame.getPaused()) {
                    runStop.setText("Pause Game");
                } else {
                    runStop.setText("Unpause");
                }
                snakeGame.pause();
            }
        }
    }

    /**
     * Listener for keystrokes
     */
    public class KeyHandler implements KeyListener {
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_UP){
                snakeGame.setDirection(SnakeGame.Direction.Up);

            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                snakeGame.setDirection(SnakeGame.Direction.Right);

            } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                snakeGame.setDirection(SnakeGame.Direction.Down);

            } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                snakeGame.setDirection(SnakeGame.Direction.Left);

            } else if (keyEvent.getKeyCode() == KeyEvent.VK_P) {
                snakeGame.pause();
            }
        }
        public void keyTyped(KeyEvent keyEvent) {}

        public void keyReleased(KeyEvent keyEvent) {}
    }

    /**
     * Main
     * @param args
     */
    public static void main(String[] args) {
        SnakeDriver driver = new SnakeDriver();

    }
}
