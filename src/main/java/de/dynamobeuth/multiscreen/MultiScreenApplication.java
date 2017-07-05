package de.dynamobeuth.multiscreen;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class MultiScreenApplication extends Application implements CloseHandler {

    protected Stage stage;

    private ScreenManager screenManager;

    private SimpleStringProperty title = new SimpleStringProperty("MultiScreen Application");

    private String startScreen = "start";

    private String skin = "default";

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        initStageTitle();
        initOnCloseRequest();
        initScreenManager();
        initStartScreen();
        initScene();
        configureStage();
        show();
    }

    protected void initStageTitle() {
        stage.titleProperty().bind(titleProperty());
    }

    protected void initOnCloseRequest() {
        stage.setOnCloseRequest(e -> {
            e.consume();

            getScreenManager().setCloseRequestActive(true);

            if (close()) {
                stage.close();
            }

            getScreenManager().setCloseRequestActive(false);
        });
    }

    protected void initScreenManager() {
        screenManager = new ScreenManager(this);
        screenManager.setSkin(getSkin());
        screenManager.initScreens();
        screenManager.initStylesheets();
    }

    protected void initStartScreen() {
        screenManager.showScreen(getStartScreen());
    }

    protected void initScene() {
        Scene scene = new Scene(screenManager);
        stage.setScene(scene);
    }

    protected void configureStage() {
    }

    protected void show() {
        stage.show();
        stage.centerOnScreen();
    }

    protected String getTitle() {
        return title.get();
    }

    protected SimpleStringProperty titleProperty() {
        return title;
    }

    protected void setTitle(String title) {
        this.title.set(title);
    }

    protected String getStartScreen() {
        return startScreen;
    }

    protected void setStartScreen(String startScreen) {
        this.startScreen = startScreen;
    }

    protected String getSkin() {
        return skin;
    }

    protected void setSkin(String skin) {
        this.skin = skin;
    }

    protected ScreenManager getScreenManager() {
        return screenManager;
    }

    protected Stage getStage() {
        return stage;
    }
}
