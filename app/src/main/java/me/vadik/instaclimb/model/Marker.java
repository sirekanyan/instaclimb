package me.vadik.instaclimb.model;

/**
 * User: vadik
 * Date: 5/2/16
 */
public class Marker {
    private final int color1;
    private final int color2;
    private final int color3;

    public Marker(int color1, int color2, int color3) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
    }

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }

    public int getColor3() {
        return color3;
    }
}
