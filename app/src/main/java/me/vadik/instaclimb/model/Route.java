package me.vadik.instaclimb.model;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class Route {
    private Integer id;
    private String name;
    private String date;

    public Route(Integer id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
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
}
