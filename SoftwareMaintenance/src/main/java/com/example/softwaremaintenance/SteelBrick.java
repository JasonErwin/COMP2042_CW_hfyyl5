package com.example.softwaremaintenance;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Random;

public class SteelBrick extends Brick{
    private static final String NAME = "Steel Brick";
    private static final Color DEF_INNER =  Color.rgb(203, 203, 201);
    private static final Color DEF_BORDER = Color.BLACK;
    private static final int STEEL_STRENGTH = 1;
    private static final double STEEL_PROBABILITY = 0.4;

    private Random rnd;
    private Shape brickFace;

    public SteelBrick(Point2D point, Dimension2D size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,STEEL_STRENGTH);
        rnd = new Random();
        brickFace = super.brickFace;
    } // constructor to instantiate SteelBrick properties when it is called


    @Override
    protected Shape makeBrickFace(Point2D pos, Dimension2D size) {
        return new Rectangle(pos.getX(),pos.getY(),size.getWidth(),size.getHeight());
    } // used abstract method provided by brick

    @Override
    public Shape getBrick() {
        return brickFace;
    } // used abstract method provided by brick

    public  boolean setImpact(Point2D point , int dir){
        if(super.isBroken())
            return false;
        impact();
        return  super.isBroken();
    } //check whether SteelBrick has impact with Ball.

    public void impact(){
        if(rnd.nextDouble() < STEEL_PROBABILITY){
            super.impact();
        }
    }// impact method to check whether to destroy steel brick

    @Override
    public Path getpath() {
        return null;
    }

}