package fr.ttl.game.scene;

import fr.ttl.game.Main;
import fr.ttl.game.SceneManager;
import fr.ttl.game.display.Color;
import fr.ttl.game.display.Renderer;
import fr.ttl.game.display.Textures;
import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.io.PlayerInputs;
import fr.ttl.game.math.Mathf;

public class DilemaScene extends Scene {

	private final int dilemaId;
	
	float cursorX, cursorY;
	int selectedResponse;
	boolean canInteract = true;
	float decay = -1;
	
	public DilemaScene(int dilemaId) {
		this.dilemaId = dilemaId;
	}
	
	@Override
	public void initScene() {
		
	}

	@Override
	public void update(double delta) {
		if(decay >= 0)
			decay += delta * .25f;
	}

	@Override
	public void render(double currentTime) {
		Renderer.setCamera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 1, 1);

		Renderer.BG_SHADER.bind();
		Renderer.BG_SHADER.setUniform1f("u_time", Main.appTime());
		Renderer.BG_SHADER.setUniform2f("u_dir", -1,1);
		Renderer.drawQuad(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null, null, Renderer.BG_SHADER);
		
		Texture texture = Textures.DILEMA_CHARACTERS[dilemaId];
		float boxHeight = SCREEN_HEIGHT * .6f;
		float boxWidth = boxHeight*texture.width/texture.height;
		Renderer.drawQuad((SCREEN_WIDTH*.5f-boxWidth)*.5f, (SCREEN_HEIGHT-boxHeight)*.5f, boxWidth, boxHeight, texture);
		
		float targetCursorX = 0;
		float targetCursorY = SCREEN_HEIGHT*((1+selectedResponse)*.15f+.05f);
		for(int i = 1; i < 4; i++) {
			float alpha = decay < 0 || i == selectedResponse+1 ? 1 : 1-decay;
			float lx = renderTextCentered(Textures.DILEMA_TEXTS[dilemaId][i], SCREEN_WIDTH*3/4.f, SCREEN_HEIGHT*(i*.15f+.05f), alpha);
			if(i == selectedResponse+1)
				targetCursorX = lx - .5f;
		}
		renderTextCentered(Textures.DILEMA_TEXTS[dilemaId][0], SCREEN_WIDTH*3/4f, SCREEN_HEIGHT*.8f, 1);

		cursorX = Mathf.lerp(.95f, targetCursorX, cursorX);
		cursorY = Mathf.lerp(.95f, targetCursorY, cursorY);
		
		Renderer.drawCursor(cursorX, cursorY);
	}
	
	public float renderTextCentered(Texture text, float x, float y, float alpha) {
		float textSize = .005f;
		float boxWidth = Math.min(text.width*textSize, SCREEN_WIDTH*.45f);
		float boxHeight = boxWidth*text.height/text.width;
		Renderer.drawQuad(
				x - boxWidth*.5f,
				y - boxHeight*.5f,
				boxWidth, boxHeight,
				text, new Color(1,1,1, alpha));
		return x - boxWidth*.5f;
	}

	@Override
	public void triggerPlayerKey(int key, int action) {
		if(action != PlayerInputs.ACTION_PRESS || !canInteract)
			return;
		if(key == PlayerInputs.KEY_DOWN)
			selectedResponse = Math.max(0, selectedResponse-1);
		if(key == PlayerInputs.KEY_UP)
		selectedResponse = Math.min(Player.GAUGE_COUNT-1, selectedResponse+1);
		if(key == PlayerInputs.KEY_RIGHT || key == PlayerInputs.KEY_SPACE)
			playResponse();
	}

	private void playResponse() {
		canInteract = false;
		decay = 0;
		Player.gauges[selectedResponse] = Math.min(Player.gauges[selectedResponse]+1, Player.GAUGE_MAX);
		SceneManager.switchScene(new WorldScene());
	}

	@Override
	public void triggerClick() {
		
	}

}
