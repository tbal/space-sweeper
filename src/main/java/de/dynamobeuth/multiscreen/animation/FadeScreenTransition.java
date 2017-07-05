package de.dynamobeuth.multiscreen.animation;

import de.dynamobeuth.multiscreen.ScreenManager;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeScreenTransition extends AbstractScreenTransition<FadeScreenTransition> {

    public enum FadeMode {
        FADE_PARALLEL,
        FADE_SEQUENTIAL
    }

    protected static final FadeMode DEFAULT_FADE_Mode = FadeMode.FADE_PARALLEL;

    private FadeMode fadeMode;

    public FadeScreenTransition() {
        super();

        setFadeMode(DEFAULT_FADE_Mode);
    }

    public FadeMode getFadeMode() {
        return fadeMode;
    }

    public FadeScreenTransition setFadeMode(FadeMode fadeMode) {
        this.fadeMode = fadeMode;

        return this;
    }

    @Override
    public void animate(ScreenManager screenManager, Node currentScreen, Node nextScreen, EventHandler<javafx.event.ActionEvent> onFinished) {
        nextScreen.setOpacity(0);
        screenManager.getChildren().add(nextScreen);

        FadeTransition fadeInNextScreenTransition = new FadeTransition(getDuration(), nextScreen);
        fadeInNextScreenTransition.setFromValue(0);
        fadeInNextScreenTransition.setToValue(1);

        if (currentScreen == null) {
            fadeInNextScreenTransition.setOnFinished(e -> {
                if (onFinished != null) {
                    onFinished.handle(e);
                }
            });

            fadeInNextScreenTransition.play();

        } else {
            fadeInNextScreenTransition.setOnFinished(e -> {
                screenManager.getChildren().remove(0);

                if (onFinished != null) {
                    onFinished.handle(e);
                }
            });

            FadeTransition fadeOutCurrentScreenTransition = new FadeTransition(getDuration(), currentScreen);
            fadeOutCurrentScreenTransition.setFromValue(1);
            fadeOutCurrentScreenTransition.setToValue(0);

            if (getFadeMode() == FadeMode.FADE_SEQUENTIAL) {
                fadeOutCurrentScreenTransition.setOnFinished(e -> fadeInNextScreenTransition.play());
            } else {
                fadeInNextScreenTransition.play();
            }

            fadeOutCurrentScreenTransition.play();
        }
    }

    @Override
    public Duration getDuration() {
        return getFadeMode() == FadeMode.FADE_SEQUENTIAL ? super.getDuration().divide(2) : super.getDuration();
    }
}
