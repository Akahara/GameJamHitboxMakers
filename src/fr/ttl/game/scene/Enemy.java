package fr.ttl.game.scene;

import fr.ttl.game.display.internal.Texture;

public class Enemy {
	
	public float[] queryWeights;
	public float queriedTotal;
	public Texture texture;
	
	public Enemy(Texture texture, float queriedTotal, float[] queryWeights) {
		this.texture = texture;
		this.queryWeights = queryWeights;
		this.queriedTotal = queriedTotal;
		if(queryWeights.length != Player.GAUGE_COUNT)
			throw new IllegalArgumentException();
	}
	
}
