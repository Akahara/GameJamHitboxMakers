package fr.ttl.game.scene;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.ttl.game.Logger;
import fr.ttl.game.Main;
import fr.ttl.game.audio.Audio;
import fr.ttl.game.audio.Sound;
import fr.ttl.game.display.Color;
import fr.ttl.game.display.Renderer;
import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.math.Mathf;

public class Narator {

	static final float TEXT_SCALE = .006f;
	public boolean triggered = false;
	float triggerTime;
	Sound speech;
	
	List<Sentence> sentences = new ArrayList<>();
	
	public void trigger() {
		triggered = true;
		triggerTime = Main.appTime();
		Audio.SOURCE_SFX.play(speech);
	}

	public float getSpeechDuration() {
		return speech.getDuration();
	}
	
	public void loadDialog(Sound speech, String naratorFile) {
		String timestampPath = "/texts/narator" + naratorFile + "/texts.txt";
		String texturePath = "/texts/narator" + naratorFile + "/text_%d.png";
		
		this.speech = speech;
		
		try (InputStream is = Narator.class.getResourceAsStream(timestampPath)) {
			if(is == null)
				throw new IllegalArgumentException("no could not find timestamps at '" + timestampPath + "'");
			
			String[] lines = Main.readStream(is).trim().split("\n");
			for(int i = 0; i < lines.length; i++) {
				String[] parts = lines[i].split(" ");
				float tsIn = Float.parseFloat(parts[0]);
				float tsOut = Float.parseFloat(parts[1]);
				Sentence s = new Sentence();
				s.popInTimestamp = tsIn;
				s.popOutTimestamp = tsOut;
				s.textTexture = Texture.loadTexture(String.format(texturePath, i));
				s.textTexture.enableAntialiasing();
				sentences.add(s);
			}
		} catch (IOException e) {
			Logger.merr(e, "could not load dialog " + naratorFile);
		}
	}
	
	public void loadDialog(int world) {
		int loop = Player.visitedWorldCounts[world] % 4;
		String naratorFile = loop + "_" + world;
		
		loadDialog(Audio.NARATOR_DIALOGS[world][loop], naratorFile);
	}
	
	public void loadEndDialog(int ending) {
		loadDialog(Audio.ENDINGS[ending], "_end"+ending);
	}
	
	public void render(float time, float xoffset) {
		if(!triggered)
			return;
		time -= triggerTime;
		for(Sentence s : sentences) {
			float alpha = Mathf.windowPass(time, s.popInTimestamp, s.popOutTimestamp, 1);
			if(alpha > 0) {
				alpha = (float)Math.pow(alpha, 1/3.f);
				float boxWidth = s.textTexture.width * TEXT_SCALE;
				float boxHeight = s.textTexture.height * TEXT_SCALE;
				Renderer.drawQuad(
						xoffset + (Scene.SCREEN_WIDTH-boxWidth)*.5f,
						(Scene.SCREEN_HEIGHT-boxHeight)*.5f,
						boxWidth, boxHeight,
						s.textTexture, new Color(1, 1, 1, alpha));
			}
		}
	}
	
	public static class Sentence {
		Texture textTexture;
		float popInTimestamp;
		float popOutTimestamp;
	}
	
}
