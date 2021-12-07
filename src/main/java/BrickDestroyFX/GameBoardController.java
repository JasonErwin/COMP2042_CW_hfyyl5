package BrickDestroyFX;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class GameBoardController {

    private GameBoardModel gameBoardModel;
    private GameBoardView gameBoardView;
    private Stage currentStage;
    private Wall wall;
    private Level level;

    public GameBoardController(GameBoardModel gameBoardModel,GameBoardView gameBoardView,Level level,Wall wall){
        this.gameBoardModel=gameBoardModel;
        this.gameBoardView=gameBoardView;
        gameBoardView.paint(gameBoardView.getGc(), gameBoardView.getWall());
        this.wall=wall;
        this.level=level;
        isKeyPressed(gameBoardView.getCanvas());
        isKeyReleased(gameBoardView.getCanvas());
    }

    AnimationTimer animationTimer= new AnimationTimer()
    {
        @Override
        public void handle(long currentNanoTime){
            gameBoardView.paint(gameBoardView.getGc(), gameBoardView.getWall());

            gameBoardView.getWall().move();
            gameBoardView.getWall().findImpacts();
            gameBoardModel.setMessage(String.format("Bricks: %d Balls %d",gameBoardView.getWall().getBrickCount(),gameBoardView.getWall().getBallCount()));
            gameBoardView.getGc().fillText(gameBoardModel.getMessage(),250,225);

            if(gameBoardModel.getInput().equalsIgnoreCase("A")){
                gameBoardView.getWall().player.moveLeft();
            }
            else if (gameBoardModel.getInput().equalsIgnoreCase("D")){
                gameBoardView.getWall().player.movRight();
            }
            else{
                gameBoardView.getWall().player.stop();
            }

            if(gameBoardView.getWall().isBallLost()){
                if(gameBoardView.getWall().ballEnd()){
                    gameBoardView.getWall().wallReset();
                    gameBoardModel.setMessage("Game over");
                }
                gameBoardView.getWall().ballReset();
                animationTimer.stop();
            }
            else if(gameBoardView.getWall().isDone()){
                if(gameBoardModel.getLevel().hasLevel()){
                    gameBoardModel.setMessage("Go to Next Level");
                    animationTimer.stop();
                    gameBoardView.getWall().ballReset();
                    gameBoardView.getWall().wallReset();
                    gameBoardModel.getLevel().nextLevel();
                }
                else{
                    gameBoardModel.setMessage("ALL WALLS DESTROYED");
                    animationTimer.stop();
                }
            }
            gameBoardView.repaint();
        }
    };

    public void isKeyPressed(Canvas canvas){
        canvas.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.A) {
                gameBoardModel.setInput("A");
            }
            else if (e.getCode() ==KeyCode.D){
                gameBoardModel.setInput("D");
            }
            else if (e.getCode() == KeyCode.SPACE) {
                if (gameBoardModel.isRun() == false){
                    gameBoardModel.setRun(true);
                    animationTimer.start();
                } else{
                    if (gameBoardModel.isCheck() == false) {
                        animationTimer.stop();
                        gameBoardModel.setCheck(true);
                    } else {
                        animationTimer.start();
                        gameBoardModel.setCheck(false);
                    }
                }
            }
            else if (e.getCode() == KeyCode.ESCAPE){
                currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
                animationTimer.stop();
                gameBoardModel.setCheck(true);
                currentStage.getScene().getRoot().setEffect(new GaussianBlur());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pausemenu.fxml"));
                Stage newStage = new Stage(StageStyle.TRANSPARENT);
                newStage.initOwner(currentStage);
                newStage.initModality(Modality.APPLICATION_MODAL);
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    newStage.setScene(scene);
                    newStage.show();
                    PauseController pauseController= fxmlLoader.getController();
                    pauseController.initialize(gameBoardView.getWall(),currentStage,gameBoardView.getGc());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if(e.getCode() == KeyCode.F1){
                if(e.isShiftDown() && e.isAltDown())
                    currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
                animationTimer.stop();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("debug.fxml"));
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.initModality(Modality.APPLICATION_MODAL);
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    newStage.setScene(scene);
                    newStage.show();
                    DebugController debugController= fxmlLoader.getController();
                    debugController.initializeDebug(wall,level);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void isKeyReleased(Canvas canvas){
        canvas.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                gameBoardModel.setInput("");
            }
        });
    }
}