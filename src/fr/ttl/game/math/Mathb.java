package fr.ttl.game.math;

public class Mathb {

    public static int count1bits(int x) {
        int c;
        for(c = 0; x != 0; c++)
            x = x & (x-1);
        return c;
    }

    public static int leastSignificantBit(int x) {
        return x & (-x);
    }

    public static int positionOfLeastSignificantBit(int x) {
        if(x == 0)
            return -1;
        int p;
        for(p = 0; (x&1) == 0; p++)
            x >>= 1;
        return p;
    }

}
