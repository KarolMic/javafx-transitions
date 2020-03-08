import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Application Transitions class.
 *
 * @author Karol Mickiewicz
 */
public class Transitions extends Application {

    /**
     * Public fields width and height of the stage.
     */
    double width = 600;
    double height = 600;
    /**
     * Public fields used to move polygon to specified X and Y coordinates.
     */
    int moveX = 240;
    int moveY = 225;

    /**
     * Application main method.
     *
     * @param args application command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Application start method.
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) throws Exception {

        Group group = new Group();

        /**
         * Creating octagon with specified coordinates.
         */
        Polygon octagon = new Polygon(25, 5, 60, 5, 80, 30, 80, 80, 60, 100, 25, 100, 5, 80, 5, 30);

        /**
         * Setting colour of the octagon.
         */
        octagon.setFill(Color.GREENYELLOW);

        /**
         * Change size of the octagon.
         */
        octagon.setScaleX(1.6);
        octagon.setScaleY(1.6);
        /**
         * Move octagon to the middle of the stage.
         */
        octagon.setLayoutX(190);
        octagon.setLayoutY(247);
        group.getChildren().add(octagon);

        /**
         * Creating list of transitions.
         */
        List<Transition> transitions = new ArrayList<>();
        transitions.add(createTranslateTransition(octagon, 0, 0, 0, -moveY));
        transitions.add(createParallelMoveRotate(octagon));
        transitions.add(createParallelMoveFade(octagon));
        transitions.add(createParallelMoveReverseFade(octagon));
        transitions.add(createParallelMoveFill(octagon));

        Pane p = new Pane();
        p.getChildren().add(octagon);

        BorderPane rootPane = new BorderPane();
        rootPane.setCenter(p);

        HBox toolBox = new HBox();

        /**
         * Creating start/stop button with on action event.
         */
        ToggleButton button = new ToggleButton("start/stop");
        button.setUserData(createSequentialTransition(transitions));
        button.setOnAction(event -> {
            ToggleButton tb = (ToggleButton) event.getSource();
            Transition t = (Transition) tb.getUserData();
            if (tb.isSelected()) {
                t.play();
            } else {
                t.pause();
            }
        });
        toolBox.getChildren().add(button);
        button.setMaxWidth(Double.POSITIVE_INFINITY);

        rootPane.setLeft(toolBox);

        /**
         * Creating the stage.
         */
        Scene scene = new Scene(rootPane, width, height);
        rootPane.setStyle("-fx-background-color: #000000;");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method which moves octagon to specified coordinates.
     */
    private Transition createTranslateTransition(Polygon octagon, int x, int y, int z, int k) {
        TranslateTransition rightDown = new TranslateTransition(Duration.millis(2000), octagon);
        rightDown.setFromX(x);
        rightDown.setFromY(y);
        rightDown.setToX(z);
        rightDown.setToY(k);
        return rightDown;
    }

    /**
     * Method which allows to rotate octagon.
     */
    private Transition createRotateTransition(Polygon octagon) {
        RotateTransition rotate = new RotateTransition(Duration.millis(2000), octagon);
        rotate.setFromAngle(0);
        rotate.setToAngle(720);
        return rotate;
    }

    /**
     * Method which allows to parallel rotate and move octagon to specified coordinates.
     */
    private Transition createParallelMoveRotate(Polygon octagon) {
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().add(createTranslateTransition(octagon, 0, -moveY, moveX, 0));
        pt.getChildren().add(createRotateTransition(octagon));
        return pt;
    }

    /**
     * Method which allows octagon to fade.
     */
    private Transition createFadeTransition(Polygon octagon) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), octagon);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setAutoReverse(true);
        return ft;
    }

    /**
     * Method which allows to parallel moving and fading of the octagon.
     */
    private Transition createParallelMoveFade(Polygon star) {
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().add(createTranslateTransition(star, moveX, 0, 0, moveY));
        pt.getChildren().add(createFadeTransition(star));
        return pt;
    }

    /**
     * Method which allows to reverse fade.
     */
    private Transition createReverseFadeTransition(Polygon star) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), star);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.setAutoReverse(true);
        return ft;
    }

    /**
     * Method which allows octagon to parallel move and reverse fade.
     */
    private Transition createParallelMoveReverseFade(Polygon star) {
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().add(createTranslateTransition(star, 0, moveY, -moveX, 0));
        pt.getChildren().add(createReverseFadeTransition(star));
        return pt;
    }

    /**
     * Method which allows to change colour to purple.
     */
    private Transition createFillTransition(Polygon star) {
        FillTransition ft = new FillTransition();
        ft.setFromValue(Color.GREENYELLOW);
        ft.setToValue(Color.PURPLE);
        ft.setShape(star);
        ft.setCycleCount(2);
        ft.cycleDurationProperty();
        ft.setAutoReverse(true);
        ft.setDuration(Duration.millis(1000));
        return ft;
    }

    /**
     * Method which allows to parallel moving and changing colour of the octagon.
     */
    private Transition createParallelMoveFill(Polygon star) {
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().add(createTranslateTransition(star, -moveX, 0, 0, 0));
        pt.getChildren().add(createFillTransition(star));
        return pt;
    }

    /**
     * Method which allows to perform all stages sequentially.
     */
    private Transition createSequentialTransition(List<Transition> transitions) {
        SequentialTransition st = new SequentialTransition();
        st.getChildren().addAll(transitions.get(0),
                transitions.get(1),
                transitions.get(2),
                transitions.get(3),
                transitions.get(4));
        for (Animation a : st.getChildren()) {
            a.setCycleCount(1);
        }
        st.setCycleCount(Timeline.INDEFINITE);
        return st;
    }

}
