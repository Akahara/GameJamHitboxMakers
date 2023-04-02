package fr.ttl.game.scene;

import java.util.ArrayList;
import java.util.List;

import fr.ttl.game.Main;
import fr.ttl.game.SceneManager;
import fr.ttl.game.audio.Audio;
import fr.ttl.game.display.Color;
import fr.ttl.game.display.Renderer;
import fr.ttl.game.display.Textures;
import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.io.PlayerInputs;
import fr.ttl.game.math.Mathf;
import fr.ttl.game.math.Mathr;
import fr.ttl.game.math.Utils;

public class WorldScene extends Scene {

	float playerX, camX;
	float playerXVel;
	float playerXKeyVel;
	boolean playerIsForward = true;
	float playerSpeed;
	static final float playerY = SCREEN_HEIGHT * 22/90;
	static final float PLAYER_SCREEN_OFFSET = 2;
	static final float enemyWidth = 2, enemyHeight = 3;
	Narator narator = new Narator();
	float footstepSoundDelay = 0;
	
	List<Trigger> remainingTriggers = new ArrayList<>();
	List<ParalaxItem> bgParalaxItems = new ArrayList<>();
	List<ParalaxItem> fgParalaxItems = new ArrayList<>();
	
	@Override
	public void initScene() {
		Audio.SOURCE_MUSIC.crossFade(Audio.MUSIC_OVERWORLDS[Player.worldId]);
		narator.loadDialog(Player.worldId);
		
		Player.visitedWorldCounts[Player.worldId]++;
		
		if(Player.worldId == 0)
			Audio.SOURCE_WATER.play();
		
		playerSpeed = SCREEN_WIDTH * 5 / narator.speech.getDuration();

		Texture prev = null;
		for(int i = -6; i < 30; i++) {
			ParalaxItem item = new ParalaxItem(true, Mathf.random()*2 + 4*i);
			do {
				item.texture = Mathr.randIn(Textures.PARALAX_ITEMS[Player.worldId]);
			} while(item.texture == prev && Textures.PARALAX_ITEMS[Player.worldId].length>1);
			prev = item.texture;
			fgParalaxItems.add(item);
		}
		if(Player.worldId != 3)
		for(int i = -5; i < 60; i++) {
			ParalaxItem item = new ParalaxItem(false, Mathf.random()*2 + 2*i);
			item.y = 2;
			item.texture = Mathr.randIn(Textures.PARALAX_ITEMS[Player.worldId]);
			bgParalaxItems.add(item);
		}
		
		// narator trigger
		remainingTriggers.add(new Trigger(playerSpeed * 3, () -> {
			narator.trigger();
		}));
		// next scene trigger
		remainingTriggers.add(new Trigger(SCREEN_WIDTH*5.7f, () -> {
			SceneManager.switchScene(new CombatScene());
			playerXKeyVel = 0;
			
			int C = 10;
			float duration = 1.f;
			for(int i = 0; i < C; i++) {
				float ii = i;
				Utils.later(() -> Audio.SOURCE_WATER.setVolume(1-(float)ii/C), duration*(float)i/C);
			}
			Utils.later(() -> Audio.SOURCE_WATER.pause(), duration);
		}));
	}
	
	@Override
	public void update(double delta) {
		playerXVel = Mathf.lerp(.9f, playerXKeyVel, playerXVel);
		
		playerX += playerXVel * playerSpeed * delta;
//		playerX = Math.max(playerX, -5);
		if(playerX < 0)
			playerXVel += -playerX*delta*2f;
		
		footstepSoundDelay -= delta;
		if(footstepSoundDelay < 0 && playerXKeyVel != 0) {
			Audio.SOURCE_SFX.playRandom(Player.worldId >= 3 ? Audio.SFX_FOOTSTEPS_INDORS : Audio.SFX_FOOTSTEPS_OUTDORS);
			footstepSoundDelay = .5f;
		}
		
		// trigger triggers
		for(int i = 0; i < remainingTriggers.size(); i++) {
			var t = remainingTriggers.get(i);
			if(playerX > t.playerXThreshold)
				remainingTriggers.remove(i--).action.run();
		}
		
		if(Player.worldId == 0) {
			Audio.SOURCE_WATER.setVolume(Math.max(1 - Math.abs(SCREEN_WIDTH*6 - playerX) / SCREEN_WIDTH / 2, 0));
		}
	}

	@Override
	public void render(double currentTime) {
		camX = Mathf.lerp(.98f, playerX-PLAYER_SCREEN_OFFSET, camX);
		camX = Math.min(camX, 5*SCREEN_WIDTH);
		
		Renderer.setCamera(camX, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 1, 1);

		Renderer.drawQuad(camX, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Textures.LEVEL_BBG[Player.worldId]);
		
		for(ParalaxItem item : bgParalaxItems)
			item.render(camX * item.paralaxFactor);
		
		for(float i = -1; i < 5; i++)
			Renderer.drawQuad(SCREEN_WIDTH*i, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Textures.TERRAIN_TEXTURES[Player.worldId]);
		Renderer.drawQuad(SCREEN_WIDTH*5, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Textures.TERRAIN_ENDS[Player.worldId]);
		if(Player.worldId == 1)
			Renderer.drawQuad(-5, 0, SCREEN_WIDTH, SCREEN_HEIGHT, Textures.LEVEL_2_BEGINING);
		
		Texture playerFrame = Math.abs(playerXVel) < .1f ?
				Textures.PLAYER_FRAMES[0] :
				Utils.pickAnim(Textures.PLAYER_FRAMES, (float)currentTime, .3f);
		Renderer.drawQuad(playerX, playerY, SCREEN_WIDTH*playerFrame.width/160, SCREEN_HEIGHT/90*playerFrame.height, playerIsForward?0:1, 0, playerIsForward?1:-1, 1, playerFrame);
		
		if(Player.worldId == 2) {
			float t = (float)currentTime*5f;
			float tx = (float) (Math.floor(t)*.2f);
			float ty = (float) (Math.floor(t)*.35f);
			Renderer.drawQuad(camX, 0, SCREEN_WIDTH, SCREEN_HEIGHT, tx, ty, 1, 1, Textures.SNOW);
		}
		
		for(ParalaxItem item : fgParalaxItems)
			item.render(camX * item.paralaxFactor);
		
		narator.render(Main.appTime(), camX);
	}

	@Override
	public void triggerPlayerKey(int key, int action) {
		playerXKeyVel = PlayerInputs.isKeyDown(PlayerInputs.KEY_LEFT) ? -1 : 
			(PlayerInputs.isKeyDown(PlayerInputs.KEY_RIGHT) ? +1 : 0);
		if(playerXKeyVel != 0)
			playerIsForward = playerXKeyVel == +1;
	}
	
	@Override
	public void triggerClick() {
		
	}
	
	private class Trigger {
		
		public float playerXThreshold;
		public Runnable action;
		
		public Trigger(float playerXThreshold, Runnable action) {
			this.playerXThreshold = playerXThreshold;
			this.action = action;
		}
		
	}
	
	private class ParalaxItem {

		static final float SIZE_FACTOR = SCREEN_HEIGHT/90.f;
		public float x;
		public float y;
		public Texture texture;
		public float paralaxFactor;
		Color color;
		
		public ParalaxItem(boolean fg, float x) {
			color = new Color(fg ?
					Mathf.lerp(Mathf.random(), .7f, .8f) :
					Mathf.lerp(Mathf.random(), .9f, 1f));
			paralaxFactor = fg ? -.3f : .7f;
			this.x = x;
		}
		
		public void render(float offset) {
			Renderer.drawQuad(x-texture.width*.5f*SIZE_FACTOR + offset, y, texture.width*SIZE_FACTOR, texture.height*SIZE_FACTOR, texture, color);
		}
		
	}

}
