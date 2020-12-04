package SnakeGame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Class made to manage a snake game
 */
public class SnakeGame {
    public enum Difficulty {Novice, Intemediate, Expert, Impossible};
    private Difficulty difficulty;
    public enum Direction {Up, Right, Down, Left}
    private Direction direction;
    public static Direction actualDirection;

    private int counter = 0;
    private int counterTop;
    private int speed =  3;
    private int delay = 20;
    private int score = 0;
    private boolean paused;

    private Timer timer;
    private LinkedList<Direction> directionQueue;
    private SnakeAnimate gamePanel;

    /**
     * No-Arg constructor
     */
    public SnakeGame() {
        this.difficulty = Difficulty.Novice;
        counterTop = 10;
        this.direction = Direction.Right;
        gamePanel = new SnakeAnimate();
        directionQueue = new LinkedList<>();
        timer = new Timer(delay, new TimerListener());
        timer.setInitialDelay(1000);
        paused = true;
    }

    /**
     * Sets the direction of the snake
     * @param direction
     */
    public void setDirection(Direction direction) {
        if (checkDirection(direction)) {
            if (directionQueue.size() < 4) {
                this.directionQueue.addLast(direction);
                //this.directionQueue.pollFirst();
            }

            //this.direction = direction;
        }
    }

    /**
     * Sets the difficulty of the game
     * @param difficulty
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        switch (difficulty) {
            case Novice:
                speed = 2;
                break;
            case Intemediate:
                speed = 3;
                break;
            case Expert:
                speed = 4;
                break;
            case Impossible:
                speed = 5;
                break;
        }
        /*
        switch (difficulty) {
            case Novice:
                counterTop = 10;
                break;
            case Intemediate:
                counterTop = 8;
                break;
            case Expert:
                counterTop = 6;
                break;
            case Impossible:
                counterTop = 4;
                break;
        }
        */
    }

    /**
     * Returns the score
     * @return int
     */
    public int getScore()
    {   return score;   }

    /**
     * Returns paused state
     * @return boolean
     */
    public boolean getPaused()
    {   return paused;  }

    /**
     * Pauses the game
     */
    public void pause(){
        if (paused){
            timer.start();
            paused = false;
        } else {
            timer.stop();
            paused = true;
        }
    }

    /**
     * Starts a game
     */
    public void run() {
        timer.restart();
        timer.stop();
    }

    /**
     * Checks food and creates new food
     */
    private void food() {
        if (gamePanel.checkFood()) {
            gamePanel.randomFood();
            gamePanel.grow();
            score += 10;
            SnakeDriver.setScoreField(score);
            /*
            if (counter == counterTop){
                System.out.println("Speed up");
                //speedUp();
                counter = 0;
            } else {
                counter++;
            }
            */
        }
    }

    /**
     * Moves the snake in a Direction
     */
    private void move() {
        Direction d;

        if (gamePanel.checkTrack()&&directionQueue.size()!=0){
            d = directionQueue.pollFirst();
            direction = d;

        } else {
            d = direction;
        }
        switch (d) {
            case Up:
                gamePanel.moveUp();
                break;
            case Right:
                gamePanel.moveRight();
                break;
            case Down:
                gamePanel.moveDown();
                break;
            case Left:
                gamePanel.moveLeft();
                break;
        }
    }

    /**
     * Speeds up the snake
     */
    private void speedUp() {
        switch (difficulty) {
            case Novice:
                speed += 1;
                break;
            case Intemediate:
                speed += 1;
                break;
            case Expert:
                speed += 1;
                break;
            case Impossible:
                speed += 1;
                break;
        }
    }

    /**
     * Creates the gamePanel
     * @return JPanel
     */
    public JPanel getGamePanel(){
        return gamePanel;
    }

    /**
     * Resets for a new game
     */
    public void reset() {
        gamePanel.reset();
        difficulty = Difficulty.Novice;
        direction = Direction.Right;
        actualDirection = Direction.Right;
        speed = 3;
        delay = 40;
        score = 0;
        counter = 0;
        counterTop = 10;
        paused = true;
    }

    /**
     * Checks if a direction is valid
     * @param d
     * @return boolean
     */
    private boolean checkDirection(Direction d){
        boolean good = true;
        Direction actual = this.actualDirection;
        if (directionQueue.size() != 0) {
            actual = directionQueue.getLast();
        }
        switch (actual) {
            case Up:
                if (d == Direction.Down)    good = false;
                break;
            case Right:
                if (d == Direction.Left)    good = false;
                break;
            case Down:
                if (d == Direction.Up)      good = false;
                break;
            case Left:
                if (d == Direction.Right)   good = false;
                break;
        }
        return good;
    }

    /**
     * Listener for the Timer
     */
    private class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!gamePanel.isStarted()){
                gamePanel.start();
                gamePanel.randomFood();
            } else {

                for (int i = 0; i < speed && !gamePanel.getLoss(); i++) {
                    move();
                    food();
                }
                if (gamePanel.getLoss()) {
                    timer.stop();
                }
            }
            gamePanel.refreshPanel();
        }
    }
}
