package fr.ttl.game.scene;

import fr.ttl.game.Main;
import fr.ttl.game.SceneManager;
import fr.ttl.game.audio.Audio;
import fr.ttl.game.audio.Sound;
import fr.ttl.game.audio.SoundSource;
import fr.ttl.game.display.Color;
import fr.ttl.game.display.Renderer;
import fr.ttl.game.display.Textures;
import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.io.PlayerInputs;
import fr.ttl.game.math.Mathf;
import fr.ttl.game.math.Mathr;
import fr.ttl.game.math.Utils;

public class CombatScene extends Scene {
	
	int[] givenResources = new int[Player.GAUGE_COUNT];
	Enemy enemy;
	
	int worldId;
	
	float time;
	
	int selectedResource;
	float cursorPosition;
	
	float traderGlow;
	SoundSource traderSpeechSource; // bad but working

	static final Color[] GAUGE_COLORS = { new Color(0.197f, 0.293f, 0.812f), new Color(0.262f, 0.859f, 0.299f), new Color(0.850f, 0.859f, 0.262f) };
	static final Color GAUGE_EMPTY = new Color(.2f, .2f, .2f, .8f);
	
	static float[] gaugeFillLevels = new float[Player.GAUGE_COUNT];
	
	boolean canInteract = false;
	
	static final Enemy[] enemies = {
			new Enemy(Textures.TRADERS[0], 30, new float[] { 5, 8, 3 }),
			new Enemy(Textures.TRADERS[1], 30, new float[] { 3, 5, 8 }),
			new Enemy(Textures.TRADERS[2], 30, new float[] { 8, 3, 5 }),
			new Enemy(Textures.TRADERS[3], 30, new float[] { 7, 2, 7 }),
			new Enemy(Textures.TRADERS[4], 30, new float[] { 6, 6, 6 }),
	};
	
	@Override
	public void initScene() {
		worldId = Player.worldId;
		enemy = enemies[worldId];
		Audio.SOURCE_MUSIC.crossFade(Audio.MUSIC_COMBATS);
		traderSpeechSource = Audio.SOURCE_SFX.play(Audio.ENEMY_TRADE_ENCOUNTERS[worldId]);
		Utils.later(() -> canInteract = true, Audio.ENEMY_TRADE_ENCOUNTERS[worldId].getDuration());
	}

	@Override
	public void update(double delta) {
		cursorPosition = Mathf.lerp(.9f, selectedResource, cursorPosition);
		if(traderGlow > 0 || (traderSpeechSource != null && traderSpeechSource.isPlaying()))
			traderGlow += delta;
		if(traderGlow > 1)
			traderGlow = 0;
		time += delta;
	}

	@Override
	public void render(double currentTime) {
		for(int i = 0; i < Player.GAUGE_COUNT; i++)
			gaugeFillLevels[i] = Mathf.lerp(.9f, Player.gauges[i], gaugeFillLevels[i]);
		
		Renderer.setCamera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 1, 1);

		Renderer.BG_SHADER.bind();
		Renderer.BG_SHADER.setUniform1f("u_time", Main.appTime());
		Renderer.BG_SHADER.setUniform2f("u_dir", 1,1);
		Renderer.drawQuad(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null, null, Renderer.BG_SHADER);
		
		float available = SCREEN_WIDTH*.5f;
		float margin = available * .1f;
		float btnWidth = (available - 2*margin)*.15f;
		float slotWidth = (available - 2*margin) / Player.GAUGE_MAX;
		float slotHeight = .5f;
		float btnHeight = btnWidth;
		for(int i = 0; i < Player.GAUGE_COUNT; i++) {
			float y = SCREEN_HEIGHT*.9f - i * btnHeight*1.1f;
			Color c = new Color(Mathf.mix(Mathf.clamp(gaugeFillLevels[i], 0, 3.2f), 3.2f, 0, .0f, .8f), .2f, .2f, .8f);
			Renderer.drawQuad(margin, y-slotHeight*.5f, available-2*margin, slotHeight, GAUGE_COLORS[i]);
			Texture tex = Textures.GAUGES_NAMES[i];
			float textScale = .005f;
			float boxWidth = tex.width * textScale;
			float boxHeight = boxWidth*tex.height/tex.width;
			Renderer.drawQuad(margin+.01f, y-boxHeight*.5f, boxWidth, boxHeight, tex);
			Renderer.drawQuad(margin, y-slotHeight*.5f, slotWidth*(Player.GAUGE_MAX-gaugeFillLevels[i]), slotHeight, c);
			Renderer.drawQuad(available-margin-btnWidth, y-btnHeight*.5f, btnWidth, btnHeight, Textures.CURSOR, GAUGE_COLORS[i]);

		}
		
		{ // text
			float textMargin = margin;
			float textSize = .005f;
			Texture textTexture = Textures.TRADERS_TEXTS[worldId];
			float boxWidth = Math.min(textTexture.width*textSize, available-textMargin*2);
			float boxHeight = boxWidth*textTexture.height/textTexture.width;
			Renderer.drawQuad(textMargin, SCREEN_HEIGHT*.7f - textMargin - boxHeight, boxWidth, boxHeight, textTexture);
		}

		{ // trader
			float boxHeight = SCREEN_HEIGHT * enemy.texture.height/90.f;
			float boxWidth = boxHeight/enemy.texture.height*enemy.texture.width;
			float grow = traderGlow;
			float alpha = 1-grow;
			grow = Mathf.lerp(grow, 1f, 1.2f);
			Renderer.drawQuad(
					SCREEN_WIDTH*.5f + (SCREEN_WIDTH*.5f-boxWidth*grow)*.5f, 0,
					boxWidth*grow, boxHeight*grow,
					enemy.texture, new Color(1,1,1, alpha), Renderer.TRADER_GLOW_SHADER);
			Renderer.drawQuad(SCREEN_WIDTH*.5f + (SCREEN_WIDTH*.5f-boxWidth)*.5f, 0, boxWidth, boxHeight, enemy.texture);
		}
		
		{ // trader name
			float traderHeight = SCREEN_HEIGHT * enemy.texture.height/90.f;
			float textSize = .01f;
			Texture textTexture = Textures.TRADERS_NAMES[worldId];
			float boxWidth = textTexture.width*textSize;
			float boxHeight = boxWidth*textTexture.height/textTexture.width;
			Renderer.drawQuad(SCREEN_WIDTH*3/4f-boxWidth*.5f, traderHeight + .15f, boxWidth, boxHeight, textTexture);
		}
		
		Renderer.BLIT_SHADER.bind();
		Renderer.BLIT_SHADER.setUniform1f("u_minResource", Mathf.min(gaugeFillLevels));
		
		float y = SCREEN_HEIGHT*.9f - cursorPosition * btnHeight*1.1f;
		Renderer.drawCursor(available-margin-btnWidth*.5f, y);
	}
	
	private void playResource(int resource) {
		if(Player.gauges[resource] == 0)
			return;
		canInteract = false;
		Player.gauges[resource]--;
		givenResources[resource]++;
		
		Audio.SOURCE_SFX.play(Audio.SFX_TRADE);
		
		if(Player.gauges[resource] == 0) {
			Utils.later(() -> {
				SceneManager.switchScene(new DeathScene());
			}, 1);
			return;
		}
		
		float total = 0;
		for(int r = 0; r < Player.GAUGE_COUNT; r++)
			total += givenResources[r] * enemy.queryWeights[r];
		
		if(total > enemy.queriedTotal) {
			Sound sfx = Audio.ENEMY_TRADE_SUCCESS[worldId];
			if(sfx != null)
				traderSpeechSource = Audio.SOURCE_SFX.play(sfx);
			Utils.later(() -> {
				Player.worldId++;
				if(Player.worldId == 2)
					SceneManager.switchScene(new DilemaScene(0));
				else if(Player.worldId == 4)
					SceneManager.switchScene(new DilemaScene(1));
				else if(Player.worldId == 5)
					SceneManager.switchScene(new EndScreen(winConditionFulfilled() ? 1 : 0));
				else
					SceneManager.switchScene(new WorldScene());
			}, sfx == null ? 0 : sfx.getDuration() + .5f);
		} else {
			Sound sfx = Mathr.randIn(Audio.ENEMY_TRADE_NOT_ENOUGH[worldId]);
			traderSpeechSource = Audio.SOURCE_SFX.play(sfx);
			Utils.later(() -> canInteract = true, sfx.getDuration());
		}
	}
	
	private static boolean winConditionFulfilled() {
//		return Mathf.min(Player.gauges) > 2;
		return Mathf.sum(Player.gauges) >= 5;
	}

	@Override
	public void triggerPlayerKey(int key, int action) {
		if(action != PlayerInputs.ACTION_PRESS || !canInteract)
			return;
		if(key == PlayerInputs.KEY_DOWN)
			selectedResource = Math.min(Player.GAUGE_COUNT-1, selectedResource+1);
		if(key == PlayerInputs.KEY_UP)
			selectedResource = Math.max(0, selectedResource-1);
		if(key == PlayerInputs.KEY_RIGHT || key == PlayerInputs.KEY_SPACE)
			playResource(selectedResource);
	}

	@Override
	public void triggerClick() {
		if(!canInteract)
			return;

		float available = SCREEN_WIDTH*.5f;
		float margin = available * .1f;
		float btnWidth = (available - 2*margin)*.15f;
		float btnHeight = btnWidth;
		for(int i = 0; i < Player.GAUGE_COUNT; i++) {
			float y = SCREEN_HEIGHT*.9f - i * btnHeight*1.1f;
			if(Utils.isInBox(cursorX, cursorY, available-margin-btnWidth, y-btnHeight*.5f, btnWidth, btnHeight)) {
				playResource(i);
			}
		}
	}
}
