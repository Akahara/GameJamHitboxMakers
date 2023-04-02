package fr.ttl.game.display;

public class Color {
	
	public static final Color white = new Color(1, 1, 1);
	public static final Color black = new Color(0, 0, 0);
	public static Color grey = new Color(.7f, .7f, .7f);
	public static Color red = new Color(1, 0, 0);
	public static Color yellow = new Color(1, 1, 0);
	public static Color pink = new Color(.8f, .2f, .8f);
	public static Color purple = new Color(.8f, .35f, 1f);
	public static final Color darkGreen = new Color(0, .8f, 0);
	public static final Color blue = new Color(0, 0, 1);

	public final float r;
	public final float g;
	public final float b;
	public final float a;
	
	public Color(float rgb) {
		this.r = this.g = this.b = rgb;
		this.a = 1;
	}

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 1);
	}

	public Color withAlpha(float a) {
		return new Color(r, g, b, a);
	}
	
	public static Color fromHSV(float hue, float saturation, float value) {
		int h = (int)(hue * 6);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);
		
		switch (h) {
		case 0: return new Color(value, t, p);
		case 1: return new Color(q, value, p);
		case 2: return new Color(p, value, t);
		case 3: return new Color(p, q, value);
		case 4: return new Color(t, p, value);
		case 5: return new Color(value, p, q);
		default: return black;
		}
	}
}
