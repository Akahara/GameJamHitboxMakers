package fr.ttl.game.audio;

import java.util.Arrays;

import fr.ttl.game.Game;

public class Audio {

	public static final Sound[] SFX_FOOTSTEPS_OUTDORS = loadSounds("GJ - Pas %d.ogg");
	public static final Sound[] SFX_FOOTSTEPS_INDORS = loadSounds("GJ - Pas %d interieur.ogg");
	public static final Sound SFX_TRADE = loadSound("GJ - click ressources.ogg");
	public static final Sound[] SFX_FIGHT_TRANSITIONS = loadSounds("GJ - Transition combat %d.ogg");
	public static final Sound[] SFX_DEATH = loadSounds("GJ - Narrat Game over %d.ogg");
	public static final Sound SFX_WATER = loadSound("GJ - Bruit d'eau.ogg");
	public static final Sound SFX_BEATING_HEART = loadSound("GJ - Amb alerte ressources.ogg");
	public static Sound[] ENEMY_TRADE_SUCCESS = loadSounds("GJ - Boss %d oui.ogg");
	static { ENEMY_TRADE_SUCCESS = Arrays.copyOf(ENEMY_TRADE_SUCCESS, 5); }
	public static final Sound[][] ENEMY_TRADE_NOT_ENOUGH = { loadSounds("GJ - Charon non %d.ogg"), loadSounds("GJ - Hel non %d.ogg"), loadSounds("GJ - Anubis non %d.ogg"), loadSounds("GJ - Belzebuth non %d.ogg"), loadSounds("GJ - La faucheuse non %d.ogg"), };
	public static final Sound[][] NARATOR_DIALOGS = loadDialogs();
	public static final Sound[] ENEMY_TRADE_ENCOUNTERS = loadSounds("GJ - Boss %d narrat.ogg");
	public static final Sound[] ENDINGS = loadSounds("GJ - Narrat Fin %d.ogg");
	
	public static final Sound[] MUSIC_OVERWORLDS = loadSounds("Game Jam Amb %d.ogg");
	public static final Sound MUSIC_MAIN_MENU = loadSound("09 - Bat - Game Jam Ecran accueil.ogg");
	public static final Sound[] MUSIC_ENDINGS = loadSounds("Game Jam Thème fin %d.ogg");
	public static final Sound MUSIC_COMBATS = loadSound("06 - Bat - Game Jam Thème combat.ogg");
	
	static {
		if(MUSIC_OVERWORLDS.length != Game.WORLD_COUNT)
			throw new IllegalStateException();
		if(ENEMY_TRADE_SUCCESS.length != Game.WORLD_COUNT)
			throw new IllegalStateException();
		if(ENEMY_TRADE_NOT_ENOUGH.length != Game.WORLD_COUNT)
			throw new IllegalStateException();
		if(NARATOR_DIALOGS.length != Game.WORLD_COUNT)
			throw new IllegalStateException();
	}
	
	public static final MusicSource SOURCE_MUSIC = new MusicSource();
	public static final SourceRingBuffer SOURCE_SFX = new SourceRingBuffer(6);
	public static final SoundSource SOURCE_LOW_RESOURCES = new SoundSource().setLooping(true).setSound(SFX_BEATING_HEART);
	public static final SoundSource SOURCE_WATER = new SoundSource().setLooping(true).setSound(SFX_WATER);
	
	/** Static initializer */
	public static void loadResources() {}

	private static Sound loadSound(String path) {
		return Sound.loadSound("/sounds/" + path);
	}
	
	private static Sound[] loadSounds(String path) {
		return Sound.loadSounds("/sounds/" + path);
	}
	
	private static Sound[][] loadDialogs() {
		return new Sound[][] {
			loadSounds("GJ - Narrat 1.%d.ogg"),
			loadSounds("GJ - Narrat 2.%d.ogg"),
			loadSounds("GJ - Narrat 3.%d.ogg"),
			loadSounds("GJ - Narrat 4.%d.ogg"),
			loadSounds("GJ - Narrat 5.%d.ogg"),
		};
//		Sound[] iter1 = loadSounds("GJ - Narrat %d.1.ogg");
//		List<Sound[]> sounds = new ArrayList<>();
//		sounds.add(iter1);
//		for(int i = 2; ; i++) {
//			try {
//				sounds.add(loadSounds("GJ - Narrat %d." + i + ".ogg"));
//			} catch (IllegalArgumentException e) {
//				break;
//			}
//		}
//		return sounds.toArray(Sound[][]::new);
	}
	
	public static class MusicSource {
		
		SoundSource[] sources = new SoundSource[] {
				new SoundSource().setLooping(true),
				new SoundSource().setLooping(true),
		};
		
		static final float CROSS_FADE_DURATION = 1.f;
		int nextSource = 0;
		long fadeTs = System.nanoTime() - (long) (CROSS_FADE_DURATION*1000);
		
		public void crossFade(Sound music) {
			fadeTs = System.currentTimeMillis();
			sources[nextSource].setSound(music).setVolume(1).play();
			nextSource = 1-nextSource;
			
//			sources[1-nextSource].setVolume(.0f);
		}
		
		public void play() {
			sources[0].play();
			sources[1].play();
		}
		
		public void update() {
			float timeSinceFade = (System.currentTimeMillis()-fadeTs)/1000.f;
			sources[nextSource].setVolume(Math.max(0, 1-timeSinceFade/CROSS_FADE_DURATION));
		}
		
	}
	
}
