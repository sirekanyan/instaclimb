package me.vadik.instaclimb.model;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class User {
    private final Integer id;
    private final String name;
    private final boolean isFlash;
    private final String date;

    public User(Integer id, String name, boolean isFlash, String date) {
        this.id = id;
        this.name = name;
        this.isFlash = isFlash;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isFlash() {
        return isFlash;
    }

    public String getDate() {
        return date;
    }
}
