package fr.ttl.game;

import fr.ttl.game.audio.Audio;
import fr.ttl.game.display.Renderer;
import fr.ttl.game.display.internal.FrameBuffer;
import fr.ttl.game.display.internal.GLUtils;
import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.math.Utils;
import fr.ttl.game.scene.Scene;

public class SceneManager {

    public static final float TRANSITION_DURATION = 4.8f; // .8s transition

    private static Scene oldScene, currentScene;
    private static long sceneFirstTime;
    private static float screenTransition;

    private static FrameBuffer currentFBO, oldFBO;
    private static FrameBuffer blitFBO;
    
    static {
    	currentFBO = new FrameBuffer();
    	oldFBO = new FrameBuffer();
    	blitFBO = new FrameBuffer();
    	currentFBO.setColorAttachment(0, Texture.fromBuffer(1920, 1080, null));
    	oldFBO.setColorAttachment(0, Texture.fromBuffer(1920, 1080, null));
    	blitFBO.setColorAttachment(0, Texture.fromBuffer(1920, 1080, null));
    }
    
    public static void update(double delta) {
    	currentScene.update(delta);
        if(oldScene != null) {
            oldScene.update(delta);
            screenTransition = Math.min(1, screenTransition+1/TRANSITION_DURATION*(float) delta);
            if(screenTransition >= 1)
                oldScene = null;
        }
    }

    public static void render() {
    	currentFBO.bind();
    	GLUtils.clear();
        currentScene.render(getSceneTime());
        currentFBO.unbind();
        if(oldScene != null) {
        	oldFBO.bind();
        	GLUtils.clear();
        	oldScene.render(getSceneTime());
        	oldFBO.unbind();
        }
        
        blitFBO.bind();
        GLUtils.clear();
        Renderer.drawTransition(screenTransition, currentFBO.getColorAttachment(0), oldFBO.getColorAttachment(0));
        blitFBO.unbind();
        Renderer.drawQuad(0,0,0,0, blitFBO.getColorAttachment(0), null, Renderer.BLIT_SHADER);
    }

    public static void triggerPlayerKey(int key, int action) {
    	currentScene.triggerPlayerKey(key, action);
    }

    public static void triggerClick() {
       currentScene.triggerClick();
    }

    public static void switchScene(Scene scene) {
        if(currentScene == null) {
            currentScene = scene;
            oldScene = null;
            screenTransition = 1;
        } else {
        	oldScene = currentScene;
            currentScene = scene;
            screenTransition = 0;
            Utils.later(() -> Audio.SOURCE_SFX.playRandom(Audio.SFX_FIGHT_TRANSITIONS), .5f);
        }
        sceneFirstTime = System.nanoTime();
        scene.initScene();
        Logger.log("Switched scene to " + scene.getClass().getSimpleName());
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static float getSceneTime() {
        return (float) ((System.nanoTime() - sceneFirstTime) / 1E9);
    }

}
