package fr.ttl.game.math;

import java.util.List;
import java.util.Random;

import static fr.ttl.game.math.Mathf.*;

public class Mathr {

    /**
     * Returns a random number in range [0,1[
     * <p>
     * This method uses the standard {@link Random} implementation.
     *
     * @return a random value in range [0,1[
     */
    public static float rand() {
        return Mathf.random();
    }

    /**
     * Fast predictable PRNG.
     * <p>
     * This method takes a seed and creates a random number from it,
     * multiple calls to this method with the same inputs will always
     * result in the same output.
     * <p>
     * This method is often used in shader computing, where each pixel
     * on a screen must be assigned a random value.
     * <p>
     * See {@link #rand2(float)} for another similar method if another
     * random value is needed for every input value.
     *
     * @param s the seed
     * @return a random number from the given seed
     */
    public static float rand1(float s) {
        return fract(sin(s*12.9898f) * 43758.5453f);
    }

    /**
     * Fast predictable PRNG.
     * <p>
     * This method takes a seed and creates a random number from it,
     * multiple calls to this method with the same inputs will always
     * result in the same output.
     * <p>
     * This method is often used in shader computing, where each pixel
     * on a screen must be assigned a random value.
     * <p>
     * See {@link #rand1(float)} for another similar method if another
     * random value is needed for every input value.
     *
     * @param s the seed
     * @return a random number from the given seed
     */
    public static float rand2(float s) {
        return fract(sin(s*78.233f) * 43758.5453f);
    }

    /**
     * Returns a random signum (either +1 or -1)
     * @return a random signum
     */
    public static int randSign() {
        return random() < .5f ? -1 : +1;
    }

    /**
     * Returns a random number between -1 and 1
     * @return a random number between -1 and 1
     */
    public static float randSigned() {
        return 2*random()-1;
    }

    /**
     * Returns a random number between 0 and 2*PI
     * @return a random angle in radians
     */
    public static float randAngle() {
        return random()*TWOPI;
    }

    /**
     * Returns a random number between 0 and PI
     * @return a random angle in radians
     */
    public static float randHalfAngle() {
        return random()*PI;
    }

    /**
     * Returns a random number in the given range.
     * <p>
     * If {@code max<min} the output range is {@code [min, max[}<br>
     * If {@code max>min} the output range is {@code ]max, min]}<br>
     * If {@code max=min} the output will be {@code min}
     *
     * @param min minimum value of the range
     * @param max maximum value of the range
     * @return a random value in the given range
     */
    public static int randRange(int min, int max) {
        int i = (int) (random()*(max-min)+min);
        return i == max ? min : i;
    }


    /**
     * Returns a random number in the given range.
     * <p>
     * If {@code max<min} the output range is {@code [min, max[}<br>
     * If {@code max>min} the output range is {@code ]max, min]}<br>
     * If {@code max=min} the output will be {@code min}
     *
     * @param min minimum value of the range
     * @param max maximum value of the range
     * @return a random value in the given range
     */
    public static float randRange(float min, float max) {
        return random()*(max-min)+min;
    }

    public static <T> T randIn(List<T> list) {
        return list.get(randRange(0, list.size()));
    }
    
    public static <T> T randIn(T[] list) {
        return list[randRange(0, list.length)];
    }

}
