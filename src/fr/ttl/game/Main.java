package fr.ttl.game;

import java.io.IOException;
import java.io.InputStream;

import fr.ttl.game.audio.Audio;
import fr.ttl.game.audio.AudioManager;
import fr.ttl.game.display.WindowManager;
import fr.ttl.game.io.PlayerInputs;
import fr.ttl.game.math.Utils;
import fr.ttl.game.scene.MainMenuScene;

public class Main {

	private static long firstMillis = System.currentTimeMillis();
	
	private static final int TARGET_FPS = 120;
	
	public static byte[] bigBuf = new byte[10000];

	public static void main(String[] args) {
		WindowManager.createWindow("Hell's alley");
        AudioManager.createSoundSystem();
        
		WindowManager.show(false);
        Audio.loadResources();
        
        Audio.SOURCE_MUSIC.play();

//        Player.worldId = 0;
//        Player.visitedWorldCounts[0]++;
//        Player.gauges[0] = 3;
        SceneManager.switchScene(new MainMenuScene());
//        SceneManager.switchScene(new WorldScene());
//		SceneManager.switchScene(new CombatScene());
//        SceneManager.switchScene(new EndScreen(1));
//        SceneManager.switchScene(new DeathScene());
//		SceneManager.switchScene(new DilemaScene(0));
		
		Audio.SOURCE_LOW_RESOURCES.play();

		long nextFrame = System.nanoTime();
		long previousFrame = nextFrame;
		int fps = 0;
		long nextFPSFrame = System.nanoTime();

		while (!WindowManager.shouldDispose()) {
			WindowManager.beginFrame();
			SceneManager.render();
			WindowManager.endFrame();
			fps++;

			PlayerInputs.fetchEvents();
			Utils.runTasks();
			Audio.SOURCE_MUSIC.update();

			long current = System.nanoTime();
			SceneManager.update((current - previousFrame) / 1E9);
			previousFrame = current;
			current = System.nanoTime();
			if (current < nextFrame) {
				try {
					Thread.sleep((nextFrame - current) / (int) 1E6);
				} catch (InterruptedException ignored) {
				}
			}
			if (current > nextFPSFrame) {
				WindowManager.setWindowTitle("Hell's Alley - " + fps + " fps");
				nextFPSFrame += 1E9;
				fps = 0;
			}
			nextFrame += 1E9 / TARGET_FPS;
		}

		AudioManager.dispose();
		WindowManager.dispose();
		System.exit(0);
	}
	
	public static String readStream(InputStream is) throws IOException {
		return new String(bigBuf, 0, is.read(bigBuf));
	}
	
	public static float appTime() {
		return (System.currentTimeMillis()-firstMillis)/1000.f;
	}

	public static void exit() {
		WindowManager.setWindowShouldDispose(true);
	}

}
