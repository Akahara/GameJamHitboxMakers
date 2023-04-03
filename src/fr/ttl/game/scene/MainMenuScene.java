package fr.ttl.game.scene;

import fr.ttl.game.SceneManager;
import fr.ttl.game.audio.Audio;
import fr.ttl.game.display.Renderer;
import fr.ttl.game.display.Textures;
import fr.ttl.game.io.PlayerInputs;

public class MainMenuScene extends Scene {

	float time = 0;
	
	@Override
	public void initScene() {
		Audio.SOURCE_MUSIC.crossFade(Audio.MUSIC_MAIN_MENU);
	}

	@Override
	public void update(double delta) {
		time += delta;
	}

	@Override
	public void render(double currentTime) {
		Renderer.setCamera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 1, 1);
		Renderer.drawQuad(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Textures.LEVEL_BBG[2]);
		Renderer.drawQuad(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Textures.MENU_BG);
	}

	@Override
	public void triggerPlayerKey(int key, int action) {
		if(time > 4 && action == PlayerInputs.ACTION_PRESS && (key == PlayerInputs.KEY_SPACE || key == PlayerInputs.KEY_RIGHT)) {
			time = -10000;
			SceneManager.switchScene(new WorldScene());
		}
	}

	@Override
	public void triggerClick() {
		
	}
	
}
