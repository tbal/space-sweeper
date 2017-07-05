package de.dynamobeuth.multiscreen.animation;

import de.dynamobeuth.multiscreen.ScreenManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class SlideScreenTransition extends AbstractScreenTransition<SlideScreenTransition> {

    public enum SlideDirection {
        SLIDE_LEFT,
        SLIDE_RIGHT,
        SLIDE_UP,
        SLIDE_DOWN
    }

    protected static final SlideDirection DEFAULT_SLIDE_DIRECTION = SlideDirection.SLIDE_LEFT;

    private SlideDirection slideDirection;

    public SlideScreenTransition() {
        super();

        setSlideDirection(DEFAULT_SLIDE_DIRECTION);
    }

    public SlideDirection getSlideDirection() {
        return slideDirection;
    }

    public SlideScreenTransition setSlideDirection(SlideDirection slideDirection) {
        this.slideDirection = slideDirection;

        return this;
    }

    @Override
    public void animate(ScreenManager screenManager, Node currentScreen, Node nextScreen, EventHandler<ActionEvent> onFinished) {
        if (currentScreen == null) {
            screenManager.getChildren().add(nextScreen);

            if (onFinished != null) {
                onFinished.handle(new ActionEvent(this, nextScreen));
            }

            return;
        }

        double screenWidth = currentScreen.getBoundsInParent().getWidth();
        double screenHeight = currentScreen.getBoundsInParent().getHeight();

        TranslateTransition moveOutCurrentScreenTransition = new TranslateTransition(getDuration(), currentScreen);

        TranslateTransition moveInNewScreenTransition = new TranslateTransition(getDuration(), nextScreen);
        moveInNewScreenTransition.setOnFinished(e -> {
            screenManager.getChildren().remove(0);

            if (onFinished != null) {
                onFinished.handle(e);
            }
        });

        switch (getSlideDirection()) {
            case SLIDE_LEFT:
                nextScreen.setTranslateX(screenWidth);

                moveOutCurrentScreenTransition.setFromX(0);
                moveOutCurrentScreenTransition.setToX(-screenWidth);

                moveInNewScreenTransition.setToX(0);

                break;

            case SLIDE_RIGHT:
                nextScreen.setTranslateX(-screenWidth);

                moveOutCurrentScreenTransition.setFromX(0);
                moveOutCurrentScreenTransition.setToX(screenWidth);

                moveInNewScreenTransition.setToX(0);

                break;

            case SLIDE_UP:
                nextScreen.setTranslateY(screenHeight);

                moveOutCurrentScreenTransition.setFromY(0);
                moveOutCurrentScreenTransition.setToY(-screenHeight);

                moveInNewScreenTransition.setToY(0);

                break;

            case SLIDE_DOWN:
                nextScreen.setTranslateY(-screenHeight);

                moveOutCurrentScreenTransition.setFromY(0);
                moveOutCurrentScreenTransition.setToY(screenHeight);

                moveInNewScreenTransition.setToY(0);

                break;

            default:
                throw new IllegalArgumentException("No implementation for SlideDirection '" + getSlideDirection().toString() + "'.");
        }

        screenManager.getChildren().add(nextScreen);

        moveInNewScreenTransition.play();
        moveOutCurrentScreenTransition.play();
    }
}
