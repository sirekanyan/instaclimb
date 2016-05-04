package me.vadik.instaclimb.model;

import android.database.Cursor;
import android.databinding.BindingConversion;

import me.vadik.instaclimb.android.CursorHelper;
import me.vadik.instaclimb.contract.ViewUsersRoutesContract;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class User {
    private final Integer id;
    private final String name;
    private String date;
    private boolean hasPicture;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Cursor cursor) {
        CursorHelper h = new CursorHelper(cursor);
        this.id = h.getInt(ViewUsersRoutesContract.USER_ID);
        this.name = h.getString(ViewUsersRoutesContract.USER_NAME);
        this.date = h.getString(ViewUsersRoutesContract.DATE);
        this.hasPicture = h.getBoolean(ViewUsersRoutesContract.HAS_PICTURE);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public boolean hasPicture() {
        return hasPicture;
    }

    public String getImageUrl() {
        return "https://vadik.me/userpic/" + String.valueOf(getId()) + ".jpg";
    }
}
