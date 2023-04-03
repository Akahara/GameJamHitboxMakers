package fr.ttl.game.scene;

import fr.ttl.game.Game;
import fr.ttl.game.audio.Audio;
import fr.ttl.game.display.Renderer;

public class Player {
	
	public static final int GAUGE_COUNT = 3;
	public static final int GAUGE_MAX = 10;
	
	public static int worldId;
	public static int[] gauges = new int[GAUGE_COUNT];
	public static int loop = 0;
	
	public static int[] visitedWorldCounts = new int[Game.WORLD_COUNT];
	
	static {
		reset();
	}
	
	public static void reset() {
		worldId = 0;
		for(int i = 0; i < GAUGE_COUNT; i++)
			gauges[i] = GAUGE_MAX;
		
		Renderer.BLIT_SHADER.bind();
		Renderer.BLIT_SHADER.setUniform1f("u_minResource", GAUGE_MAX);
		Audio.SOURCE_LOW_RESOURCES.setVolume(0);
	}
	
	
}
