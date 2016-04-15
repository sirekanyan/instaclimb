package me.vadik.instaclimb.model;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class Route {
    private final Integer id;
    private final String name;
    private final String date;
    private final int color1;
    private final int color2;
    private final int color3;
    private String grade;
    private final boolean flash;

    public Route(Integer id, String name, String date, int c1, int c2, int c3, String grade, boolean isFlash) {
        this.id = id;
        this.name = name;
        this.date = date;
        color1 = c1;
        color2 = c2;
        color3 = c3;
        this.grade = grade;
        this.flash = isFlash;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Integer getId() {
        return id;
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

    public String getGrade() {
        return grade;
    }

    public boolean isFlash() {
        return flash;
    }
}
