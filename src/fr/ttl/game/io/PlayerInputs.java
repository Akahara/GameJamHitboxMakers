package fr.ttl.game.io;

import fr.ttl.game.Logger;
import fr.ttl.game.SceneManager;
import fr.ttl.game.display.WindowManager;
import fr.ttl.game.scene.Scene;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerInputs {

	/** Player keys */
    public static final int
            KEY_LEFT = GLFW_KEY_LEFT,
            KEY_RIGHT = GLFW_KEY_RIGHT,
            KEY_DOWN = GLFW_KEY_DOWN,
            KEY_UP = GLFW_KEY_UP,
            KEY_SPACE = GLFW_KEY_SPACE;
    
    public static final int
            ACTION_PRESS = GLFW_PRESS,
            ACTION_RELEASE = GLFW_RELEASE;

    public static void initInputs(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (_window, key, scanCode, action, mods) -> {
            SceneManager.triggerPlayerKey(key, action);
        });
        glfwSetMouseButtonCallback(windowHandle, (_window, btn, action, mods) -> {
        	SceneManager.triggerClick();
        });
        glfwSetCursorPosCallback(windowHandle, (_window, x, y) -> {
        	Scene.cursorX = (float) (x / WindowManager.getWinWidth() * Scene.SCREEN_WIDTH);
        	Scene.cursorY = (float) ((1 - y / WindowManager.getWinHeight()) * Scene.SCREEN_HEIGHT);
        });

        Logger.log("Bound player inputs");
    }

    public static boolean isKeyDown(int key) {
    	return glfwGetKey(WindowManager.window, key) == GLFW_PRESS;
    }
    
    public static void fetchEvents() {
        glfwPollEvents();
    }

}
