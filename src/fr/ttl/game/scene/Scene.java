package fr.ttl.game.scene;

public abstract class Scene {
	
	public static final float SCREEN_WIDTH = 16, SCREEN_HEIGHT = 9;
	
	public static float cursorX, cursorY;
	
    public abstract void initScene();
    public abstract void update(double delta);
    public abstract void render(double currentTime);
    public abstract void triggerPlayerKey(int key, int action);
    public abstract void triggerClick();

}
