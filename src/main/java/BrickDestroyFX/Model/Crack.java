package BrickDestroyFX.Model;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

import java.util.Random;

/**
 * Crack  for Cement
 */
public class Crack {
    private static final int CRACK_SECTIONS = 3;
    private static final double JUMP_PROBABILITY = 0.7;

    public static final int LEFT = 10;
    public static final int RIGHT = 20;
    public static final int UP = 30;
    public static final int DOWN = 40;
    public static final int VERTICAL = 100;
    public static final int HORIZONTAL = 200;
    private static Random rnd;
    Shape brickFace;
    private Path crack;

    private int crackDepth;
    private int steps;

    /**
     * Define Crack Property
     * @param crackDepth should te brick crack
     * @param steps number of steps
     */
    public Crack(int crackDepth, int steps) {
        crack = new Path();
        this.crackDepth = crackDepth;
        this.steps = steps;
        rnd = new Random();

    } //constructor to instantiate crack properties when it is called, used by cement

    /**
     * Draw the crack
     * @return crack
     */
    public Path draw() {

        return crack;
    } // method to return crack, used in cement

    /**
     * reset the crack
     */
    public void reset() {
        crack.getElements().clear();
    } //method to remove te crack icon from bricks, Used in cement brick

    /**
     * Make it crack at a specific point based on direction
     * @param point x and y coordinates
     * @param direction which direction
     * @param brickFace get the brickface of the brick.
     */
    public void makeCrack(Point2D point, int direction, Shape brickFace) {
        Bounds bounds = brickFace.getBoundsInParent();

        Point2D impact = new Point2D((int) point.getX(), (int) point.getY());
        Point2D start;
        Point2D end;


        switch (direction) {
            case LEFT:
                start = new Point2D(bounds.getMaxX(), bounds.getMinY());
                end = new Point2D(bounds.getMaxX(), bounds.getMaxY());
                Point2D tmp = makeRandomPoint(start, end, VERTICAL);
                makeCrack(impact, tmp);

                break;
            case RIGHT:
                start = new Point2D(bounds.getMinX(), bounds.getMinY());
                end = new Point2D(bounds.getMinX(), bounds.getMaxY());
                tmp = makeRandomPoint(start, end, VERTICAL);
                makeCrack(impact, tmp);

                break;
            case UP:
                start = new Point2D(bounds.getMinX(), bounds.getMaxY());
                end = new Point2D(bounds.getMaxX(), bounds.getMinY());
                tmp = makeRandomPoint(start, end, HORIZONTAL);
                makeCrack(impact, tmp);
                break;
            case DOWN:
                start = new Point2D(bounds.getMinX(), bounds.getMinY());
                end = new Point2D(bounds.getMaxX(), bounds.getMinY());
                tmp = makeRandomPoint(start, end, HORIZONTAL);
                makeCrack(impact, tmp);

                break;

        }
    } // method to crack the brick

    /**
     * Make brick crack
     * @param start x and y coordinate of the start
     * @param end x and y coordinate of the end
     */
    protected void makeCrack(Point2D start, Point2D end) {

        Path path = new Path();


        //path move to(start.x,start.y);
        MoveTo moveTo = new MoveTo();
        moveTo.setX(start.getX());
        moveTo.setY(start.getY());

        path.getElements().add(moveTo);

        double w = (end.getX() - start.getX()) / (double) steps;
        double h = (end.getY() - start.getY()) / (double) steps;

        int bound = crackDepth;
        int jump = bound * 5;

        double x, y;

        for (int i = 1; i < steps; i++) {

            x = (i * w) + start.getX();
            y = (i * h) + start.getY() + randomInBounds(bound);

            if (inMiddle(i, CRACK_SECTIONS, steps))
                y += jumps(jump, JUMP_PROBABILITY);

            //LineTo lineTo = (x,y);
            LineTo lineTo = new LineTo();
            lineTo.setX(x);
            lineTo.setY(y);

            path.getElements().add(lineTo);
        }

        //path.lineTo(end.getX(),end.getY());
        LineTo lineto = new LineTo();
        lineto.setX(end.getX());
        lineto.setY(end.getY());
        path.getElements().add(lineto);

        crack = path;
    }

    private int randomInBounds(int bound) {
        int n = (bound * 2) + 1;
        return rnd.nextInt(n) - bound;
    }

    private boolean inMiddle(int i, int steps, int divisions) {
        int low = (steps / divisions);
        int up = low * (divisions - 1);

        return (i > low) && (i < up);
    }

    private int jumps(int bound, double probability) {

        if (rnd.nextDouble() > probability)
            return randomInBounds(bound);
        return 0;

    }

    private Point2D makeRandomPoint(Point2D from, Point2D to, int direction) {

        Point2D out = new Point2D(0, 0);
        int pos;

        switch (direction) {
            case HORIZONTAL:
                pos = rnd.nextInt((int) to.getX() - (int) from.getX()) + (int) from.getX();
                out = new Point2D(pos, to.getY());
                break;
            case VERTICAL:
                pos = rnd.nextInt((int) to.getY() - (int) from.getY()) + (int) from.getY();
                out = new Point2D(to.getX(), pos);
                break;
        }
        return out;
    }
}

