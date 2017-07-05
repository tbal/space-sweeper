package de.dynamobeuth.multiscreen;

abstract public class ScreenController {

    private ScreenManager screenManager;

//    private Application application;
//
//    public void setApplication(Application application) {
//        this.application = application;
//    }
//
//    public Application getApplication() {
//        return application;
//    }

    public void setScreenManager(ScreenManager manager) {
        this.screenManager = manager;
    }

    protected ScreenManager getScreenManager() {
        return screenManager;
    }

    /**
     * Will be called once after initialize() and after application and screenManager were injected
     */
    protected void prepare() {
    }

    /**
     * Will be called every time, the screen is to be shown
     * and just before the screen is added to its root and before possible screen transition animations
     */
    protected void beforeShow() {
    }

    /**
     * Will be called every time, the screen is shown
     * and when possible screen transition animations are just finished
     */
    protected void show() {
    }

    // TODO: integration should be possible; wait for use-case before implementation
//    public void beforeHide() {
//        System.out.println("show game view");
//    }
//
//    public void hide() {
//        System.out.println("hide game view");
//    }
}
