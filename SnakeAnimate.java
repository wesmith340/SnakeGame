package SnakeGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * This class animates a snake game
 */
public class SnakeAnimate extends JPanel {
    private final String lossMessage = "YOU LOSE";
    private final Color snakeColor = new Color(0, 67, 47);
    private final Color eyeColor = new Color(186, 125, 81);
    private final Font font  = new Font("Calibri", Font.PLAIN, 80);


    private ArrayList<Segment> segments;
    private Segment food;
    private int segDiameter = 30;
    private Dimension dim;
    private int width = 900;
    private int height = 600;
    private int perSegment = 8;
    private int movement = segDiameter/perSegment;
    private boolean started = false;
    private boolean loss = false;

    private BufferedImage image;


    /**
     * No-Arg constructor
     */
    public SnakeAnimate() {
        URL resource = getClass().getResource("assets/food.png");
        try {
            image = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dim = new Dimension(width, height);
        this.setMaximumSize(dim);
        this.setMinimumSize(dim);
        segments = new ArrayList<>();
        for (int i = 0; i < 4*perSegment; i++) {
            segments.add(new Segment(420, 420+i*segDiameter/perSegment, snakeColor, segDiameter));
        }
        this.repaint();
    }

    /**
     * This paints the panel
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (started) {
            g.drawImage(image,food.x(),food.y(),food.diameter(),food.diameter(), this);

            Iterator it = segments.iterator();
            while (it.hasNext()) {
                Segment segment = (Segment) it.next();
                g.setColor(segment.color());
                g.drawOval(segment.x(), segment.y(), segment.diameter(), segment.diameter());
                g.fillOval(segment.x(), segment.y(), segment.diameter(), segment.diameter());
            }

            g.setColor(eyeColor);
            Segment head = segments.get(0);
            int eyeSize = 7;
            int sideInset = 2;
            int topInset = 2;
            switch (SnakeGame.actualDirection) {
                case Up:
                    g.drawOval(head.x() + sideInset,
                              head.y() + topInset, eyeSize, eyeSize);
                    g.fillOval(head.x() + sideInset,
                              head.y() + topInset, eyeSize, eyeSize);
                    g.drawOval(head.x() + segDiameter - eyeSize - sideInset,
                              head.y() + topInset, eyeSize, eyeSize);
                    g.fillOval(head.x() + segDiameter - eyeSize - sideInset,
                              head.y() + topInset, eyeSize, eyeSize);
                    break;
                case Right:
                    g.drawOval(head.x() + segDiameter - eyeSize - topInset,
                              head.y() + segDiameter - eyeSize - sideInset, eyeSize, eyeSize);
                    g.fillOval(head.x() + segDiameter - eyeSize - topInset,
                              head.y() + segDiameter - eyeSize - sideInset, eyeSize, eyeSize);
                    g.drawOval(head.x() + segDiameter - eyeSize - topInset,
                              head.y() + sideInset, eyeSize, eyeSize);
                    g.fillOval(head.x() + segDiameter - eyeSize - topInset,
                              head.y() + sideInset, eyeSize, eyeSize);
                    break;
                case Left:
                    g.drawOval(head.x() + topInset,
                              head.y() + segDiameter - eyeSize - sideInset, eyeSize, eyeSize);
                    g.fillOval(head.x() + topInset,
                              head.y() + segDiameter - eyeSize - sideInset, eyeSize, eyeSize);
                    g.drawOval(head.x() + topInset,
                              head.y() + sideInset, eyeSize, eyeSize);
                    g.fillOval(head.x() + topInset,
                              head.y() + sideInset, eyeSize, eyeSize);
                    break;
                case Down:
                    g.drawOval(head.x() + topInset,
                              head.y() + segDiameter - eyeSize - sideInset, eyeSize, eyeSize);
                    g.fillOval(head.x() + topInset,
                              head.y() + segDiameter - eyeSize - sideInset, eyeSize, eyeSize);
                    g.drawOval(head.x() + segDiameter - eyeSize - sideInset,
                              head.y() + segDiameter - eyeSize - sideInset, eyeSize, eyeSize);
                    g.fillOval(head.x() + segDiameter - eyeSize - sideInset,
                              head.y() + segDiameter - eyeSize - sideInset, eyeSize, eyeSize);
            }

            if (loss) {
                g.setFont(font);
                g.setColor(Color.RED);
                g.drawString(lossMessage, ((int) (dim.getWidth() / 2 - 150)),
                        ((int) dim.getHeight() / 2));
            }
        } else {
            g.setColor(Color.WHITE);
            g.drawRect(0,0, dim.width-1, dim.height-1);
        }
        g.setColor(Color.RED);
        g.drawRect(0, 0, dim.width-1, dim.height-1);
    }

    /**
     * Returns the games start state
     * @return boolean
     */
    public boolean isStarted(){
        return started;
    }

    /**
     * Starts the game
     */
    public void start() {
        started = true;
    }

    /**
     * Returns the game's loss state
     * @return boolean
     */
    public boolean getLoss() {
        //return loss;
        return false;
    }

    /**
     * Creates a food in a random place
     */
    public void randomFood() {
        Random r = new Random();

        boolean good;
        do {
            good = true;
            int x = r.nextInt(width/segDiameter)*segDiameter;
            int y = r.nextInt(height/segDiameter)*segDiameter;
            food = new Segment(x, y, segDiameter);

            Iterator it = segments.iterator();

            while (good == true && it.hasNext()) {
                if (((Segment) it.next()).equals(food)) {
                    good = false;
                }
            }

        } while (!good);
    }

    /**
     * Moves the snake up
     */
    public void moveUp() {
        if (checkTop() && checkSegments() && !loss) {
            follow();
            SnakeGame.actualDirection = SnakeGame.Direction.Up;
            segments.get(0).y(segments.get(0).y() - movement);
        } else {
            loss = true;
        }
    }

    /**
     * Moves the snake right
     */
    public void moveRight() {
        if (checkRight() && checkSegments() && !loss) {
            follow();
            SnakeGame.actualDirection = SnakeGame.Direction.Right;
            segments.get(0).x(segments.get(0).x() + movement);

        } else {
            loss = true;
        }

    }

    /**
     * Moves the snake down
     */
    public void moveDown() {
        if (checkBottom() && checkSegments() && !loss) {
            follow();
            SnakeGame.actualDirection = SnakeGame.Direction.Down;
            segments.get(0).y(segments.get(0).y() + movement);

        } else {
            loss = true;
        }

    }

    /**
     * Moves the snake left
     */
    public void moveLeft() {
        if (checkLeft() && checkSegments() && !loss) {
            follow();
            SnakeGame.actualDirection = SnakeGame.Direction.Left;
            segments.get(0).x(segments.get(0).x() - movement);
        } else {
            loss = true;
        }
    }

    /**
     * Moves the snakes tail with it
     */
    private void follow() {
        for (int i = segments.size()-1; i > 0; i--) {
            segments.get(i).x(segments.get(i-1).x());
            segments.get(i).y(segments.get(i-1).y());
        }
    }

    /**
     * Grows the snake
     */
    public void grow() {
        for (int i = 0; i < perSegment*5; i++){
            int size = segments.size()-1;
            int x = segments.get(size).x();
            int y = segments.get(size).y();
            segments.add(new Segment(x, y, segments.get(size).color(), segDiameter));
        }
    }

    /**
     * Checks if snake has run into itself
     * @return boolean
     */
    public boolean checkSegments() {
        boolean dead = false;
        Iterator it = segments.iterator();
        it.next();
        while (dead == false && it.hasNext()) {
            if (((Segment) it.next()).equals(segments.get(0))) {
                dead = true;
            }
        }
        return !dead;
    }

    /**
     * Checks if the snake has run into the top
     * @return boolean
     */
    public boolean checkTop() {
        return segments.get(0).y() != 0;
    }
    /**
     * Checks if the snake has run into the right
     * @return boolean
     */
    public boolean checkRight() {
        return segments.get(0).x() + segDiameter != dim.width;
    }
    /**
     * Checks if the snake has run into the bottom
     * @return boolean
     */
    public boolean checkBottom() {
        return segments.get(0).y() + segDiameter != dim.height;
    }
    /**
     * Checks if the snake has run into the left
     * @return boolean
     */
    public boolean checkLeft() {
        return segments.get(0).x() != 0;
    }
    /**
     * Checks if the snake has eaten the food
     * @return boolean
     */
    public boolean checkFood() {
        return segments.get(0).equals(food);
    }
    /**
     * Checks if the snake is on a line it can turn
     * @return boolean
     */
    public boolean checkTrack() {
        return segments.get(0).x() % segDiameter == 0 && segments.get(0).y() % segDiameter == 0;
    }

    /**
     * Repaints the panel
     */
    public void refreshPanel(){
        this.repaint();
    }

    /**
     * Resets the game
     */
    public void reset(){
        segments.clear();

        for (int i = 0; i < 4*perSegment; i++) {
            segments.add(new Segment(420, 420+i*segDiameter/perSegment, snakeColor, segDiameter));
        }

        loss = false;
        started = false;

        this.repaint();
    }
}
