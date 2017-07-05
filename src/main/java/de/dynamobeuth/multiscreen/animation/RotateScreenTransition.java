package de.dynamobeuth.multiscreen.animation;

import de.dynamobeuth.multiscreen.ScreenManager;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class RotateScreenTransition extends AbstractScreenTransition<RotateScreenTransition> {

    public enum RotationMode {
        ROTATE_IN,
        ROTATE_OUT
    }

    protected static final int DEFAULT_ANGLE = 360 * 2;
    protected static final RotationMode DEFAULT_ROTATION_MODE = RotationMode.ROTATE_OUT;

    private int angle;

    private RotationMode rotationMode;

    public RotateScreenTransition() {
        super();

        setAngle(DEFAULT_ANGLE);
        setRotationMode(DEFAULT_ROTATION_MODE);
    }

    public int getAngle() {
        return angle;
    }

    public RotateScreenTransition setAngle(int angle) {
        this.angle = angle;

        return this;
    }

    public RotationMode getRotationMode() {
        return rotationMode;
    }

    public RotateScreenTransition setRotationMode(RotationMode rotationMode) {
        this.rotationMode = rotationMode;

        return this;
    }

    @Override
    public void animate(ScreenManager screenManager, Node currentScreen, Node nextScreen, EventHandler<ActionEvent> onFinished) {

        if (getRotationMode() == RotationMode.ROTATE_OUT && currentScreen == null) {
            screenManager.getChildren().add(0, nextScreen);

            if (onFinished != null) {
                onFinished.handle(new ActionEvent(this, nextScreen));
            }

            return;
        }

        RotateTransition rotateTransition = new RotateTransition(getDuration());
        rotateTransition.setByAngle(getAngle());

        ScaleTransition scaleTransition = new ScaleTransition(getDuration());

        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, scaleTransition);

        switch (getRotationMode()) {
            case ROTATE_IN:
                nextScreen.setScaleX(0);
                nextScreen.setScaleY(0);

                screenManager.getChildren().add(nextScreen);

                parallelTransition.setNode(nextScreen);
                parallelTransition.setOnFinished(e -> {
                    screenManager.getChildren().remove(0);

                    if (onFinished != null) {
                        onFinished.handle(e);
                    }
                });

                scaleTransition.setToX(1);
                scaleTransition.setToY(1);

                break;

            case ROTATE_OUT:
                screenManager.getChildren().add(0, nextScreen);

                parallelTransition.setNode(currentScreen);
                parallelTransition.setOnFinished(e -> {
                    screenManager.getChildren().remove(1);

                    if (onFinished != null) {
                        onFinished.handle(e);
                    }
                });

                scaleTransition.setToX(0);
                scaleTransition.setToY(0);

                break;

            default:
                throw new IllegalArgumentException("No implementation for RotationMode '" + getRotationMode().toString() + "'.");
        }

        parallelTransition.play();
    }
}
