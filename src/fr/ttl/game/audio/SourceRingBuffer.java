package fr.ttl.game.audio;

import fr.ttl.game.math.Mathr;

public class SourceRingBuffer {
	
	private final SoundSource[] sources;
	
	public SourceRingBuffer(int sourceCount) {
		this.sources = new SoundSource[sourceCount];
		for(int i = 0; i < sourceCount; i++)
			sources[i] = new SoundSource();
	}
	
	public SoundSource play(Sound sound) {
		for(int i = 0; i < sources.length; i++) {
			if(!sources[i].isPlaying()) {
				sources[i].setSound(sound).play();
				return sources[i];
			}
		}
		return null;
	}
	
	public void playRandom(Sound[] pool) {
		play(Mathr.randIn(pool));
	}

}
