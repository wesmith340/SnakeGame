package SnakeGame;

import java.awt.*;

/**
 * This class manages information about one segment
 */
public class Segment {
    private int x;
    private int y;
    private Color color;
    private int diameter;

    /**
     * No-Arg constructor
     */
    public Segment() {
        this.x = 0;
        this.y = 0;
        this.color = Color.BLACK;
        this.diameter = 10;
    }

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Segment(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = Color.BLACK;
        this.diameter = 10;
    }
    /**
     * Constructor
     * @param x
     * @param y
     * @param diameter
     */
    public Segment(int x, int y, int diameter) {
        this.x = x;
        this.y = y;
        this.color = Color.BLACK;
        this.diameter = diameter;
    }

    /**
     * Constructor
     * @param x
     * @param y
     * @param color
     * @param diameter
     */
    public Segment(int x, int y, Color color, int diameter) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.diameter = diameter;
    }
/*-------------------------------------------------------------------*/

    /**
     * X-Coordinate
     * @return int
     */
    public int x()
    {   return x;   }
    /**
     * X-Coordinate
     * @param x
     */
    public void x(int x)
    {   this.x = x; }

    /**
     * Y-Coordinate
     * @return int
     */
    public int y()
    {   return y;   }
    /**
     * Y-Coordinate
     * @param y
     */
    public void y(int y)
    {   this.y = y; }

    /**
     * Color of the segment
     * @return Color
     */
    public Color color()
    {   return color;}
    /**
     * Set Color
     * @param color
     */
    public void color(Color color)
    {   this.color = color; }

    /**
     * Get Diameter
     * @return int
     */
    public int diameter()
    {   return diameter;}
    /**
     * Set Diameter
     * @param diameter
     */
    public void diameter(int diameter)
    {   this.diameter = diameter;}
/*------------------------------------------------------------*/

    /**
     * Checks if segments have same x and y coordinates
     * @param seg
     * @return boolean
     */
    public boolean equals(Segment seg){
        return (this.x == seg.x && this.y == seg.y);
    }
}
