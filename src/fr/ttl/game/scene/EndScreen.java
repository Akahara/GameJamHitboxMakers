package fr.ttl.game.scene;

import fr.ttl.game.Main;
import fr.ttl.game.audio.Audio;
import fr.ttl.game.display.Renderer;
import fr.ttl.game.display.Textures;
import fr.ttl.game.math.Utils;

public class EndScreen extends Scene {
	
	int ending;
	Narator narator = new Narator();
	
	public EndScreen(int ending) {
		this.ending = ending;
		narator.loadEndDialog(ending);
		Audio.SOURCE_MUSIC.crossFade(Audio.MUSIC_ENDINGS[ending]);
		Utils.later(() -> {
			Audio.SOURCE_SFX.play(Audio.ENDINGS[ending]); 
			narator.trigger();
		}, 2.5f);
		Utils.later(() -> System.exit(0), Audio.MUSIC_ENDINGS[ending].getDuration() + 2.5f);
	}

	@Override
	public void initScene() {
	}

	@Override
	public void update(double delta) {
		
	}

	@Override
	public void render(double currentTime) {
		Renderer.setCamera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 1, 1);
		Renderer.drawQuad(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Textures.ENDING_SCREENS[ending]);
		narator.render(Main.appTime(), SCREEN_WIDTH*.2f);
	}

	@Override
	public void triggerPlayerKey(int key, int action) {
		
	}

	@Override
	public void triggerClick() {
		
	}

}
