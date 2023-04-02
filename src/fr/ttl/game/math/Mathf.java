package fr.ttl.game.math;

public class Mathf {

    public static final float PI = (float) Math.PI;
    public static final float TWOPI = 2*PI;

    public static float clamp(float x, float min, float max) {
        return Math.min(Math.max(x, min), max);
    }

    public static float sin(float x) {
        return (float) Math.sin(x);
    }

    public static float fract(float x) {
        return mod(x, 1);
    }

    public static double mod(double x, double m) {
        x %= m;
        return x < 0 ? x+m : x;
    }

    public static float mod(float x, float m) {
        x %= m;
        return x < 0 ? x+m : x;
    }

    public static int mod(int x, int m) {
        x %= m;
        return x < 0 ? x+m : x;
    }

    public static float frac(float x) {
        return mod(x, 1);
    }

    public static float smoothstep(float edge0, float edge1, float x) {
        x = clamp((x - edge0) / (edge1 - edge0), 0, 1);
        return x * x * (3 - 2 * x);
    }

    /**
     * Taken from <a href="https://github.com/Akahara/fr.wonder.commons.math/blob/master/src/fr/wonder/commons/math/Mathf.java">fr.wonder.commons.math</a>
     */
    public static float exp(double x) {
        x = 1d + x / 256d;
        x *= x; x *= x; x *= x; x *= x;
        x *= x; x *= x; x *= x; x *= x;
        return (float)x;
    }

    public static long randomLong() {
        return (long) (Long.MAX_VALUE * Math.random());
    }

    public static float random() {
        return (float) Math.random();
    }

    public static float lerpAngle(float r1, float r2, float t) {
        float d = twoPIToPMPIRange(mod(r1-r2, TWOPI));
        return twoPIToPMPIRange(mod(r1-t*d, TWOPI));
    }
    
    public static float lerp(float x, float a, float b) {
    	return a+(b-a)*x;
    }
    
    public static float mix(float x, float xmin, float xmax, float ymin, float ymax) {
    	return (x-xmin)/(xmax-xmin) * (ymax-ymin) + ymin;
    }

    /** transforms a value in range 0..2pi to the same value in range -pi..pi */
    public static float twoPIToPMPIRange(float r) {
        return r > PI ? r-TWOPI : r;
    }
    
    public static float windowPass(float x, float min, float max, float steepness) {
    	return clamp(((max-min)*.5f - Math.abs(x-(max+min)*.5f)) / steepness, 0, 1);
    }

	public static int max(int[] x) {
		int m = x[0];
		for(int i : x)
			m = Math.max(i, m);
		return m;
	}
	
	public static float min(float[] x) {
		float m = x[0];
		for(float i : x)
			m = Math.min(i, m);
		return m;
	}

	public static int min(int[] x) {
		int m = x[0];
		for(int i : x)
			m = Math.min(i, m);
		return m;
	}

	public static int sum(int[] x) {
		int t = 0;
		for(int i : x)
			t += i;
		return t;
	}

}
