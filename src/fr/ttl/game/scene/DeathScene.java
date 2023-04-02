package fr.ttl.game.scene;

import fr.ttl.game.SceneManager;
import fr.ttl.game.audio.Audio;
import fr.ttl.game.audio.Sound;
import fr.ttl.game.display.Color;
import fr.ttl.game.display.Renderer;
import fr.ttl.game.display.Textures;
import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.math.Mathf;
import fr.ttl.game.math.Mathr;
import fr.ttl.game.math.Utils;

public class DeathScene extends Scene {

	float time = 0;
	Texture textTexture = Player.loop < Textures.DEATH_TEXTS.length ? Textures.DEATH_TEXTS[Player.loop] : Mathr.randIn(Textures.DEATH_TEXTS);
	float textSize = .01f;
	
	Sound narrated = Mathr.randIn(Audio.SFX_DEATH);
	
	@Override
	public void initScene() {
		Player.loop++;
		Utils.later(() -> {
			Audio.SOURCE_SFX.play(narrated);
			Utils.later(() -> {
				Player.reset();
				SceneManager.switchScene(new WorldScene());
			}, narrated.getDuration() + 1);
		}, SceneManager.TRANSITION_DURATION);
	}

	@Override
	public void update(double delta) {
		time += delta;
	}

	@Override
	public void render(double currentTime) {
		Renderer.setCamera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 1, 1);
		float alpha = Mathf.windowPass(time, 1, narrated.getDuration(), 1);
		alpha = (float)Math.sqrt(alpha);
		Renderer.drawQuad(
				(SCREEN_WIDTH-textSize*textTexture.width)*.5f,
				(SCREEN_HEIGHT-textSize*textTexture.height)*.5f,
				textSize*textTexture.width, textSize*textTexture.height,
				textTexture, new Color(1,1,1, alpha));
	}

	@Override
	public void triggerPlayerKey(int key, int action) {
		
	}

	@Override
	public void triggerClick() {
		
	}

}
