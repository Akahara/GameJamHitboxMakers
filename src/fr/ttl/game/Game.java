package fr.ttl.game;

import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.scene.Enemy;

public class Game {

	public static final int WORLD_COUNT = 5;
	
	public static final Enemy[] ENEMIES = new Enemy[] {
			new Enemy(Texture.DUMMY_TEXTURE, 30, new float[] { 2, 4, 5 }),
			new Enemy(Texture.DUMMY_TEXTURE, 30, new float[] { 2, 4, 5 }),
			new Enemy(Texture.DUMMY_TEXTURE, 30, new float[] { 2, 4, 5 }),
			new Enemy(Texture.DUMMY_TEXTURE, 30, new float[] { 2, 4, 5 }),
			new Enemy(Texture.DUMMY_TEXTURE, 30, new float[] { 2, 4, 5 }),
	};
	
	static {
		if(ENEMIES.length != WORLD_COUNT)
			throw new IllegalStateException();
	}
	
}
