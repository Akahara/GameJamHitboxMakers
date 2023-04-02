package fr.ttl.game.display;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.ttl.game.Logger;
import fr.ttl.game.display.internal.Texture;

public class Textures {

	public static final Texture[] TERRAIN_TEXTURES = loadTextures("level_%d_bg.png");
	public static final Texture[] TERRAIN_ENDS = loadTextures("level_%d_end.png");
	public static final Texture LEVEL_2_BEGINING = loadTexture("level_2_begining.png");
	public static final Texture[] GAUGES_NAMES = loadTextures("/texts/gaugesnames/text_%d.png", false);
	public static final Texture[] PLAYER_FRAMES = loadTextures("player_ (%d).png");
	public static final Texture[] LEVEL_BBG = loadTextures("level_%d_bbg.png");
	public static final Texture[][] PARALAX_ITEMS = { loadTextures("feature_level_1  (%d).png"), null, loadTextures("feature_level_2 (%d).png"), loadTextures("feature_level_3 (%d).png"), loadTextures("feature_level_4 (%d).png") };
	static { PARALAX_ITEMS[1] = PARALAX_ITEMS[0]; }
	public static final Texture[] DEATH_TEXTS = loadTextures("/texts/death/text_%d.png", false);
	public static final Texture[] TRADERS = loadTextures("trader_%d.png");
	public static final Texture[] TRADERS_TEXTS = loadTextures("/texts/trader%d/text.png", false);
	public static final Texture NOISE = loadTexture("noise2.png");
	public static final Texture CURSOR = loadTexture("cursor.png");
	public static final Texture MENU_BG = loadTexture("menu_bg.png");
	public static final Texture[] ENDING_SCREENS = loadTextures("end_screen (%d).png");
	public static final Texture SNOW = loadTexture("snow.png");
	public static final Texture[] DILEMA_CHARACTERS = { loadTexture("ghost.png"), loadTexture("ghost.png") };
	public static final Texture[] TRADERS_NAMES = loadTextures("/texts/tradernames/text_%d.png", false);
	public static final Texture[][] DILEMA_TEXTS = { loadTextures("/texts/dilema1/text_%d.png", false), loadTextures("/texts/dilema2/text_%d.png", false), };
	
	static {
//		if(TERRAIN_TEXTURES.length != Game.WORLD_COUNT)
//			throw new IllegalStateException();
//		if(PARALAX_ITEMS.length != Game.WORLD_COUNT)
//			throw new IllegalStateException();
//		if(TRADERS.length != Game.WORLD_COUNT)
//			throw new IllegalStateException();

		for(Texture t : GAUGES_NAMES)
			t.enableAntialiasing();
		for(Texture t : DEATH_TEXTS)
			t.enableAntialiasing();
		for(Texture[] tt : DILEMA_TEXTS)
			for(Texture t : tt)
				t.enableAntialiasing();
		for(Texture t : List.of(NOISE, CURSOR))
			t.enableAntialiasing();
		SNOW.enableWrapping();
	}
	
    public static void loadTextures() {}

    private static Texture[] loadTextures(String path, boolean commonRoot) {
    	List<Texture> textures = new ArrayList<>();
    	Texture tex0 = loadTexture(String.format(path, 0), commonRoot, true);
    	if(tex0 != Texture.DUMMY_TEXTURE)
    		textures.add(tex0);
    	for(int i = 1; ; i++) {
    		Texture tex = loadTexture(String.format(path, i), commonRoot, true);
    		if(tex == Texture.DUMMY_TEXTURE)
    			break;
			textures.add(tex);
    	}
    	if(textures.isEmpty()) {
    		textures.add(Texture.DUMMY_TEXTURE);
    		Logger.err("No texture found using " + path);
    	}
    	return textures.toArray(Texture[]::new);
    }
    
    private static Texture[] loadTextures(String path) {
    	return loadTextures(path, true);
    }
    
    private static Texture loadTexture(String path, boolean useCommonPath) {
    	return loadTexture(path, useCommonPath, false);
    }
    
    private static Texture loadTexture(String path, boolean useCommonPath, boolean silence) {
        try {
            return Texture.loadTexture(useCommonPath ? "/textures/" + path : path);
        } catch (IOException e) {
        	if(!silence)
        		Logger.err(e.getMessage());
            return Texture.DUMMY_TEXTURE;
        }
    }

    private static Texture loadTexture(String path) {
    	return loadTexture(path, true);
    }

}
